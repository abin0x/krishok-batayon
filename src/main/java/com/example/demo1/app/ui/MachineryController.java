package com.example.demo1.app.ui;

import com.example.demo1.app.model.User;
import com.example.demo1.app.util.NavigationHelper;
import com.example.demo1.app.util.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.awt.Desktop;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class MachineryController implements Initializable {

    private static final String DATA_FILE = "machinery_data.json";
    private static final String CATEGORY_ALL = "সব ক্যাটাগরি";
    private static final String DISTRICT_ALL = "সব জেলা";
    private static final String LEGACY_OWNER_ID = "__legacy__";
    private static final String SYSTEM_NO_OWNER_MSG = "Only the record creator can edit, delete, or hide this data.";
    private static final Type MACHINE_LIST_TYPE = new TypeToken<ArrayList<MachineRecord>>() {}.getType();

    @FXML private Button btnHome;
    @FXML private Button btnAdvisory;
    @FXML private Button btnStorage;
    @FXML private Button btnLocalManagement;
    @FXML private Button btnMachinery;

    @FXML private TextField searchField;
    @FXML private ComboBox<String> categoryCombo;
    @FXML private ComboBox<String> districtCombo;
    @FXML private Button addMachineBtn;
    @FXML private FlowPane machineryContainer;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0");
    private final List<MachineRecord> machineRecords = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);

        loadData();
        setupFilters();
        setupActions();
        renderCards();
    }

    private void setupFilters() {
        if (searchField != null) {
            searchField.textProperty().addListener((observable, oldValue, newValue) -> renderCards());
        }

        if (categoryCombo != null) {
            categoryCombo.setOnAction(e -> renderCards());
        }

        if (districtCombo != null) {
            districtCombo.setOnAction(e -> renderCards());
        }

        rebuildFilterOptions();
    }

    private void setupActions() {
        if (addMachineBtn != null) {
            addMachineBtn.setOnAction(e -> showAddDialog());
        }
    }

    private void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            seedDefaultData();
            saveData();
            return;
        }

        try (Reader reader = new FileReader(file)) {
            List<MachineRecord> loaded = gson.fromJson(reader, MACHINE_LIST_TYPE);
            if (loaded != null) {
                machineRecords.clear();
                machineRecords.addAll(loaded);
                if (normalizeRecords()) {
                    saveData();
                }
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "যন্ত্রপাতি ডেটা পড়া যায়নি।");
        }
    }

    private boolean normalizeRecords() {
        boolean changed = false;
        for (MachineRecord record : machineRecords) {
            if (safeTrim(record.createdByUserId).isEmpty()) {
                record.createdByUserId = LEGACY_OWNER_ID;
                changed = true;
            }
            if (record.hidden == null) {
                record.hidden = false;
                changed = true;
            }
            record.category = mapCategoryToBangla(record.category);
        }
        return changed;
    }

    private void seedDefaultData() {
        machineRecords.clear();
        machineRecords.add(new MachineRecord(
                UUID.randomUUID().toString(),
                "পাওয়ার টিলার ট্রাক্টর",
                "ট্রাক্টর",
                "আব্দুল করিম",
                "কুমিল্লা",
                "দাউদকান্দি",
                400,
                3500,
                "নতুন মডেল পাওয়ার টিলার। জমি চাষের জন্য উপযুক্ত।",
                "+8801711000001",
                "seed_owner_1",
                false
        ));
        machineRecords.add(new MachineRecord(
                UUID.randomUUID().toString(),
                "রাইস হারভেস্টার",
                "হারভেস্টার",
                "মোঃ জামাল হোসেন",
                "ময়মনসিংহ",
                "ত্রিশাল",
                1500,
                10000,
                "স্বয়ংক্রিয় ধান কাটার যন্ত্র।",
                "+8801711000002",
                "seed_owner_2",
                false
        ));
        machineRecords.add(new MachineRecord(
                UUID.randomUUID().toString(),
                "স্প্রেয়ার মেশিন",
                "স্প্রেয়ার",
                "রফিকুল ইসলাম",
                "রাজশাহী",
                "গোদাগাড়ী",
                500,
                3000,
                "কীটনাশক ও সার স্প্রের জন্য।",
                "+8801711000003",
                "seed_owner_3",
                false
        ));
    }

    private void saveData() {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            gson.toJson(machineRecords, writer);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Save Error", "যন্ত্রপাতি ডেটা সংরক্ষণ করা যায়নি।");
        }
    }

    private void rebuildFilterOptions() {
        Set<String> categories = new LinkedHashSet<>();
        categories.add(CATEGORY_ALL);
        Set<String> districts = new LinkedHashSet<>();
        districts.add(DISTRICT_ALL);

        machineRecords.forEach(m -> {
            categories.add(mapCategoryToBangla(m.category));
            districts.add(safeText(m.district));
        });

        if (categoryCombo != null) {
            String previous = categoryCombo.getValue();
            categoryCombo.getItems().setAll(categories);
            categoryCombo.setValue(previous != null && categories.contains(previous) ? previous : CATEGORY_ALL);
        }

        if (districtCombo != null) {
            String previous = districtCombo.getValue();
            districtCombo.getItems().setAll(districts);
            districtCombo.setValue(previous != null && districts.contains(previous) ? previous : DISTRICT_ALL);
        }
    }

    private void renderCards() {
        if (machineryContainer == null) {
            return;
        }

        machineryContainer.getChildren().clear();

        String q = searchField == null ? "" : safeTrim(searchField.getText()).toLowerCase(Locale.ROOT);
        String selectedCategory = categoryCombo == null || categoryCombo.getValue() == null
                ? CATEGORY_ALL
                : categoryCombo.getValue();
        String selectedDistrict = districtCombo == null || districtCombo.getValue() == null
                ? DISTRICT_ALL
                : districtCombo.getValue();

        List<MachineRecord> filtered = machineRecords.stream()
                .filter(r -> !isHiddenForViewer(r))
                .filter(r -> CATEGORY_ALL.equals(selectedCategory) || mapCategoryToBangla(r.category).equals(selectedCategory))
                .filter(r -> DISTRICT_ALL.equals(selectedDistrict) || safeText(r.district).equals(selectedDistrict))
                .filter(r -> q.isEmpty() || matchesSearch(r, q))
                .collect(Collectors.toList());

        if (filtered.isEmpty()) {
            VBox empty = new VBox(8);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(30));
            empty.getStyleClass().add("machine-card");
            Label title = new Label("কোনো যন্ত্রপাতি পাওয়া যায়নি");
            title.getStyleClass().add("card-title");
            Label sub = new Label("ফিল্টার পরিবর্তন করুন অথবা নতুন যন্ত্র যুক্ত করুন");
            sub.getStyleClass().add("desc-text");
            empty.getChildren().addAll(title, sub);
            machineryContainer.getChildren().add(empty);
            return;
        }

        filtered.forEach(record -> machineryContainer.getChildren().add(createCard(record)));
    }

    private boolean matchesSearch(MachineRecord r, String q) {
        return safeText(r.title).toLowerCase(Locale.ROOT).contains(q)
                || mapCategoryToBangla(r.category).toLowerCase(Locale.ROOT).contains(q)
                || safeText(r.ownerName).toLowerCase(Locale.ROOT).contains(q)
                || safeText(r.district).toLowerCase(Locale.ROOT).contains(q)
                || safeText(r.upazila).toLowerCase(Locale.ROOT).contains(q);
    }

    private VBox createCard(MachineRecord r) {
        VBox card = new VBox(10);
        card.getStyleClass().add("machine-card");

        boolean owner = canModifyRecord(r);

        Label title = new Label(safeText(r.title));
        title.getStyleClass().add("card-title");
        title.setWrapText(true);

        Label status = new Label(isRecordHidden(r) ? "লুকানো" : "উপলব্ধ");
        status.getStyleClass().add(isRecordHidden(r) ? "badge-unavailable" : "badge-available");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(10, title, spacer, status);
        top.setAlignment(Pos.CENTER_LEFT);

        HBox tags = new HBox(8,
                createTag(mapCategoryToBangla(r.category), "tag-grey"),
                createTag("জেলা: " + safeText(r.district), "tag-verified")
        );

        VBox details = new VBox(7,
                detailRow("👤", safeText(r.ownerName)),
                detailRow("📍", safeText(r.district) + ", " + safeText(r.upazila)),
                detailRow("📞", safeText(r.phone)),
                detailRow("💰", "৳" + moneyFormat.format(r.hourlyRate) + "/ঘণ্টা | ৳" + moneyFormat.format(r.dailyRate) + "/দিন")
        );
        details.getStyleClass().add("details-box");

        Label desc = new Label(safeText(r.description));
        desc.getStyleClass().add("desc-text");
        desc.setWrapText(true);

        Button contactBtn = new Button("WhatsApp যোগাযোগ");
        contactBtn.getStyleClass().add("btn-contact");
        contactBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(contactBtn, Priority.ALWAYS);
        contactBtn.setOnAction(e -> openWhatsApp(r));

        HBox actions = new HBox(8, contactBtn);

        if (owner) {
            Button editBtn = new Button("এডিট");
            editBtn.getStyleClass().add("btn-secondary");
            editBtn.setOnAction(e -> showEditDialog(r));

            Button hideBtn = new Button(isRecordHidden(r) ? "দেখান" : "লুকান");
            hideBtn.getStyleClass().add("btn-secondary");
            hideBtn.setOnAction(e -> toggleHide(r));

            Button deleteBtn = new Button("ডিলিট");
            deleteBtn.getStyleClass().add("btn-danger");
            deleteBtn.setOnAction(e -> confirmDelete(r));

            actions.getChildren().addAll(editBtn, hideBtn, deleteBtn);
        } else {
            Label ownerOnly = new Label("Owner only controls");
            ownerOnly.getStyleClass().add("owner-note");
            actions.getChildren().add(ownerOnly);
        }

        card.getChildren().addAll(top, tags, details, desc, actions);
        if (isRecordHidden(r) && owner) {
            card.getStyleClass().add("machine-card-hidden");
        }
        return card;
    }

    private Label createTag(String text, String styleClass) {
        Label tag = new Label(text);
        tag.getStyleClass().add(styleClass);
        return tag;
    }

    private HBox detailRow(String icon, String text) {
        Label iconLabel = new Label(icon);
        Label textLabel = new Label(text);
        textLabel.getStyleClass().add("detail-text");
        return new HBox(8, iconLabel, textLabel);
    }

    private void showAddDialog() {
        String userId = currentUserIdOrNull();
        if (userId == null) {
            showAlert(Alert.AlertType.WARNING, "Unauthorized", "Please login to add machinery.");
            return;
        }

        MachineRecord created = showMachineDialog(null);
        if (created == null) {
            return;
        }

        created.id = UUID.randomUUID().toString();
        created.createdByUserId = userId;
        created.hidden = false;
        machineRecords.add(0, created);

        saveData();
        rebuildFilterOptions();
        renderCards();
    }

    private void showEditDialog(MachineRecord original) {
        if (!canModifyRecord(original)) {
            showAlert(Alert.AlertType.WARNING, "Unauthorized", SYSTEM_NO_OWNER_MSG);
            return;
        }

        MachineRecord edited = showMachineDialog(original);
        if (edited == null) {
            return;
        }

        original.title = edited.title;
        original.category = mapCategoryToBangla(edited.category);
        original.ownerName = edited.ownerName;
        original.district = edited.district;
        original.upazila = edited.upazila;
        original.hourlyRate = edited.hourlyRate;
        original.dailyRate = edited.dailyRate;
        original.description = edited.description;
        original.phone = edited.phone;

        saveData();
        rebuildFilterOptions();
        renderCards();
    }

    private MachineRecord showMachineDialog(MachineRecord existing) {
        Dialog<MachineRecord> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "নতুন যন্ত্র যোগ করুন" : "যন্ত্র তথ্য এডিট করুন");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));

        TextField titleField = new TextField(existing == null ? "" : safeText(existing.title));
        ComboBox<String> categoryField = new ComboBox<>();
        categoryField.getItems().addAll("ট্রাক্টর", "হারভেস্টার", "স্প্রেয়ার", "থ্রেশার", "অন্যান্য");
        categoryField.setValue(existing == null ? "ট্রাক্টর" : mapCategoryToBangla(existing.category));

        TextField ownerField = new TextField(existing == null ? currentUserDisplayName() : safeText(existing.ownerName));
        TextField districtField = new TextField(existing == null ? "" : safeText(existing.district));
        TextField upazilaField = new TextField(existing == null ? "" : safeText(existing.upazila));
        TextField hourlyField = new TextField(existing == null ? "" : String.valueOf((int) existing.hourlyRate));
        TextField dailyField = new TextField(existing == null ? "" : String.valueOf((int) existing.dailyRate));
        TextField phoneField = new TextField(existing == null ? "" : safeText(existing.phone));
        TextArea descriptionField = new TextArea(existing == null ? "" : safeText(existing.description));
        descriptionField.setPrefRowCount(3);

        grid.addRow(0, new Label("যন্ত্রের নাম:"), titleField);
        grid.addRow(1, new Label("ক্যাটাগরি:"), categoryField);
        grid.addRow(2, new Label("মালিকের নাম:"), ownerField);
        grid.addRow(3, new Label("জেলা:"), districtField);
        grid.addRow(4, new Label("উপজেলা:"), upazilaField);
        grid.addRow(5, new Label("প্রতি ঘণ্টা ভাড়া:"), hourlyField);
        grid.addRow(6, new Label("প্রতি দিন ভাড়া:"), dailyField);
        grid.addRow(7, new Label("মোবাইল:"), phoneField);
        grid.addRow(8, new Label("বিবরণ:"), descriptionField);

        dialog.getDialogPane().setContent(grid);

        dialog.setResultConverter(btn -> {
            if (btn != ButtonType.OK) {
                return null;
            }
            try {
                MachineRecord draft = new MachineRecord();
                draft.title = safeTrim(titleField.getText());
                draft.category = mapCategoryToBangla(categoryField.getValue());
                draft.ownerName = safeTrim(ownerField.getText());
                draft.district = safeTrim(districtField.getText());
                draft.upazila = safeTrim(upazilaField.getText());
                draft.hourlyRate = Double.parseDouble(safeTrim(hourlyField.getText()));
                draft.dailyRate = Double.parseDouble(safeTrim(dailyField.getText()));
                draft.phone = safeTrim(phoneField.getText());
                draft.description = safeTrim(descriptionField.getText());

                if (draft.title.isEmpty() || draft.ownerName.isEmpty() || draft.district.isEmpty() || draft.phone.isEmpty()) {
                    throw new IllegalArgumentException("missing");
                }
                return draft;
            } catch (Exception ex) {
                showAlert(Alert.AlertType.WARNING, "ইনপুট ত্রুটি", "সব তথ্য সঠিকভাবে দিন।");
                return null;
            }
        });

        return dialog.showAndWait().orElse(null);
    }

    private void toggleHide(MachineRecord record) {
        if (!canModifyRecord(record)) {
            showAlert(Alert.AlertType.WARNING, "Unauthorized", SYSTEM_NO_OWNER_MSG);
            return;
        }
        record.hidden = !isRecordHidden(record);
        saveData();
        renderCards();
    }

    private void confirmDelete(MachineRecord record) {
        if (!canModifyRecord(record)) {
            showAlert(Alert.AlertType.WARNING, "Unauthorized", SYSTEM_NO_OWNER_MSG);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                safeText(record.title) + " ডিলিট করতে চান?",
                ButtonType.YES,
                ButtonType.NO);
        alert.setTitle("রেকর্ড ডিলিট");
        alert.setHeaderText(null);

        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES) {
                machineRecords.remove(record);
                saveData();
                rebuildFilterOptions();
                renderCards();
            }
        });
    }

    private void openWhatsApp(MachineRecord record) {
        try {
            String phone = normalizePhoneForWhatsApp(record.phone);
            String message = "আসসালামু আলাইকুম, " + record.title + " ভাড়া বিষয়ে জানতে চাই।";
            String url = "https://wa.me/" + phone + "?text=" + URLEncoder.encode(message, StandardCharsets.UTF_8);

            if (!Desktop.isDesktopSupported() || !Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
                showAlert(Alert.AlertType.INFORMATION, "WhatsApp", "এই ডিভাইসে ব্রাউজার সাপোর্ট নেই।\n" + url);
                return;
            }
            Desktop.getDesktop().browse(new URI(url));
        } catch (Exception e) {
            showAlert(Alert.AlertType.WARNING, "WhatsApp", "WhatsApp খুলতে সমস্যা হয়েছে।");
        }
    }

    private String normalizePhoneForWhatsApp(String raw) {
        String digits = raw == null ? "" : raw.replaceAll("[^0-9]", "");
        if (digits.startsWith("880")) {
            return digits;
        }
        if (digits.startsWith("0")) {
            return "88" + digits;
        }
        return digits;
    }

    private boolean canModifyRecord(MachineRecord record) {
        String current = currentUserIdOrNull();
        String owner = safeTrim(record.createdByUserId);
        return current != null && !owner.isEmpty() && !LEGACY_OWNER_ID.equals(owner) && current.equals(owner);
    }

    private boolean isHiddenForViewer(MachineRecord record) {
        if (!isRecordHidden(record)) {
            return false;
        }
        return !canModifyRecord(record);
    }

    private boolean isRecordHidden(MachineRecord record) {
        return record.hidden != null && record.hidden;
    }

    private String currentUserIdOrNull() {
        User user = SessionManager.getLoggedInUser();
        if (user == null) {
            return null;
        }
        String username = safeTrim(user.getUsername());
        if (!username.isEmpty()) return username;
        String mobile = safeTrim(user.getMobile());
        if (!mobile.isEmpty()) return mobile;
        String email = safeTrim(user.getEmail());
        if (!email.isEmpty()) return email;
        String name = safeTrim(user.getName());
        return name.isEmpty() ? null : name;
    }

    private String currentUserDisplayName() {
        User user = SessionManager.getLoggedInUser();
        if (user == null) {
            return "";
        }
        String name = safeTrim(user.getName());
        return name.isEmpty() ? safeTrim(user.getUsername()) : name;
    }

    private String safeText(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private String mapCategoryToBangla(String value) {
        String raw = safeTrim(value);
        if (raw.isEmpty()) return "অন্যান্য";
        String lower = raw.toLowerCase(Locale.ROOT);

        if (lower.contains("tractor") || raw.contains("ট্রাক্টর") || raw.contains("à¦Ÿà§à¦°à¦¾à¦•à§à¦Ÿà¦°")) return "ট্রাক্টর";
        if (lower.contains("harvester") || raw.contains("হারভেস্টার") || raw.contains("à¦¹à¦¾à¦°à¦­à§‡à¦¸à§à¦Ÿà¦¾à¦°")) return "হারভেস্টার";
        if (lower.contains("sprayer") || raw.contains("স্প্রেয়ার") || raw.contains("à¦¸à§à¦ªà§à¦°à§‡à¦¯à¦¼à¦¾à¦°")) return "স্প্রেয়ার";
        if (lower.contains("thresher") || raw.contains("থ্রেশার")) return "থ্রেশার";
        if (lower.contains("other") || raw.contains("অন্যান্য")) return "অন্যান্য";

        return raw;
    }

    private void showAlert(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private static class MachineRecord {
        String id;
        String title;
        String category;
        String ownerName;
        String district;
        String upazila;
        double hourlyRate;
        double dailyRate;
        String description;
        String phone;
        String createdByUserId;
        Boolean hidden;

        MachineRecord() {}

        MachineRecord(String id,
                      String title,
                      String category,
                      String ownerName,
                      String district,
                      String upazila,
                      double hourlyRate,
                      double dailyRate,
                      String description,
                      String phone,
                      String createdByUserId,
                      Boolean hidden) {
            this.id = id;
            this.title = title;
            this.category = category;
            this.ownerName = ownerName;
            this.district = district;
            this.upazila = upazila;
            this.hourlyRate = hourlyRate;
            this.dailyRate = dailyRate;
            this.description = description;
            this.phone = phone;
            this.createdByUserId = createdByUserId;
            this.hidden = hidden;
        }
    }
}
