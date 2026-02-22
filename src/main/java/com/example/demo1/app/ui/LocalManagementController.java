package com.example.demo1.app.ui;

import com.example.demo1.app.util.NavigationHelper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URL;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.stream.Collectors;

public class LocalManagementController implements Initializable {

    private static final String STATUS_PENDING = "pending";
    private static final String STATUS_COMPLETED = "completed";
    private static final String FILTER_ALL = "সব";
    private static final String FILTER_PENDING = "বাকি";
    private static final String FILTER_COMPLETED = "পরিশোধিত";
    private static final String PERIOD_TODAY = "today";
    private static final String PERIOD_WEEK = "week";
    private static final String PERIOD_MONTH = "month";
    private static final String DATA_FILE = "workers_data.json";
    private static final DateTimeFormatter DATE_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter PAID_AT_FMT = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
    private static final Type WORKER_RECORD_LIST_TYPE = new TypeToken<ArrayList<WorkerRecord>>() {}.getType();

    @FXML private Button btnHome;
    @FXML private Button btnAdvisory;
    @FXML private Button btnStorage;
    @FXML private Button btnLocalManagement;
    @FXML private Button btnMachinery;

    @FXML private Button addRecordBtn;
    @FXML private Button quickAddBtn;
    @FXML private Button emptyAddBtn;

    @FXML private ToggleButton tabToday;
    @FXML private ToggleButton tabWeek;
    @FXML private ToggleButton tabMonth;

    @FXML private Label totalWorkersLabel;
    @FXML private Label todayCostLabel;
    @FXML private Label pendingPaymentLabel;
    @FXML private Label completedPaymentLabel;

    @FXML private ComboBox<String> filterComboBox;
    @FXML private ComboBox<String> quickWorkTypeCombo;

    @FXML private TextField searchField;
    @FXML private TextField quickNameField;
    @FXML private TextField quickHoursField;
    @FXML private TextField quickRateField;

    @FXML private VBox workersListContainer;
    @FXML private VBox emptyState;

