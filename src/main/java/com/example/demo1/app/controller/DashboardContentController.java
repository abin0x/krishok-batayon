package com.example.demo1.app.controller;
import com.example.demo1.app.model.User;
import com.example.demo1.app.util.SessionManager;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.util.Duration;

import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// class shuru hoiche dashboard content controller
public class DashboardContentController {

    private static final Path WORKERS_FILE = Path.of("workers_data.json");
    private static final DateTimeFormatter WORK_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter WORK_PAID_AT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Locale LOCALE_BN = new Locale("bn", "BD");
    private static final DateTimeFormatter UPDATED_AT_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm:ss a", LOCALE_BN);
    private static final String DEFAULT_FARMER = "Farmer";
    private static final String UNKNOWN_PHONE = "Not set";
    private static final String UNKNOWN_SOIL = "Not set";
    private static final String UNKNOWN_LOCATION = "Location unavailable";
    private static final String NO_WORK_TYPE = "No activities found";
    private static final String NO_WORKER_RECORD = "No worker records found";
    private static final String NO_RECENT_ACTIVITY = "No recent activities logged";
    private static final String ANALYSIS_UNAVAILABLE = "Unavailable";
    private static final String ANALYSIS_LOAD_FAIL = "Could not load worker analytics";
    private static final String NO_LOCAL_DATASET = "No local dataset found";
    private static final String DATASET_UPDATED_PREFIX = "Latest dataset update: ";

    private final Gson gson = new Gson();//ata Gson instance for JSON parsing
    private final DecimalFormat numberFormat = new DecimalFormat("#,##0");//ata DecimalFormat for integer formatting(1000 -> 1,000)
    private final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");//ata DecimalFormat for decimal formatting(1234.567 -> 1,234.57)

    private Timeline refreshTimer;

    @FXML private Label lblWelcomeUser;
    @FXML private Label lblNow;

    @FXML private Label lblTotalLandSummary;

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
        User activeUser = resolveActiveUser();
        String name = (activeUser != null && hasText(activeUser.getName())) ? activeUser.getName() : DEFAULT_FARMER;
        lblWelcomeUser.setText("Welcome, " + name);

        refreshDashboard();

