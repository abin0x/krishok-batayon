package com.example.demo1.app.ui;

import com.example.demo1.app.util.NavigationHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class CropRotationController implements Initializable {

    @FXML private Button btnHome, btnAdvisory, btnGuide, btnFertilizer, btnIrrigation, btnCropRotation, btnLocalManagement, btnStorage, btnMachinery;
    @FXML private ComboBox<String> districtComboBox, landTypeComboBox, soilTypeComboBox, currentSeasonComboBox, prevCropComboBox;
    @FXML private RadioButton irrigationYes;
    @FXML private RadioButton irrigationRain;
    @FXML private Button generateBtn, resetBtn;
    @FXML private VBox resultsContainer, emptyState;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);
        NavigationHelper.setupAdvisoryNav(btnGuide, btnFertilizer, btnIrrigation, btnCropRotation);

        populateDropdowns();
        generateBtn.setOnAction(e -> calculateRotation());
        resetBtn.setOnAction(e -> resetForm());
    }

    private void populateDropdowns() {
        districtComboBox.getItems().addAll("ঢাকা", "কুমিল্লা", "বগুড়া", "রাজশাহী", "রংপুর", "দিনাজপুর", "যশোর", "বরিশাল");
        landTypeComboBox.getItems().addAll("উঁচু জমি", "মাঝারি উঁচু জমি", "মাঝারি নিচু জমি", "নিচু জমি");
        soilTypeComboBox.getItems().addAll("দোআঁশ", "বেলে দোআঁশ", "এঁটেল দোআঁশ", "এঁটেল");
        currentSeasonComboBox.getItems().addAll("রবি (শীত)", "খরিফ-১ (গ্রীষ্ম)", "খরিফ-২ (বর্ষা)");
        prevCropComboBox.getItems().addAll("আমন ধান", "বোরো ধান", "গম", "ভুট্টা", "আলু", "সরিষা", "মসুর ডাল", "পাট", "সবজি");
    }

    private void calculateRotation() {
        if (!validateInputs()) {
            return;
        }

        String land = landTypeComboBox.getValue();
        String soil = soilTypeComboBox.getValue();
        String season = currentSeasonComboBox.getValue();
        String prevCrop = prevCropComboBox.getValue();
        boolean hasIrrigation = irrigationYes.isSelected();

        resultsContainer.getChildren().clear();
        emptyState.setVisible(false);
        emptyState.setManaged(false);

        if (prevCrop.contains("আমন") || season.contains("রবি")) {
            if ((soil.contains("দোআঁশ") || soil.contains("বেলে")) && hasIrrigation) {
                addCard(
                        "বাণিজ্যিক লাভজনক মডেল",
                        "উচ্চ মুনাফা",
                        "💰",
                        new Step("সরিষা/আলু", "রবি"),
                        new Step("বোরো/ভুট্টা", "খরিফ-১"),
                        new Step("আমন ধান", "খরিফ-২"),
                        "আলু বা সরিষা দিয়ে শুরু করলে দ্রুত নগদ অর্থ আসে, পরে ধান/ভুট্টায় ফলন ভালো হয়।"
                );
            }

            addCard(
                    "মাটির স্বাস্থ্য মডেল",
                    "উর্বরতা বৃদ্ধি",
                    "🌿",
                    new Step("মসুর/মুগ ডাল", "রবি"),
                    new Step("পাট/আউশ", "খরিফ-১"),
                    new Step("আমন ধান", "খরিফ-২"),
                    "ডাল ফসল নাইট্রোজেন বাড়ায়, পাট মাটির গঠন উন্নত করে।"
            );

            if (land.contains("উঁচু")) {
                addCard(
                        "স্বল্প সেচ মডেল",
                        "পানি সাশ্রয়ী",
                        "💧",
                        new Step("গম", "রবি"),
                        new Step("মুগ ডাল", "খরিফ-১"),
                        new Step("আমন ধান", "খরিফ-২"),
                        "উঁচু জমিতে গম ও ডাল যুক্ত চক্র পানি ও সারের খরচ কমাতে সহায়তা করে।"
                );
            }
        } else if (prevCrop.contains("বোরো") || season.contains("খরিফ-১")) {
            addCard(
                    "সবুজ সার মডেল",
                    "জৈব উপযোগী",
                    "🌱",
                    new Step("ঢেঁইচা", "খরিফ-১"),
                    new Step("আমন ধান", "খরিফ-২"),
                    new Step("সরিষা", "রবি"),
                    "ঢেঁইচা মাটিতে মিশালে জৈব পদার্থ বাড়ে এবং রাসায়নিক সারের উপর নির্ভরতা কমে।"
            );

            addCard(
                    "অর্থকরী চক্র",
                    "নগদ আয়",
                    "💸",
                    new Step("পাট", "খরিফ-১"),
                    new Step("আমন ধান", "খরিফ-২"),
                    new Step("গম", "রবি"),
                    "পাট ও গম একসাথে আয় ও মাটির ব্যবহার দক্ষতা বাড়ায়।"
            );
        } else {
            addCard(
                    "সবজি ভিত্তিক চক্র",
                    "পারিবারিক পুষ্টি",
                    "🥗",
                    new Step("বেগুন/টমেটো", "রবি"),
                    new Step("লালশাক", "খরিফ-১"),
                    new Step("লতাজাতীয়", "খরিফ-২"),
                    "একই জমিতে এক ফসল বারবার না করে চক্রাকারে সবজি চাষ করলে রোগ কমে।"
            );
        }
    }

    private boolean validateInputs() {
        if (landTypeComboBox.getValue() == null
                || soilTypeComboBox.getValue() == null
                || currentSeasonComboBox.getValue() == null
                || prevCropComboBox.getValue() == null) {
            new Alert(Alert.AlertType.WARNING, "দয়া করে সব বাধ্যতামূলক তথ্য পূরণ করুন।").show();
            return false;
        }
        return true;
    }

    private void addCard(String title, String badge, String badgeIcon, Step s1, Step s2, Step s3, String tip) {
        VBox card = new VBox(10);
        card.getStyleClass().add("rotation-card");

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("option-title");

        Label badgeLabel = new Label(badgeIcon + " " + badge);
        badgeLabel.getStyleClass().add("option-badge");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(10, titleLabel, spacer, badgeLabel);

        HBox cycle = new HBox(8, createStep(s1), createArrow(), createStep(s2), createArrow(), createStep(s3));
        cycle.setAlignment(Pos.CENTER);
        cycle.getStyleClass().add("cycle-container");

        HBox footer = new HBox(10, new Label("💡"), new Label(tip));
        footer.getStyleClass().add("benefit-box");
        ((Label) footer.getChildren().get(1)).setWrapText(true);

        card.getChildren().addAll(header, cycle, footer);
        resultsContainer.getChildren().add(card);
    }

    private VBox createStep(Step s) {
        Label iconLabel = new Label(getCropIcon(s.name));
        Label cropLabel = new Label(s.name);
        cropLabel.getStyleClass().add("step-crop");
        cropLabel.setWrapText(true);
        Label seasonLabel = new Label(s.season);
        seasonLabel.getStyleClass().add("step-season");

        VBox box = new VBox(3, iconLabel, cropLabel, seasonLabel);
        box.setAlignment(Pos.CENTER);
        box.getStyleClass().add("cycle-step");
        box.setPrefWidth(96);
        return box;
    }

    private Label createArrow() {
        Label arrow = new Label("➜");
        arrow.getStyleClass().add("arrow-icon");
        return arrow;
    }

    private String getCropIcon(String name) {
        if (name.contains("ধান")) return "🌾";
        if (name.contains("আলু") || name.contains("সবজি")) return "🥔";
        if (name.contains("ভুট্টা")) return "🌽";
        if (name.contains("পাট") || name.contains("ঢেঁইচা")) return "🌿";
        if (name.contains("সরিষা")) return "🌼";
        if (name.contains("ডাল")) return "🥜";
        return "🌱";
    }

    private void resetForm() {
        districtComboBox.setValue(null);
        landTypeComboBox.setValue(null);
        soilTypeComboBox.setValue(null);
        currentSeasonComboBox.setValue(null);
        prevCropComboBox.setValue(null);
        irrigationYes.setSelected(true);
        if (irrigationRain != null) {
            irrigationRain.setSelected(false);
        }
        resultsContainer.getChildren().clear();
        emptyState.setVisible(true);
        emptyState.setManaged(true);
    }

    private static class Step {
        private final String name;
        private final String season;

        Step(String name, String season) {
            this.name = name;
            this.season = season;
        }
    }
}
