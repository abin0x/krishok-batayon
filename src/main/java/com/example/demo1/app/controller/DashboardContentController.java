package com.example.demo1.app.controller;

import com.example.demo1.app.config.NavigationManager;
import com.example.demo1.app.model.User;
import com.example.demo1.app.util.SessionManager;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DashboardContentController {

    private static final Path USERS_FILE = Path.of("src/main/resources/data/user_data.json");
    private static final Path WORKERS_FILE = Path.of("workers_data.json");
    private static final DateTimeFormatter WORK_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter WORK_PAID_AT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Locale LOCALE_BN = new Locale("bn", "BD");
    private static final DateTimeFormatter UPDATED_AT_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm:ss a", LOCALE_BN);

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final Gson gson = new Gson();
    private final DecimalFormat numberFormat = new DecimalFormat("#,##0");
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");

    private Timeline refreshTimer;

    @FXML private Label lblWelcomeUser;
    @FXML private Label lblNow;

    @FXML private Label lblTotalUsers;
    @FXML private Label lblUsersProfiled;
    @FXML private Label lblTotalLand;
    @FXML private Label lblTotalModules;
    @FXML private Label lblTotalUsersSummary;
    @FXML private Label lblUsersProfiledSummary;
    @FXML private Label lblTotalLandSummary;
    @FXML private Label lblTotalModulesSummary;

    @FXML private Label lblWorkerRecords;
    @FXML private Label lblPendingPayments;
    @FXML private Label lblCompletedPayments;
    @FXML private Label lblMonthlyLaborCost;

    @FXML private Label lblTodayActivities;
    @FXML private Label lblTodayCompleted;
    @FXML private Label lblCompletionRate;
    @FXML private ProgressBar pbCompletion;

    @FXML private Label lblTopWorkType;
    @FXML private Label lblLatestEntry;
    @FXML private Label lblDataFreshness;

    @FXML
    public void initialize() {
        User sessionUser = SessionManager.getLoggedInUser();
        String name = (sessionUser != null && sessionUser.getName() != null && !sessionUser.getName().isBlank())
                ? sessionUser.getName()
                : "কৃষক";
        lblWelcomeUser.setText("স্বাগতম, " + name);

        refreshDashboard();

        refreshTimer = new Timeline(
                new KeyFrame(Duration.seconds(20), e -> refreshDashboard())
        );
        refreshTimer.setCycleCount(Timeline.INDEFINITE);
        refreshTimer.play();
    }

    private void refreshDashboard() {
        List<User> users = loadUsers();
        WorkerMetrics workerMetrics = loadWorkerMetrics();

        long totalUsers = users.size();
        long profiledUsers = users.stream().filter(this::hasProfileData).count();
        double totalLandAmount = users.stream().mapToDouble(u -> parseDouble(u.getLandAmount())).sum();

        setIfPresent(lblTotalUsers, numberFormat.format(totalUsers));
        setIfPresent(lblUsersProfiled, numberFormat.format(profiledUsers));
        setIfPresent(lblTotalLand, decimalFormat.format(totalLandAmount) + " একর");
        setIfPresent(lblTotalModules, String.valueOf(NavigationManager.sidebarRoutes().size()));
        setIfPresent(lblTotalUsersSummary, numberFormat.format(totalUsers) + " অ্যাকাউন্ট");
        setIfPresent(lblUsersProfiledSummary, numberFormat.format(profiledUsers) + " প্রোফাইল");
        setIfPresent(lblTotalLandSummary, decimalFormat.format(totalLandAmount) + " একর");
        setIfPresent(lblTotalModulesSummary, NavigationManager.sidebarRoutes().size() + " ফিচার");

        lblWorkerRecords.setText(numberFormat.format(workerMetrics.totalRecords));
        lblPendingPayments.setText(numberFormat.format(workerMetrics.pendingCount) + " (৳ " + decimalFormat.format(workerMetrics.pendingAmount) + ")");
        lblCompletedPayments.setText(numberFormat.format(workerMetrics.completedCount));
        lblMonthlyLaborCost.setText("৳ " + decimalFormat.format(workerMetrics.currentMonthTotal));

        lblTodayActivities.setText(numberFormat.format(workerMetrics.todayTotal));
        lblTodayCompleted.setText(numberFormat.format(workerMetrics.todayCompleted));

        double completionRate = workerMetrics.todayTotal == 0
                ? 0.0
                : (workerMetrics.todayCompleted * 100.0) / workerMetrics.todayTotal;

        lblCompletionRate.setText(decimalFormat.format(completionRate) + "%");
        pbCompletion.setProgress(Math.min(1.0, Math.max(0.0, completionRate / 100.0)));

        lblTopWorkType.setText(workerMetrics.topWorkType);
        lblLatestEntry.setText(workerMetrics.latestEntrySummary);
        lblDataFreshness.setText(resolveDataFreshness());

        updateCurrentTime();
    }

    private void updateCurrentTime() {
        lblNow.setText(LocalDate.now().format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).withLocale(LOCALE_BN)));
    }

    private String resolveDataFreshness() {
        List<Path> files = List.of(USERS_FILE, WORKERS_FILE);
        LocalDateTime newest = null;

        for (Path file : files) {
            if (!Files.exists(file)) {
                continue;
            }
            try {
                LocalDateTime modifiedAt = LocalDateTime.ofInstant(
                        Files.getLastModifiedTime(file).toInstant(),
                        java.time.ZoneId.systemDefault()
                );
                if (newest == null || modifiedAt.isAfter(newest)) {
                    newest = modifiedAt;
                }
            } catch (IOException ignored) {
                // keep dashboard responsive even if file metadata is unavailable
            }
        }

        if (newest == null) {
            return "কোনো লোকাল ডেটাসেট পাওয়া যায়নি";
        }
        return "সর্বশেষ ডেটাসেট আপডেট: " + newest.format(UPDATED_AT_FORMAT);
    }

    private boolean hasProfileData(User user) {
        return hasText(user.getDivision())
                || hasText(user.getDistrict())
                || hasText(user.getUpazila())
                || parseDouble(user.getLandAmount()) > 0;
    }

    private List<User> loadUsers() {
        if (!Files.exists(USERS_FILE)) {
            return new ArrayList<>();
        }

        try {
            return objectMapper.readValue(USERS_FILE.toFile(), new TypeReference<List<User>>() {});
        } catch (IOException e) {
            return new ArrayList<>();
        }
    }

    private WorkerMetrics loadWorkerMetrics() {
        WorkerMetrics metrics = new WorkerMetrics();
        if (!Files.exists(WORKERS_FILE)) {
            metrics.topWorkType = "কোনো কার্যক্রম নেই";
            metrics.latestEntrySummary = "কোনো শ্রমিক রেকর্ড পাওয়া যায়নি";
            return metrics;
        }

        try (Reader reader = Files.newBufferedReader(WORKERS_FILE)) {
            JsonArray rows = gson.fromJson(reader, JsonArray.class);
            if (rows == null) {
                metrics.topWorkType = "কোনো কার্যক্রম নেই";
                metrics.latestEntrySummary = "কোনো শ্রমিক রেকর্ড পাওয়া যায়নি";
                return metrics;
            }

            LocalDate today = LocalDate.now();
            LocalDateTime latestEvent = null;
            String latestWorkerName = "-";
            Map<String, Integer> workTypeCounts = new HashMap<>();

            for (JsonElement row : rows) {
                if (!row.isJsonObject()) {
                    continue;
                }

                JsonObject obj = row.getAsJsonObject();
                metrics.totalRecords++;

                String status = getString(obj, "status").toLowerCase(Locale.ENGLISH);
                String workType = getString(obj, "workType");
                String workerName = getString(obj, "name");

                LocalDate workDate = parseWorkDate(getString(obj, "date"));
                double hours = getDouble(obj, "hours");
                double rate = getDouble(obj, "rate");
                double total = hours * rate;

                if (!workType.isBlank() && !"-".equals(workType)) {
                    workTypeCounts.merge(workType, 1, Integer::sum);
                }

                if ("completed".equals(status)) {
                    metrics.completedCount++;
                } else {
                    metrics.pendingCount++;
                    metrics.pendingAmount += total;
                }

                if (workDate != null && workDate.getYear() == today.getYear() && workDate.getMonth() == today.getMonth()) {
                    metrics.currentMonthTotal += total;
                }

                if (workDate != null && workDate.equals(today)) {
                    metrics.todayTotal++;
                    if ("completed".equals(status)) {
                        metrics.todayCompleted++;
                    }
                }

                LocalDateTime eventTime = parsePaidAt(getString(obj, "paidAt"));
                if (eventTime == null && workDate != null) {
                    eventTime = workDate.atStartOfDay();
                }

                if (eventTime != null && (latestEvent == null || eventTime.isAfter(latestEvent))) {
                    latestEvent = eventTime;
                    latestWorkerName = workerName;
                }
            }

            metrics.topWorkType = workTypeCounts.entrySet().stream()
                    .max(Comparator.comparingInt(Map.Entry::getValue))
                    .map(e -> e.getKey() + " (" + e.getValue() + ")")
                    .orElse("কোনো কার্যক্রম নেই");

            if (latestEvent == null) {
                metrics.latestEntrySummary = "সাম্প্রতিক কোনো কার্যক্রম লগ হয়নি";
            } else {
                metrics.latestEntrySummary = latestWorkerName + " | সময়: " + latestEvent.format(UPDATED_AT_FORMAT);
            }
        } catch (Exception e) {
            metrics.topWorkType = "উপলব্ধ নয়";
            metrics.latestEntrySummary = "শ্রমিক বিশ্লেষণ লোড করা যায়নি";
        }

        return metrics;
    }

    private String getString(JsonObject object, String key) {
        if (object == null || !object.has(key) || object.get(key).isJsonNull()) {
            return "-";
        }
        String value = object.get(key).getAsString();
        return value == null || value.isBlank() ? "-" : value.trim();
    }

    private double getDouble(JsonObject object, String key) {
        if (object == null || !object.has(key) || object.get(key).isJsonNull()) {
            return 0;
        }
        try {
            return object.get(key).getAsDouble();
        } catch (Exception ignored) {
            return 0;
        }
    }

    private LocalDate parseWorkDate(String value) {
        if (!hasText(value) || "-".equals(value)) {
            return null;
        }
        try {
            return LocalDate.parse(value, WORK_DATE_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime parsePaidAt(String value) {
        if (!hasText(value) || "-".equals(value)) {
            return null;
        }
        try {
            return LocalDateTime.parse(value, WORK_PAID_AT_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }

    private double parseDouble(String value) {
        if (!hasText(value)) {
            return 0;
        }
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private void setIfPresent(Label label, String value) {
        if (label != null) {
            label.setText(value);
        }
    }

    private static class WorkerMetrics {
        int totalRecords;
        int pendingCount;
        int completedCount;
        int todayTotal;
        int todayCompleted;
        double pendingAmount;
        double currentMonthTotal;
        String topWorkType;
        String latestEntrySummary;
    }
}
