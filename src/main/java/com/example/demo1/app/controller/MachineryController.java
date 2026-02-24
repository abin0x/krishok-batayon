package com.example.demo1.app.controller;

import com.example.demo1.app.model.User;
import com.example.demo1.app.util.NavigationHelper;
import com.example.demo1.app.util.SessionManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MachineryController {

    private static final String DATA_FILE = "machinery_data.json";
    private static final String OWNER_ONLY_MSG = "Only the record creator can edit or delete this entry.";
    private static final Type MACHINE_LIST_TYPE = new TypeToken<ArrayList<MachineRecord>>() {}.getType();//etar maddhome amra bole dichi je json file er moddhe array of MachineRecord ache

    @FXML private Button btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery, addMachineBtn;
    @FXML private FlowPane machineryContainer;

    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();//etar maddhome amra json file ke pretty print korbo mane easily readable format e save korbo
    private final DecimalFormat moneyFormat = new DecimalFormat("#,##0");
    private final List<MachineRecord> machineRecords = new ArrayList<>();//etar maddhome amra machine record gulo ke memory te store korbo jokhon user add/edit/delete korbe

    @FXML
    public void initialize() {
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);
        loadData();
        setupActions();
        renderCards();
    }
    
    //ei method gulo te amra button click er action set korbo, jemon addMachineBtn click korle showAddDialog() method call hobe
    private void setupActions() {
        if (addMachineBtn != null) {
            addMachineBtn.setOnAction(e -> showAddDialog());
        }
    }

    //ei method gulo te amra json file theke data load korbo, data save korbo, card render korbo, add/edit/delete dialog show korbo, and utility methods for safe text handling and authorization check
    private void loadData() {
        File file = new File(DATA_FILE);
        if (!file.exists()) {
            return;
        }

        try (Reader reader = new FileReader(file)) {
            List<MachineRecord> loaded = gson.fromJson(reader, MACHINE_LIST_TYPE);
            if (loaded != null) {
                machineRecords.clear();
                machineRecords.addAll(loaded);
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Load Error", "Could not read machinery data.");
        }
    }

    //ei method gulo te amra json file e machineRecords list ke save korbo jokhon user add/edit/delete korbe
    private void saveData() {
        try (Writer writer = new FileWriter(DATA_FILE)) {
            gson.toJson(machineRecords, writer);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Save Error", "Could not save machinery data.");
        }
    }

    //ei method gulo te amra machineRecords list er data ke card format e render korbo machineryContainer er moddhe, jodi kono record na thake tahole ekta empty state show korbo
    private void renderCards() {
        if (machineryContainer == null) {
            return;
        }

        machineryContainer.getChildren().clear();

        if (machineRecords.isEmpty()) {
            VBox empty = new VBox(8);
            empty.setAlignment(Pos.CENTER);
            empty.setPadding(new Insets(30));
            empty.getStyleClass().add("machine-card");

            Label title = new Label("No machinery data found");
            title.getStyleClass().add("card-title");

            Label sub = new Label("Click '+ Add Machine' to create your first entry");
            sub.getStyleClass().add("desc-text");

            empty.getChildren().addAll(title, sub);
            machineryContainer.getChildren().add(empty);
            return;
        }

        machineRecords.forEach(record -> machineryContainer.getChildren().add(createCard(record)));//etar maddhome amra machineRecords list er prottek record ke createCard() method er maddhome ekta card e convert kore machineryContainer er moddhe add korbo
    }

    private VBox createCard(MachineRecord record) {
        VBox card = new VBox(10);
        card.getStyleClass().add("machine-card");

        Label title = new Label(safeText(record.title));
        title.getStyleClass().add("card-title");
        title.setWrapText(true);

        Label status = new Label("Available");
        status.getStyleClass().add("badge-available");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox top = new HBox(10, title, spacer, status);
        top.setAlignment(Pos.CENTER_LEFT);

        VBox details = new VBox(7,
                detailRow("Owner", safeText(record.ownerName)),
                detailRow("Phone", safeText(record.phone)),
                detailRow("Rate", "Tk " + moneyFormat.format(record.hourlyRate) + "/hour | Tk " + moneyFormat.format(record.dailyRate) + "/day")
        );
        details.getStyleClass().add("details-box");

        Label desc = new Label(safeText(record.description));
        desc.getStyleClass().add("desc-text");
        desc.setWrapText(true);

        // Edit/Delete buttons only for the record creator
        HBox actions = new HBox(8);
        if (canModifyRecord(record)) {
            Button editBtn = new Button("Edit");
            editBtn.getStyleClass().add("btn-secondary");
            editBtn.setOnAction(e -> showEditDialog(record));

            Button deleteBtn = new Button("Delete");
            deleteBtn.getStyleClass().add("btn-danger");
            deleteBtn.setOnAction(e -> confirmDelete(record));
            actions.getChildren().addAll(editBtn, deleteBtn);
        } else {
            Label ownerOnly = new Label("Owner only: edit/delete");
            ownerOnly.getStyleClass().add("owner-note");
            actions.getChildren().add(ownerOnly);
        }

        card.getChildren().addAll(top, details, desc, actions);
        return card;
    }


    //ei method gulo te amra machine record er prottek detail ke ekta row format e convert korbo, jekhane label and value thakbe, and value te styling apply korbo
    private HBox detailRow(String label, String value) {
        Label labelNode = new Label(label + ":");
        Label valueNode = new Label(value);
        valueNode.getStyleClass().add("detail-text");
        return new HBox(8, labelNode, valueNode);
    }

    //ei method gulo te amra add/edit dialog show korbo, jekhane user machine record er details input korte parbe, and input validation o thakbe, jodi user input thik na kore tahole warning alert show korbo
    private void showAddDialog() {
        String currentUserId = currentUserIdOrNull();
        if (currentUserId == null) {
            showAlert(Alert.AlertType.WARNING, "Unauthorized", "Please login before adding machinery.");
            return;
        }

        MachineRecord created = showMachineDialog(null);
        if (created == null) {
            return;
        }

        created.id = UUID.randomUUID().toString();//etar maddhome amra prottek machine record er jonno ekta unique id generate korbo
        created.createdByUserId = currentUserId;
        machineRecords.add(0, created);

        saveData();
        renderCards();
    }


    //ei method gulo te amra edit dialog show korbo, jekhane user existing machine record er details edit korte parbe, and input validation o thakbe, jodi user input thik na kore tahole warning alert show korbo, and jodi user current record er creator na hoy tahole unauthorized warning show korbo
    private void showEditDialog(MachineRecord original) {
        if (!canModifyRecord(original)) {
            showAlert(Alert.AlertType.WARNING, "Unauthorized", OWNER_ONLY_MSG);
            return;
        }

        MachineRecord edited = showMachineDialog(original);
        if (edited == null) {
            return;
        }


        // Update original record with edited values
        original.title = edited.title;
        original.ownerName = edited.ownerName;
        original.hourlyRate = edited.hourlyRate;
        original.dailyRate = edited.dailyRate;
        original.description = edited.description;
        original.phone = edited.phone;

        saveData();
        renderCards();
    }


    //ei method gulo te amra machine record er details input er jonno ekta dialog show korbo, jekhane user input nibe, and input validation o korbe, jodi user input thik na kore tahole warning alert show korbo, and jodi user cancel button click kore tahole null return korbo
    private MachineRecord showMachineDialog(MachineRecord existing) {
        Dialog<MachineRecord> dialog = new Dialog<>();
        dialog.setTitle(existing == null ? "Add Machine" : "Edit Machine");
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(16));

        TextField titleField = new TextField(existing == null ? "" : safeText(existing.title));
        TextField ownerField = new TextField(existing == null ? "" : safeText(existing.ownerName));
        TextField hourlyField = new TextField(existing == null ? "" : String.valueOf((int) existing.hourlyRate));
        TextField dailyField = new TextField(existing == null ? "" : String.valueOf((int) existing.dailyRate));
        TextField phoneField = new TextField(existing == null ? "" : safeText(existing.phone));
        TextArea descriptionField = new TextArea(existing == null ? "" : safeText(existing.description));
        descriptionField.setPrefRowCount(3);

        grid.addRow(0, new Label("Name:"), titleField);
        grid.addRow(1, new Label("Owner:"), ownerField);
        grid.addRow(2, new Label("Hourly Rate:"), hourlyField);
        grid.addRow(3, new Label("Daily Rate:"), dailyField);
        grid.addRow(4, new Label("Phone:"), phoneField);
        grid.addRow(5, new Label("Description:"), descriptionField);

        dialog.getDialogPane().setContent(grid);//etar maddhome amra dialog er content hishebe grid pane set korbo, jekhane user machine record er details input korbe


        // Convert result to MachineRecord when OK is clicked, with input validation, and show warning alert if validation fails
        dialog.setResultConverter(btn -> {
            if (btn != ButtonType.OK) {
                return null;
            }
            try {
                MachineRecord draft = new MachineRecord();
                draft.title = safeTrim(titleField.getText());
                draft.ownerName = safeTrim(ownerField.getText());
                draft.hourlyRate = Double.parseDouble(safeTrim(hourlyField.getText()));
                draft.dailyRate = Double.parseDouble(safeTrim(dailyField.getText()));
                draft.phone = safeTrim(phoneField.getText());
                draft.description = safeTrim(descriptionField.getText());

                if (draft.title.isEmpty() || draft.ownerName.isEmpty() || draft.phone.isEmpty()) {
                    throw new IllegalArgumentException("missing");
                }
                return draft;
            } catch (Exception ex) {
                showAlert(Alert.AlertType.WARNING, "Input Error", "Please fill all required fields correctly.");
                return null;
            }
        });

        return dialog.showAndWait().orElse(null);
    }

    
    private void confirmDelete(MachineRecord record) {
        if (!canModifyRecord(record)) {
            showAlert(Alert.AlertType.WARNING, "Unauthorized", OWNER_ONLY_MSG);
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                safeText(record.title) + " - delete this record?",
                ButtonType.YES,
                ButtonType.NO);
        alert.setTitle("Delete Record");
        alert.setHeaderText(null);

        alert.showAndWait().ifPresent(type -> {
            if (type == ButtonType.YES) {
                machineRecords.remove(record);
                saveData();
                renderCards();
            }
        });
    }

    private String safeText(String value) {
        return value == null || value.isBlank() ? "-" : value;
    }

    private String safeTrim(String value) {
        return value == null ? "" : value.trim();
    }

    private boolean canModifyRecord(MachineRecord record) {
        if (record == null) {
            return false;
        }
        String current = currentUserIdOrNull();
        String owner = safeTrim(record.createdByUserId);
        return current != null && !owner.isEmpty() && current.equals(owner);
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
        String ownerName;
        double hourlyRate;
        double dailyRate;
        String description;
        String phone;
        String createdByUserId;

        MachineRecord() {}
    }
}
