package com.example.demo1.app.controller;

import com.example.demo1.app.util.NavigationHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import com.example.demo1.app.util.UiAlerts;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FertilizerCalculatorController implements Initializable {

    @FXML private Button btnHome, btnAdvisory, btnLocalManagement, btnStorage, btnMachinery;
    @FXML private Button btnGuide, btnFertilizer, btnIrrigation, btnCropRotation;

    @FXML private ComboBox<String> cropComboBox, varietyComboBox, seasonComboBox, unitComboBox, soilTypeComboBox, previousCropComboBox;
    @FXML private TextField landAreaField, organicManureAmount;
    @FXML private RadioButton nLow, nMedium, nHigh, pLow, pMedium, pHigh, kLow, kMedium, kHigh;
    @FXML private CheckBox organicManureCheck;
    @FXML private Button calculateBtn, resetBtn;

    @FXML private VBox emptyResultState, resultContentContainer;
    @FXML private VBox fertilizerResultsContainer, costBreakdownContainer, timelineContainer;
    @FXML private Label totalCostLabel, summaryCrop, summaryLand, summarySoil;

    private final Map<String, CropData> cropDatabase = new HashMap<>();
    private final Map<String, Double> prices = new HashMap<>();
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);
        NavigationHelper.setupAdvisoryNav(btnGuide, btnFertilizer, btnIrrigation, btnCropRotation);
        initData();
        setupInputs();
        setupActions();
    }

    private void initData() {
        addCrop("সরিষা (Mustard)", new String[]{"রবি"}, new double[]{90, 30, 40}, "BARI-14", "লোকাল");
        addCrop("ধান (Boro)", new String[]{"রবি"}, new double[]{140, 50, 60}, "BRRI-28", "হাইব্রিড");
        addCrop("গম (Wheat)", new String[]{"রবি"}, new double[]{110, 40, 50}, "BARI-30", "প্রদীপ");
        addCrop("আলু (Potato)", new String[]{"রবি"}, new double[]{150, 60, 120}, "ডায়মন্ড", "কার্ডিনাল");

        prices.put("Urea", 27.0);
        prices.put("TSP", 24.0);
        prices.put("MOP", 18.0);
        prices.put("Gypsum", 12.0);
        prices.put("Zinc", 180.0);
    }

    private void addCrop(String name, String[] seasons, double[] npk, String... vars) {
        cropDatabase.put(name, new CropData(vars, seasons, npk));
    }

    private void setupInputs() {
        cropComboBox.getItems().addAll(cropDatabase.keySet());
        unitComboBox.getItems().addAll("একর", "হেক্টর", "শতাংশ", "বিঘা");
        unitComboBox.setValue("একর");
        soilTypeComboBox.getItems().addAll("দো-আঁশ", "বেলে দো-আঁশ", "এঁটেল", "পলি মাটি");
        previousCropComboBox.getItems().addAll("কিছু না", "ধান", "গম", "ডাল জাতীয়", "সবজি");

        cropComboBox.setOnAction(e -> {
            CropData d = cropDatabase.get(cropComboBox.getValue());
            if (d != null) {
                varietyComboBox.getItems().setAll(d.varieties);
                varietyComboBox.getSelectionModel().selectFirst();
                seasonComboBox.getItems().setAll(d.seasons);
                seasonComboBox.getSelectionModel().selectFirst();
            }
        });
    }

    private void setupActions() {
        calculateBtn.setOnAction(e -> calculate());
        resetBtn.setOnAction(e -> reset());
        organicManureCheck.setOnAction(e -> organicManureAmount.setDisable(!organicManureCheck.isSelected()));
        nMedium.setSelected(true);
        pMedium.setSelected(true);
        kMedium.setSelected(true);
    }

    private void calculate() {
        if (!validate()) return;

        try {
            double area = Double.parseDouble(landAreaField.getText().trim());
            double ha = convertToHa(area, unitComboBox.getValue());
            CropData data = cropDatabase.get(cropComboBox.getValue());
            double[] npk = data.npk.clone();

            if (varietyComboBox.getValue() != null && varietyComboBox.getValue().contains("হাইব্রিড")) {
                npk[0] *= 1.2;
                npk[1] *= 1.2;
                npk[2] *= 1.2;
            }
            adjustForSoil(npk);
            adjustForManure(npk);

            double urea = Math.max(0, npk[0] * 2.17 * ha);
            double tsp = Math.max(0, npk[1] * 2.17 * ha);
            double mop = Math.max(0, npk[2] * 1.67 * ha);
            double gyp = 60 * ha;
            double zinc = 8 * ha;

            displayResults(area, urea, tsp, mop, gyp, zinc);
        } catch (NumberFormatException e) {
            UiAlerts.error("??????", "জমির পরিমাণ সঠিকভাবে লিখুন।");
        }
    }

    private void adjustForSoil(double[] npk) {
        if (nHigh.isSelected()) npk[0] *= 0.6; else if (nLow.isSelected()) npk[0] *= 1.25;
        if (pHigh.isSelected()) npk[1] *= 0.6; else if (pLow.isSelected()) npk[1] *= 1.25;
        if (kHigh.isSelected()) npk[2] *= 0.6; else if (kLow.isSelected()) npk[2] *= 1.25;
    }

    private void adjustForManure(double[] npk) {
        if (organicManureCheck.isSelected() && !organicManureAmount.getText().isBlank()) {
            try {
                double ton = Double.parseDouble(organicManureAmount.getText());
                npk[0] -= ton * 4;
                npk[1] -= ton * 1.5;
                npk[2] -= ton * 4;
            } catch (Exception ignored) {
            }
        }
    }

    private void displayResults(double area, double u, double t, double m, double g, double z) {
        summaryCrop.setText(cropComboBox.getValue());
        summaryLand.setText(area + " " + unitComboBox.getValue());
        summarySoil.setText(soilTypeComboBox.getValue());

        fertilizerResultsContainer.getChildren().clear();
        costBreakdownContainer.getChildren().clear();
        timelineContainer.getChildren().clear();

        double total = 0;
        total += addCard("ইউরিয়া (Urea)", u, prices.get("Urea"), "নাইট্রোজেন");
        total += addCard("টিএসপি (TSP)", t, prices.get("TSP"), "ফসফরাস");
        total += addCard("এমওপি (MoP)", m, prices.get("MOP"), "পটাশ");
        total += addCard("জিপসাম", g, prices.get("Gypsum"), "সালফার");
        total += addCard("জিংক", z, prices.get("Zinc"), "মাইক্রোনিউট্রিয়েন্ট");

        totalCostLabel.setText("৳ " + df.format(total));

        addTimeline("1", "জমি তৈরির শেষ চাষে TSP, MOP, জিপসাম ও জিংক প্রয়োগ করুন।");
        addTimeline("2", "চারা রোপণের ৭-১০ দিন পর ইউরিয়ার ১ম কিস্তি দিন।");
        addTimeline("3", "৩০ দিন পর ইউরিয়ার ২য় কিস্তি দিন।");

        emptyResultState.setVisible(false);
        emptyResultState.setManaged(false);
        resultContentContainer.setVisible(true);
        resultContentContainer.setManaged(true);
    }

    private double addCard(String name, double kg, double price, String sub) {
        if (kg <= 0.1) return 0;

        HBox card = new HBox(15);
        card.getStyleClass().add("result-card");
        card.setAlignment(Pos.CENTER_LEFT);

        Label title = new Label(name);
        title.setStyle("-fx-font-weight: bold; -fx-text-fill: #2e7d32;");
        VBox left = new VBox(3, title, new Label(sub));

        Label weight = new Label(df.format(kg) + " কেজি");
        weight.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        card.getChildren().addAll(left, spacer, weight);
        fertilizerResultsContainer.getChildren().add(card);

        double cost = kg * price;
        HBox row = new HBox(10, new Label("• " + name), new Region(), new Label("৳ " + df.format(cost)));
        HBox.setHgrow(row.getChildren().get(1), Priority.ALWAYS);
        costBreakdownContainer.getChildren().add(row);
        return cost;
    }

    private void addTimeline(String num, String txt) {
        Label badge = new Label(num);
        badge.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-padding: 2 8; -fx-background-radius: 10;");
        Label text = new Label(txt);
        text.setWrapText(true);
        timelineContainer.getChildren().add(new HBox(10, badge, text));
    }

    private boolean validate() {
        if (cropComboBox.getValue() == null || landAreaField.getText().isBlank()) {
            UiAlerts.warning("???????", "প্রয়োজনীয় তথ্য পূরণ করুন।");
            return false;
        }
        return true;
    }

    private void reset() {
        landAreaField.clear();
        cropComboBox.setValue(null);
        resultContentContainer.setVisible(false);
        resultContentContainer.setManaged(false);
        emptyResultState.setVisible(true);
        emptyResultState.setManaged(true);
    }

    private double convertToHa(double val, String unit) {
        if ("হেক্টর".equals(unit)) return val;
        if ("শতাংশ".equals(unit)) return val * 0.004046;
        if ("বিঘা".equals(unit)) return val * 0.1338;
        return val * 0.4046;
    }

    private static class CropData {
        String[] varieties;
        String[] seasons;
        double[] npk;

        CropData(String[] v, String[] s, double[] n) {
            varieties = v;
            seasons = s;
            npk = n;
        }
    }
}