        refreshTimer = new Timeline(new KeyFrame(Duration.seconds(20), e -> refreshDashboard()));
        refreshTimer.setCycleCount(Timeline.INDEFINITE);
        refreshTimer.play();
    }

    private void refreshDashboard() {
        User activeUser = resolveActiveUser();
        WorkerMetrics workerMetrics = loadWorkerMetrics();

        double userLandAmount = activeUser == null ? 0 : parseDouble(activeUser.getLandAmount());
        setIfPresent(lblTotalLandSummary, decimalFormat.format(userLandAmount) + " acres");
        updateProfileCards(activeUser);

        lblWorkerRecords.setText(numberFormat.format(workerMetrics.totalRecords));
        lblPendingPayments.setText(numberFormat.format(workerMetrics.pendingCount) + " (Tk " + decimalFormat.format(workerMetrics.pendingAmount) + ")");
        lblCompletedPayments.setText(numberFormat.format(workerMetrics.completedCount));
        lblMonthlyLaborCost.setText("Tk " + decimalFormat.format(workerMetrics.currentMonthTotal));

        lblTodayActivities.setText(numberFormat.format(workerMetrics.todayTotal));
        lblTodayCompleted.setText(numberFormat.format(workerMetrics.todayCompleted));

        double completionRate = workerMetrics.todayTotal == 0 ? 0.0 : (workerMetrics.todayCompleted * 100.0) / workerMetrics.todayTotal;
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

    private void updateProfileCards(User user) {
        String name = user != null && hasText(user.getName()) ? user.getName() : DEFAULT_FARMER;
        String mobile = user != null && hasText(user.getMobile()) ? user.getMobile().trim() : UNKNOWN_PHONE;
        String soil = user != null && hasText(user.getSoilType()) ? user.getSoilType().trim() : UNKNOWN_SOIL;
        String location = buildLocation(user);

        setIfPresent(lblProfileName, name);
        setIfPresent(lblProfilePhone, "Phone: " + mobile);
        setIfPresent(lblProfileSoil, "Soil: " + soil);
        setIfPresent(lblProfileLocation, location);
        setIfPresent(lblProfileLocationDetail, location);
        setIfPresent(lblProfileSoilDetail, soil);
        setIfPresent(lblProfileMobileDetail, mobile);
    }

    private String buildLocation(User user) {
        if (user == null) {
            return UNKNOWN_LOCATION;
        }
        String division = hasText(user.getDivision()) ? user.getDivision().trim() : "";
        String district = hasText(user.getDistrict()) ? user.getDistrict().trim() : "";
        String upazila = hasText(user.getUpazila()) ? user.getUpazila().trim() : "";

        StringBuilder location = new StringBuilder();
        if (!upazila.isEmpty()) location.append(upazila);
        if (!district.isEmpty()) {
            if (location.length() > 0) location.append(", ");
            location.append(district);
        }
        if (!division.isEmpty()) {
            if (location.length() > 0) location.append(", ");
            location.append(division);
        }
        return location.length() == 0 ? UNKNOWN_LOCATION : location.toString();
    }

    private User resolveActiveUser() {
        User sessionUser = SessionManager.getLoggedInUser();
        User storedUser = findStoredUser(sessionUser);

        if (sessionUser == null) {
            return storedUser;
        }
        if (storedUser == null) {
            return sessionUser;
        }
        return mergeUsers(sessionUser, storedUser);
    }

    private User findStoredUser(User sessionUser) {
        List<User> users = loadStoredUsers();
        if (users.isEmpty()) {
            return null;
        }

        if (sessionUser == null) {
            return users.get(0);
        }

        return users.stream()
                .filter(u -> matchesUser(sessionUser, u))
                .findFirst()
                .orElse(null);
    }

    private boolean matchesUser(User a, User b) {
        return sameText(a.getUsername(), b.getUsername())
                || sameText(a.getMobile(), b.getMobile())
                || sameText(a.getEmail(), b.getEmail())
                || sameText(a.getName(), b.getName());
    }

    private boolean sameText(String first, String second) {
        if (!hasText(first) || !hasText(second)) {
            return false;
        }
        return first.trim().equalsIgnoreCase(second.trim());
    }

    private User mergeUsers(User primary, User fallback) {
        User merged = new User();
        merged.setName(pick(primary.getName(), fallback.getName()));
        merged.setEmail(pick(primary.getEmail(), fallback.getEmail()));
        merged.setMobile(pick(primary.getMobile(), fallback.getMobile()));
        merged.setUsername(pick(primary.getUsername(), fallback.getUsername()));
        merged.setPasswordHash(pick(primary.getPasswordHash(), fallback.getPasswordHash()));
        merged.setDivision(pick(primary.getDivision(), fallback.getDivision()));
        merged.setDistrict(pick(primary.getDistrict(), fallback.getDistrict()));
        merged.setUpazila(pick(primary.getUpazila(), fallback.getUpazila()));
        merged.setLandAmount(pick(primary.getLandAmount(), fallback.getLandAmount()));
        merged.setSoilType(pick(primary.getSoilType(), fallback.getSoilType()));
        merged.setProfileImagePath(pick(primary.getProfileImagePath(), fallback.getProfileImagePath()));
        return merged;
    }

    private String pick(String first, String second) {
        return hasText(first) ? first : second;
    }

    private List<User> loadStoredUsers() {
        if (!Files.exists(USER_DATA_FILE)) {
            return List.of();
        }
        try (Reader reader = Files.newBufferedReader(USER_DATA_FILE)) {
            Type listType = new TypeToken<List<User>>() {}.getType();
            List<User> users = gson.fromJson(reader, listType);
            return users == null ? List.of() : users;
        } catch (Exception ignored) {
            return List.of();
        }
    }

    private String resolveDataFreshness() {
        LocalDateTime newest = null;

        for (Path file : List.of(WORKERS_FILE)) {
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
        return newest == null ? NO_LOCAL_DATASET : DATASET_UPDATED_PREFIX + newest.format(UPDATED_AT_FORMAT);
    }

    private WorkerMetrics loadWorkerMetrics() {
        WorkerMetrics metrics = new WorkerMetrics();
        if (!Files.exists(WORKERS_FILE)) {
            applyNoWorkerData(metrics);
            return metrics;
        }

        try (Reader reader = Files.newBufferedReader(WORKERS_FILE)) {
            JsonArray rows = gson.fromJson(reader, JsonArray.class);
            if (rows == null) {
                applyNoWorkerData(metrics);
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
                    .orElse(NO_WORK_TYPE);
            metrics.latestEntrySummary = latestEvent == null
                    ? NO_RECENT_ACTIVITY
                    : latestWorkerName + " | Time: " + latestEvent.format(UPDATED_AT_FORMAT);
        } catch (Exception e) {
            metrics.topWorkType = ANALYSIS_UNAVAILABLE;
            metrics.latestEntrySummary = ANALYSIS_LOAD_FAIL;
        }

        return metrics;
    }

    private void applyNoWorkerData(WorkerMetrics metrics) {
        metrics.topWorkType = NO_WORK_TYPE;
        metrics.latestEntrySummary = NO_WORKER_RECORD;
    }

    private String getString(JsonObject object, String key) {
        if (object == null || !object.has(key) || object.get(key).isJsonNull()) return "-";
        String value = object.get(key).getAsString();
        return value == null || value.isBlank() ? "-" : value.trim();
    }

    private double getDouble(JsonObject object, String key) {
        if (object == null || !object.has(key) || object.get(key).isJsonNull()) return 0;
        try {
            return object.get(key).getAsDouble();
        } catch (Exception ignored) {
            return 0;
        }
    }

    private LocalDate parseWorkDate(String value) {
        if (!hasText(value) || "-".equals(value)) return null;
        try {
            return LocalDate.parse(value, WORK_DATE_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }

    private LocalDateTime parsePaidAt(String value) {
        if (!hasText(value) || "-".equals(value)) return null;
        try {
            return LocalDateTime.parse(value, WORK_PAID_AT_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }

    private double parseDouble(String value) {
        if (!hasText(value)) return 0;
        try {
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return 0;
        }
    }

    private boolean hasText(String value) { return value != null && !value.isBlank(); }

    private void setIfPresent(Label label, String value) { if (label != null) label.setText(value); }

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



