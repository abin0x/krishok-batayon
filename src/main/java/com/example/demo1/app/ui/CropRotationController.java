package com.example.demo1.app.ui;

import com.example.demo1.app.util.NavigationHelper; // Import Helper
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.URL;
import java.util.ResourceBundle;

public class CropRotationController implements Initializable {

    // --- Navigation Buttons ---
    @FXML private Button btnHome, btnAdvisory, btnGuide, btnFertilizer, btnIrrigation, btnCropRotation, btnLocalManagement, btnStorage,btnMachinery;

    // --- Inputs ---
    @FXML private ComboBox<String> districtComboBox, landTypeComboBox, soilTypeComboBox, currentSeasonComboBox, prevCropComboBox;
    @FXML private RadioButton irrigationYes;
    @FXML private Button generateBtn, resetBtn;

    // --- Results ---
    @FXML private VBox resultsContainer, emptyState;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("âœ… Crop Rotation Controller Initialized");

        // 1. Setup Navigation (1 Line)
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement,btnMachinery);
        NavigationHelper.setupAdvisoryNav(btnGuide, btnFertilizer, btnIrrigation, btnCropRotation);

        // 2. Setup Logic
        populateDropdowns();
        generateBtn.setOnAction(e -> calculateRotation());
        resetBtn.setOnAction(e -> resetForm());
    }

    // ===========================
    // 1. DATA & INPUTS
    // ===========================
    private void populateDropdowns() {
        districtComboBox.getItems().addAll("à¦¢à¦¾à¦•à¦¾", "à¦•à§à¦®à¦¿à¦²à§à¦²à¦¾", "à¦¬à¦—à§à¦¡à¦¼à¦¾", "à¦°à¦¾à¦œà¦¶à¦¾à¦¹à§€", "à¦°à¦‚à¦ªà§à¦°", "à¦¦à¦¿à¦¨à¦¾à¦œà¦ªà§à¦°", "à¦¯à¦¶à§‹à¦°", "à¦¬à¦°à¦¿à¦¶à¦¾à¦²");
        landTypeComboBox.getItems().addAll("à¦‰à¦à¦šà§ à¦œà¦®à¦¿", "à¦®à¦¾à¦à¦¾à¦°à¦¿ à¦‰à¦à¦šà§ à¦œà¦®à¦¿", "à¦®à¦¾à¦à¦¾à¦°à¦¿ à¦¨à¦¿à¦šà§ à¦œà¦®à¦¿", "à¦¨à¦¿à¦šà§ à¦œà¦®à¦¿");
        soilTypeComboBox.getItems().addAll("à¦¦à§‹à¦†à¦à¦¶", "à¦¬à§‡à¦²à§‡ à¦¦à§‹à¦†à¦à¦¶", "à¦à¦à¦Ÿà§‡à¦² à¦¦à§‹à¦†à¦à¦¶", "à¦à¦à¦Ÿà§‡à¦²");
        currentSeasonComboBox.getItems().addAll("à¦°à¦¬à¦¿ (à¦¶à§€à¦¤)", "à¦–à¦°à¦¿à¦«-à§§ (à¦—à§à¦°à§€à¦·à§à¦®)", "à¦–à¦°à¦¿à¦«-à§¨ (à¦¬à¦°à§à¦·à¦¾)");
        prevCropComboBox.getItems().addAll("à¦†à¦®à¦¨ à¦§à¦¾à¦¨", "à¦¬à§‹à¦°à§‹ à¦§à¦¾à¦¨", "à¦—à¦®", "à¦­à§à¦Ÿà§à¦Ÿà¦¾", "à¦†à¦²à§", "à¦¸à¦°à¦¿à¦·à¦¾", "à¦®à¦¸à§à¦° à¦¡à¦¾à¦²", "à¦ªà¦¾à¦Ÿ", "à¦¸à¦¬à¦œà¦¿");
    }

    // ===========================
    // 2. CALCULATION LOGIC
    // ===========================
    private void calculateRotation() {
        if (!validateInputs()) return;

        String land = landTypeComboBox.getValue();
        String soil = soilTypeComboBox.getValue();
        String season = currentSeasonComboBox.getValue();
        String prevCrop = prevCropComboBox.getValue();
        boolean irrigation = irrigationYes.isSelected();

        resultsContainer.getChildren().clear();
        emptyState.setVisible(false); emptyState.setManaged(false);

        // Logic for Suggesting Patterns
        if (prevCrop.contains("à¦†à¦®à¦¨") || season.contains("à¦°à¦¬à¦¿")) {
            if ((soil.contains("à¦¦à§‹à¦†à¦à¦¶") || soil.contains("à¦¬à§‡à¦²à§‡")) && irrigation) {
                addCard("à¦¬à¦¾à¦¨à¦¿à¦œà§à¦¯à¦¿à¦• à¦²à¦¾à¦­à¦œà¦¨à¦• à¦®à¦¡à§‡à¦²", "à¦¸à¦°à§à¦¬à¦¾à¦§à¦¿à¦• à¦®à§à¦¨à¦¾à¦«à¦¾", "ðŸ’°",
                        new Step("à¦¸à¦°à¦¿à¦·à¦¾/à¦†à¦²à§", "à¦°à¦¬à¦¿"), new Step("à¦¬à§‹à¦°à§‹/à¦­à§à¦Ÿà§à¦Ÿà¦¾", "à¦–à¦°à¦¿à¦«-à§§"), new Step("à¦†à¦®à¦¨ à¦§à¦¾à¦¨", "à¦–à¦°à¦¿à¦«-à§¨"),
                        "à¦†à¦²à§ à¦¬à¦¾ à¦¸à¦°à¦¿à¦·à¦¾ à¦¸à§à¦¬à¦²à§à¦ªà¦®à§‡à§Ÿà¦¾à¦¦à§€ à¦²à¦¾à¦­à¦œà¦¨à¦• à¦«à¦¸à¦²à¥¤ à¦à¦°à¦ªà¦° à¦¬à§‹à¦°à§‹ à¦¬à¦¾ à¦­à§à¦Ÿà§à¦Ÿà¦¾ à¦šà¦¾à¦· à¦•à¦°à¦²à§‡ à¦«à¦²à¦¨ à¦­à¦¾à¦²à§‹ à¦¹à§Ÿà¥¤");
            }

            addCard("à¦®à¦¾à¦Ÿà¦¿à¦° à¦¸à§à¦¬à¦¾à¦¸à§à¦¥à§à¦¯ à¦¸à§à¦°à¦•à§à¦·à¦¾ à¦®à¦¡à§‡à¦²", "à¦‰à¦°à§à¦¬à¦°à¦¤à¦¾ à¦¬à§ƒà¦¦à§à¦§à¦¿", "ðŸŒ¿",
                    new Step("à¦®à¦¸à§à¦°/à¦®à§à¦— à¦¡à¦¾à¦²", "à¦°à¦¬à¦¿"), new Step("à¦ªà¦¾à¦Ÿ/à¦†à¦‰à¦¶", "à¦–à¦°à¦¿à¦«-à§§"), new Step("à¦†à¦®à¦¨ à¦§à¦¾à¦¨", "à¦–à¦°à¦¿à¦«-à§¨"),
                    "à¦¡à¦¾à¦² à¦œà¦¾à¦¤à§€à§Ÿ à¦«à¦¸à¦² à¦®à¦¾à¦Ÿà¦¿à¦° à¦¨à¦¾à¦‡à¦Ÿà§à¦°à§‹à¦œà§‡à¦¨ à¦¬à¦¾à§œà¦¾à§Ÿà¥¤ à¦ªà¦¾à¦Ÿ à¦®à¦¾à¦Ÿà¦¿à¦° à¦—à¦ à¦¨ à¦­à¦¾à¦²à§‹ à¦°à¦¾à¦–à§‡à¥¤");

            if (land.contains("à¦‰à¦à¦šà§")) {
                addCard("à¦¸à§à¦¬à¦²à§à¦ª à¦¸à§‡à¦š à¦®à¦¡à§‡à¦²", "à¦ªà¦¾à¦¨à¦¿ à¦¸à¦¾à¦¶à§à¦°à§Ÿà§€", "ðŸ’§",
                        new Step("à¦—à¦®", "à¦°à¦¬à¦¿"), new Step("à¦®à§à¦— à¦¡à¦¾à¦²", "à¦–à¦°à¦¿à¦«-à§§"), new Step("à¦†à¦®à¦¨ à¦§à¦¾à¦¨", "à¦–à¦°à¦¿à¦«-à§¨"),
                        "à¦¬à§‹à¦°à§‹ à¦§à¦¾à¦¨à§‡à¦° à¦šà§‡à§Ÿà§‡ à¦—à¦®à§‡ à¦¸à§‡à¦š à¦•à¦® à¦²à¦¾à¦—à§‡à¥¤ à¦‰à¦à¦šà§ à¦œà¦®à¦¿à¦° à¦œà¦¨à§à¦¯ à¦à¦Ÿà¦¿ à¦†à¦¦à¦°à§à¦¶à¥¤");
            }
        }
        else if (prevCrop.contains("à¦¬à§‹à¦°à§‹") || season.contains("à¦–à¦°à¦¿à¦«-à§§")) {
            addCard("à¦¸à¦¬à§à¦œ à¦¸à¦¾à¦° à¦®à¦¡à§‡à¦²", "à¦œà§ˆà¦¬ à¦¸à¦¾à¦°", "ðŸ€",
                    new Step("à¦§à¦žà§à¦šà§‡", "à¦–à¦°à¦¿à¦«-à§§"), new Step("à¦†à¦®à¦¨ à¦§à¦¾à¦¨", "à¦–à¦°à¦¿à¦«-à§¨"), new Step("à¦¸à¦°à¦¿à¦·à¦¾", "à¦°à¦¬à¦¿"),
                    "à¦§à¦žà§à¦šà§‡ à¦šà¦¾à¦· à¦•à¦°à§‡ à¦®à¦¾à¦Ÿà¦¿à¦¤à§‡ à¦®à¦¿à¦¶à¦¿à§Ÿà§‡ à¦¦à¦¿à¦²à§‡ à¦‡à¦‰à¦°à¦¿à§Ÿà¦¾ à¦¸à¦¾à¦°à§‡à¦° à¦–à¦°à¦š à¦…à¦°à§à¦§à§‡à¦• à¦•à¦®à§‡ à¦¯à¦¾à§Ÿà¥¤");

            addCard("à¦…à¦°à§à¦¥à¦•à¦°à§€ à¦«à¦¸à¦² à¦®à¦¡à§‡à¦²", "à¦ªà¦¾à¦Ÿ à¦šà¦¾à¦·", "ðŸ’¸",
                    new Step("à¦ªà¦¾à¦Ÿ", "à¦–à¦°à¦¿à¦«-à§§"), new Step("à¦†à¦®à¦¨ à¦§à¦¾à¦¨", "à¦–à¦°à¦¿à¦«-à§¨"), new Step("à¦—à¦®", "à¦°à¦¬à¦¿"),
                    "à¦ªà¦¾à¦Ÿà§‡à¦° à¦ªà¦¾à¦¤à¦¾ à¦ªà¦šà§‡ à¦®à¦¾à¦Ÿà¦¿à¦° à¦‰à¦°à§à¦¬à¦°à¦¤à¦¾ à¦¬à¦¾à§œà¦¾à§Ÿ à¦à¦¬à¦‚ à¦à¦Ÿà¦¿ à¦²à¦¾à¦­à¦œà¦¨à¦•à¥¤");
        }
        else {
            addCard("à¦†à¦¦à¦°à§à¦¶ à¦¸à¦¬à¦œà¦¿ à¦šà¦•à§à¦°", "à¦ªà¦¾à¦°à¦¿à¦¬à¦¾à¦°à¦¿à¦• à¦ªà§à¦·à§à¦Ÿà¦¿", "ðŸ¥—",
                    new Step("à¦¬à§‡à¦—à§à¦¨/à¦Ÿà¦®à§‡à¦Ÿà§‹", "à¦°à¦¬à¦¿"), new Step("à¦²à¦¾à¦²à¦¶à¦¾à¦•", "à¦–à¦°à¦¿à¦«-à§§"), new Step("à¦²à¦¤à¦¾à¦œà¦¾à¦¤à§€à¦¯à¦¼", "à¦–à¦°à¦¿à¦«-à§¨"),
                    "à¦à¦•à¦‡ à¦œà¦®à¦¿à¦¤à§‡ à¦¬à¦¾à¦°à¦¬à¦¾à¦° à¦à¦•à¦‡ à¦¸à¦¬à¦œà¦¿ à¦¨à¦¾ à¦•à¦°à§‡ à¦šà¦•à§à¦°à¦¾à¦•à¦¾à¦°à§‡ à¦šà¦¾à¦· à¦•à¦°à§à¦¨à¥¤");
        }
    }

    private boolean validateInputs() {
        if (landTypeComboBox.getValue() == null || soilTypeComboBox.getValue() == null ||
                currentSeasonComboBox.getValue() == null || prevCropComboBox.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "à¦¦à¦¯à¦¼à¦¾ à¦•à¦°à§‡ à¦¸à¦¬ à¦¤à¦¥à§à¦¯ à¦ªà§‚à¦°à¦£ à¦•à¦°à§à¦¨à¥¤").show();
            return false;
        }
        return true;
    }

    // ===========================
    // 3. UI GENERATION
    // ===========================
    private void addCard(String title, String badge, String badgeIcon, Step s1, Step s2, Step s3, String tip) {
        VBox card = new VBox(10);
        card.getStyleClass().add("rotation-card");

        // Header
        Label badgeLbl = new Label(badgeIcon + " " + badge);
        badgeLbl.getStyleClass().add("option-badge");
        HBox header = new HBox(10, new Label(title), new Region(), badgeLbl);
        HBox.setHgrow(header.getChildren().get(1), Priority.ALWAYS);
        ((Label)header.getChildren().get(0)).getStyleClass().add("option-title");

        // Cycle View
        HBox cycle = new HBox(5, createStep(s1), createArrow(), createStep(s2), createArrow(), createStep(s3));
        cycle.setAlignment(Pos.CENTER);
        cycle.getStyleClass().add("cycle-container");

        // Benefit Footer
        HBox footer = new HBox(10, new Label("ðŸ’¡"), new Label(tip));
        footer.getStyleClass().add("benefit-box");
        ((Label)footer.getChildren().get(1)).setWrapText(true);

        card.getChildren().addAll(header, cycle, footer);
        resultsContainer.getChildren().add(card);
    }

    private VBox createStep(Step s) {
        VBox box = new VBox(2, new Label(getCropIcon(s.name)), new Label(s.name), new Label(s.season));
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("cycle-step");
        box.setPrefWidth(90);
        ((Label)box.getChildren().get(1)).setWrapText(true); // Name wrapping
        ((Label)box.getChildren().get(1)).getStyleClass().add("step-crop");
        ((Label)box.getChildren().get(2)).getStyleClass().add("step-season");
        return box;
    }

    private Label createArrow() {
        Label arrow = new Label("âžœ");
        arrow.getStyleClass().add("arrow-icon");
        return arrow;
    }

    private String getCropIcon(String name) {
        if (name.contains("à¦§à¦¾à¦¨")) return "ðŸŒ¾";
        if (name.contains("à¦†à¦²à§") || name.contains("à¦¸à¦¬à¦œà¦¿")) return "ðŸ¥”";
        if (name.contains("à¦­à§à¦Ÿà§à¦Ÿà¦¾")) return "ðŸŒ½";
        if (name.contains("à¦ªà¦¾à¦Ÿ") || name.contains("à¦§à¦žà§à¦šà§‡")) return "ðŸŒ¿";
        if (name.contains("à¦¸à¦°à¦¿à¦·à¦¾")) return "ðŸŒ¼";
        if (name.contains("à¦¡à¦¾à¦²")) return "ðŸ¥˜";
        return "ðŸŒ±";
    }

    private void resetForm() {
        prevCropComboBox.setValue(null);
        resultsContainer.getChildren().clear();
        emptyState.setVisible(true); emptyState.setManaged(true);
    }

    // --- Helper Class ---
    private static class Step {
        String name, season;
        Step(String n, String s) { name = n; season = s; }
    }
}