    private List<WorkerRecord> workerRecords = new ArrayList<>();
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0.00");
    private String selectedPeriod = PERIOD_TODAY;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);

        setupFilter();
        setupQuickAdd();
        setupSearch();

        if (tabToday != null) {
            tabToday.setSelected(true);
            applyPeriodTabStyles();
        }
        if (addRecordBtn != null) {
            addRecordBtn.setOnAction(e -> showAddDialog());
        }
        if (emptyAddBtn != null) {
            emptyAddBtn.setOnAction(e -> showAddDialog());
        }

        loadData();
        refreshView();
    }

    @FXML
    private void handlePeriodChange(ActionEvent event) {
        if (tabWeek != null && tabWeek.isSelected()) {
            selectedPeriod = PERIOD_WEEK;
        } else if (tabMonth != null && tabMonth.isSelected()) {
            selectedPeriod = PERIOD_MONTH;
        } else {
            selectedPeriod = PERIOD_TODAY;
        }
        applyPeriodTabStyles();
        refreshView();
    }

    private void applyPeriodTabStyles() {
        updateTabStyle(tabToday, PERIOD_TODAY.equals(selectedPeriod));
        updateTabStyle(tabWeek, PERIOD_WEEK.equals(selectedPeriod));
        updateTabStyle(tabMonth, PERIOD_MONTH.equals(selectedPeriod));
    }

    private void updateTabStyle(ToggleButton button, boolean active) {
        if (button == null) {
            return;
        }
        button.getStyleClass().remove("period-tab-active");
        if (active) {
            button.getStyleClass().add("period-tab-active");
        }
    }

    private void setupSearch() {
        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldV, newV) -> refreshView());
        }
    }

    private void setupQuickAdd() {
        if (quickWorkTypeCombo != null) {
            quickWorkTypeCombo.getItems().addAll(
                    "জমি চাষ",
                    "বীজ বপন",
                    "সেচ",
                    "সার প্রয়োগ",
                    "ফসল কাটা",
                    "অন্যান্য"
            );
        }
        if (quickAddBtn != null) {
            quickAddBtn.setOnAction(e -> handleQuickAdd());
        }
    }

    private void handleQuickAdd() {
        String name = safeTrim(quickNameField.getText());
        String workType = quickWorkTypeCombo.getValue();
        String hourText = safeTrim(quickHoursField.getText());
        String rateText = safeTrim(quickRateField.getText());

        if (name.isEmpty() || workType == null || hourText.isEmpty() || rateText.isEmpty()) {
            showAlert(Alert.AlertType.WARNING, "তথ্য অসম্পূর্ণ", "Quick Add এর সব ঘর পূরণ করুন।");
            return;
        }

        try {
            double hours = Double.parseDouble(hourText);
            double rate = Double.parseDouble(rateText);
            WorkerRecord record = new WorkerRecord(
                    UUID.randomUUID().toString(),
                    name,
                    "",
                    workType,
                    LocalDate.now().format(DATE_FMT),
                    hours,
                    rate,
                    STATUS_PENDING,
                    "",
                    ""
            );

            workerRecords.add(0, record);
            saveData();
            clearQuickAddFields();
            refreshView();
        } catch (NumberFormatException ex) {
            showAlert(Alert.AlertType.WARNING, "তথ্য ত্রুটি", "ঘণ্টা এবং রেট সংখ্যায় লিখুন।");
        }
    }

    private void clearQuickAddFields() {
        quickNameField.clear();
        quickHoursField.clear();
        quickRateField.clear();
        quickWorkTypeCombo.setValue(null);
    }

    private void setupFilter() {
        filterComboBox.getItems().addAll(FILTER_ALL, FILTER_PENDING, FILTER_COMPLETED);
        filterComboBox.setValue(FILTER_ALL);
        filterComboBox.setOnAction(e -> refreshView());
    }

    private void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }

        try (Reader reader = new FileReader(file)) {
            List<WorkerRecord> data = gson.fromJson(reader, WORKER_RECORD_LIST_TYPE);
            if (data != null) {
                workerRecords = data;
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "ডাটা লোড ত্রুটি", "ডাটা ফাইল পড়া যায়নি।");
        }
    }

    private void saveData() {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            gson.toJson(workerRecords, writer);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "ডাটা সংরক্ষণ ত্রুটি", "ডাটা সংরক্ষণ করা যায়নি।");
        }
    }

    private void refreshView() {
        updateStats();
        renderList();
    }

    private void updateStats() {
        List<WorkerRecord> periodRecords = workerRecords.stream()
                .filter(this::matchesPeriod)
                .collect(Collectors.toList());

        long uniqueWorkers = periodRecords.stream()
                .map(r -> safeText(r.name))
                .distinct()
                .count();

        double todayCost = workerRecords.stream()
                .filter(this::isToday)
                .mapToDouble(WorkerRecord::getTotal)
                .sum();

        double pending = periodRecords.stream()
                .filter(r -> STATUS_PENDING.equals(r.status))
                .mapToDouble(WorkerRecord::getTotal)
                .sum();

        double completed = periodRecords.stream()
                .filter(r -> STATUS_COMPLETED.equals(r.status))
                .mapToDouble(WorkerRecord::getTotal)
                .sum();

        totalWorkersLabel.setText(String.valueOf(uniqueWorkers));
        todayCostLabel.setText("৳" + moneyFormat.format(todayCost));
        pendingPaymentLabel.setText("৳" + moneyFormat.format(pending));
        completedPaymentLabel.setText("৳" + moneyFormat.format(completed));
    }

    private void renderList() {
        workersListContainer.getChildren().clear();

        List<WorkerRecord> visibleRecords = workerRecords.stream()
                .filter(this::matchesPeriod)
                .filter(this::matchesStatusFilter)
                .filter(this::matchesSearch)
                .collect(Collectors.toList());

        if (visibleRecords.isEmpty()) {
            emptyState.setVisible(true);
            emptyState.setManaged(true);
            workersListContainer.getChildren().add(emptyState);
            return;
        }

        emptyState.setVisible(false);
        emptyState.setManaged(false);
        visibleRecords.forEach(record -> workersListContainer.getChildren().add(createCard(record)));
    }

    private boolean matchesStatusFilter(WorkerRecord record) {
        String filter = filterComboBox.getValue();
        if (filter == null || FILTER_ALL.equals(filter)) {
            return true;
        }
        if (FILTER_PENDING.equals(filter)) {
            return STATUS_PENDING.equals(record.status);
        }
        return STATUS_COMPLETED.equals(record.status);
    }

    private boolean matchesSearch(WorkerRecord record) {
        String q = safeTrim(searchField.getText()).toLowerCase();
        if (q.isEmpty()) {
            return true;
        }
        return safeText(record.name).toLowerCase().contains(q)
                || safeText(record.phone).toLowerCase().contains(q);
    }

    private boolean matchesPeriod(WorkerRecord record) {
        LocalDate date = parseDate(record.date);
        if (date == null) {
            return false;
        }
        LocalDate now = LocalDate.now();

        if (PERIOD_WEEK.equals(selectedPeriod)) {
            LocalDate weekStart = now.minusDays(6);
            return !date.isBefore(weekStart) && !date.isAfter(now);
        }
        if (PERIOD_MONTH.equals(selectedPeriod)) {
            return date.getYear() == now.getYear() && date.getMonthValue() == now.getMonthValue();
        }
        return date.equals(now);
    }

    private boolean isToday(WorkerRecord record) {
        LocalDate date = parseDate(record.date);
        return date != null && date.equals(LocalDate.now());
    }

    private LocalDate parseDate(String dateText) {
        try {
            return LocalDate.parse(dateText, DATE_FMT);
        } catch (Exception e) {
            return null;
        }
    }

    private VBox createCard(WorkerRecord record) {
        VBox card = new VBox(10);
        card.getStyleClass().add("worker-card");
        card.setPadding(new Insets(16));

        boolean isPending = STATUS_PENDING.equals(record.status);
        Label statusLabel = new Label(isPending ? "বাকি" : "পরিশোধিত");
        statusLabel.getStyleClass().add(isPending ? "status-badge-pending" : "status-badge-completed");

        Label workerName = new Label("👤 " + safeText(record.name));
        workerName.getStyleClass().add("worker-name");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(10, workerName, spacer, statusLabel);
        header.setAlignment(Pos.CENTER_LEFT);

        HBox row1 = createInfoRow(
                "📞 " + safeText(record.phone),
                "🛠️ " + safeText(record.workType),
                "📅 " + safeText(record.date)
        );

        HBox row2 = createInfoRow(
                "⏰ " + record.hours + " ঘণ্টা",
                "💰 ৳" + record.rate + "/ঘণ্টা",
                "🧾 মোট: ৳" + moneyFormat.format(record.getTotal())
        );

        HBox actions = new HBox(8);
        actions.setAlignment(Pos.CENTER_RIGHT);

        if (isPending) {
            Button payBtn = new Button("পরিশোধ");
            payBtn.getStyleClass().add("pay-btn");
            payBtn.setOnAction(e -> markAsPaid(record));
            actions.getChildren().add(payBtn);
        }

        Button editBtn = new Button("এডিট");
        editBtn.getStyleClass().add("edit-btn");
        editBtn.setOnAction(e -> showEditDialog(record));

        Button deleteBtn = new Button("মুছুন");
        deleteBtn.getStyleClass().add("delete-btn");
        deleteBtn.setOnAction(e -> confirmDelete(record));

        actions.getChildren().addAll(editBtn, deleteBtn);

        card.getChildren().addAll(header, row1, row2);

        if (!safeText(record.notes).equals("-")) {
            Label notes = new Label("📝 " + record.notes);
            notes.setWrapText(true);
            notes.getStyleClass().add("notes-text");
            card.getChildren().add(notes);
        }

        if (STATUS_COMPLETED.equals(record.status) && !safeTrim(record.paidAt).isEmpty()) {
            Label paidAt = new Label("✅ পরিশোধের সময়: " + record.paidAt);
            paidAt.getStyleClass().add("detail-value");
            card.getChildren().add(paidAt);
        }

        card.getChildren().add(actions);
        return card;
    }

    private HBox createInfoRow(String... texts) {
        HBox row = new HBox(20);
        for (String text : texts) {
            Label label = new Label(text);
            label.getStyleClass().add("detail-value");
            row.getChildren().add(label);
        }
        return row;
    }

    private void markAsPaid(WorkerRecord record) {
        record.status = STATUS_COMPLETED;
        record.paidAt = LocalDateTime.now().format(PAID_AT_FMT);
        saveData();
        refreshView();
    }

    private void showAddDialog() {
        WorkerRecord record = showRecordDialog(null);
        if (record != null) {
            workerRecords.add(0, record);
            saveData();
            refreshView();
        }
    }

    private void showEditDialog(WorkerRecord original) {
        WorkerRecord edited = showRecordDialog(original);
        if (edited == null) {
            return;
        }

        original.name = edited.name;
        original.phone = edited.phone;
        original.workType = edited.workType;
        original.date = edited.date;
        original.hours = edited.hours;
        original.rate = edited.rate;
        original.notes = edited.notes;

        saveData();
        refreshView();
    }

    private WorkerRecord showRecordDialog(WorkerRecord existing) {
        Dialog<WorkerRecord> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "নতুন রেকর্ড" : "রেকর্ড এডিট");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(18));

        TextField name = new TextField(existing == null ? "" : safeText(existing.name));
        TextField phone = new TextField(existing == null ? "" : safeText(existing.phone));
        ComboBox<String> type = new ComboBox<>();
        type.getItems().addAll("জমি চাষ", "বীজ বপন", "সেচ", "সার প্রয়োগ", "ফসল কাটা", "অন্যান্য");
        if (existing != null) {
            type.setValue(existing.workType);
        }

        DatePicker date = new DatePicker(existing == null ? LocalDate.now() : parseDate(existing.date));
        TextField hours = new TextField(existing == null ? "" : String.valueOf(existing.hours));
        TextField rate = new TextField(existing == null ? "" : String.valueOf(existing.rate));
        TextArea notes = new TextArea(existing == null ? "" : safeText(existing.notes));
        notes.setPrefRowCount(2);

        grid.addRow(0, new Label("নাম:"), name);
        grid.addRow(1, new Label("ফোন:"), phone);
        grid.addRow(2, new Label("কাজের ধরন:"), type);
        grid.addRow(3, new Label("তারিখ:"), date);
        grid.addRow(4, new Label("ঘণ্টা:"), hours);
        grid.addRow(5, new Label("রেট:"), rate);
        grid.addRow(6, new Label("নোট:"), notes);

        dialog.getDialogPane().setContent(grid);
        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                try {
                    return new WorkerRecord(
                            existing == null ? UUID.randomUUID().toString() : existing.id,
                            safeTrim(name.getText()),
                            safeTrim(phone.getText()),
                            type.getValue(),
                            date.getValue().format(DATE_FMT),
                            Double.parseDouble(safeTrim(hours.getText())),
                            Double.parseDouble(safeTrim(rate.getText())),
                            existing == null ? STATUS_PENDING : existing.status,
                            safeTrim(notes.getText()),
                            existing == null ? "" : safeText(existing.paidAt)
                    );
                } catch (Exception e) {
                    showAlert(Alert.AlertType.WARNING, "ইনপুট ত্রুটি", "সব তথ্য সঠিকভাবে দিন।");
                    return null;
                }
            }
            return null;
        });

        return dialog.showAndWait().orElse(null);
    }

    private void confirmDelete(WorkerRecord record) {
        Alert alert = new Alert(
                Alert.AlertType.CONFIRMATION,
                safeText(record.name) + " এর রেকর্ড মুছতে চান?",
                ButtonType.YES,
                ButtonType.NO
        );
        alert.setTitle("রেকর্ড মুছুন");
        alert.setHeaderText(null);

        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES) {
                workerRecords.remove(record);
                saveData();
                refreshView();
            }
        });
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private String safeText(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private static class WorkerRecord {
        String id;
        String name;
        String phone;
        String workType;
        String date;
        String status;
        String notes;
        String paidAt;
        double hours;
        double rate;

        WorkerRecord(String id, String name, String phone, String workType, String date,
                     double hours, double rate, String status, String notes, String paidAt) {
            this.id = id;
            this.name = name;
            this.phone = phone;
            this.workType = workType;
            this.date = date;
            this.hours = hours;
            this.rate = rate;
            this.status = status;
            this.notes = notes;
            this.paidAt = paidAt;
        }

        double getTotal() {
            return hours * rate;
        }
    }
}
