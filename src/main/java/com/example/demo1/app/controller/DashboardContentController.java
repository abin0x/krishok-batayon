package com.example.demo1.app.controller;
import com.example.demo1.app.model.User;
import com.example.demo1.app.util.SessionManager;
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
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

// class shuru hoiche dashboard content controller
public class DashboardContentController {

    private static final Path WORKERS_FILE = Path.of("workers_data.json");//ata path of workers data
    private static final DateTimeFormatter WORK_DATE_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy");//ata date format of work date
    private static final DateTimeFormatter WORK_PAID_AT_FORMAT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");//ata date time format of payment time
    private static final Locale LOCALE_BN = new Locale("bn", "BD");//ata locale for Bangla (Bangladesh)
    private static final DateTimeFormatter UPDATED_AT_FORMAT = DateTimeFormatter.ofPattern("dd MMM yyyy, hh:mm:ss a", LOCALE_BN);//ata date time format for last update time
    private static final String DEFAULT_FARMER = "Farmer";
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

    @FXML private Label lblWelcomeUser,lblNow, lblWorkerRecords, lblPendingPayments, lblCompletedPayments, lblMonthlyLaborCost,lblTotalLandSummary,lblTodayActivities, lblTodayCompleted, lblCompletionRate, lblTopWorkType, lblLatestEntry, lblDataFreshness,pbCompletion;

    @FXML
    public void initialize() {
        User sessionUser = SessionManager.getLoggedInUser();
        String name = (sessionUser != null && hasText(sessionUser.getName())) ? sessionUser.getName() : DEFAULT_FARMER;
        lblWelcomeUser.setText("Welcome, " + name);

        refreshDashboard();

        refreshTimer = new Timeline(new KeyFrame(Duration.seconds(20), e -> refreshDashboard()));
        refreshTimer.setCycleCount(Timeline.INDEFINITE);
        refreshTimer.play();
    }

    private void refreshDashboard() {
        WorkerMetrics workerMetrics = loadWorkerMetrics();
        User sessionUser = SessionManager.getLoggedInUser();
        double userLandAmount = sessionUser == null ? 0 : parseDouble(sessionUser.getLandAmount());
        setIfPresent(lblTotalLandSummary, decimalFormat.format(userLandAmount) + " acres");

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



