package com.example.demo1.app.ui;

import com.example.demo1.app.util.NavigationHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class IrrigationCalculatorController implements Initializable {

    @FXML private Button btnHome, btnAdvisory, btnAiHelper, btnLocalManagement, btnStorage, btnMachinery;
    @FXML private Button btnGuide, btnFertilizer, btnIrrigation, btnCropRotation;

    @FXML private ComboBox<String> cropComboBox, varietyComboBox, growthStageComboBox;
    @FXML private TextField cropAgeField, landAreaField;
    @FXML private ComboBox<String> farmingMethodComboBox, unitComboBox, soilTypeComboBox, landTypeComboBox, seasonComboBox;
    @FXML private ComboBox<String> waterSourceComboBox, pumpPowerComboBox, pipeTypeComboBox, pipeLengthUnitComboBox;
    @FXML private TextField pipeLengthField;
    @FXML private CheckBox strongSunCheck, windyCheck, cloudyCheck, recentRainCheck;
    @FXML private RadioButton tempCold, tempModerate, tempHot, rainForecastYes;
    @FXML private TextField rainfallAmount, lastIrrigationField;
    @FXML private RadioButton methodSprinkler, methodDrip, methodFlood, methodFurrow;
    @FXML private Button calculateBtn, resetBtn;

    @FXML private VBox emptyResultState, resultContentContainer, scheduleContainer;
    @FXML private Label waterPerIrrigationLabel, efficiencyLabel, timePerIrrigationLabel;
    @FXML private Label fuelCostLabel, laborCostLabel, maintenanceCostLabel, totalCostLabel;

    private final Map<String, CropWaterRequirement> cropDataMap = new HashMap<>();
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupNavigation();
        initData();
        setupInputs();
        setupActions();
    }

    private void setupNavigation() {
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);
        NavigationHelper.setupAdvisoryNav(btnGuide, btnFertilizer, btnIrrigation, btnCropRotation);
    }

    private void initData() {
        addCrop("ধান (Boro)", 5.0, 3, 25, new String[]{"চারা রোপণ", "কুশি", "থোড়", "পাকা"}, "ব্রি ধান-28", "ব্রি ধান-29");
        addCrop("ধান (Aman)", 4.5, 4, 20, new String[]{"চারা রোপণ", "কুশি", "থোড়", "পাকা"}, "ব্রি ধান-49", "ব্রি ধান-71");
        addCrop("গম (Wheat)", 4.0, 7, 5, new String[]{"বপন", "কুশি", "শীষ", "দানা পূর্ণতা"}, "বারি গম-30", "প্রদীপ");
        addCrop("আলু (Potato)", 5.5, 4, 12, new String[]{"গজানো", "বাড়ন্ত", "কন্দ গঠন", "পরিপক্কতা"}, "ডায়মন্ড", "কার্ডিনাল");
        addCrop("ভুট্টা (Maize)", 5.0, 5, 10, new String[]{"গজানো", "বৃদ্ধি", "ফুল", "পরিপক্কতা"}, "বারি হাইব্রিড-9", "NK-40");
    }

    private void addCrop(String name, double water, int freq, int total, String[] stages, String... vars) {
        cropDataMap.put(name, new CropWaterRequirement(vars, stages, water, freq, total));
    }

    private void setupInputs() {
        cropComboBox.getItems().addAll(cropDataMap.keySet());
        cropComboBox.setOnAction(e -> updateCropDetails());

        farmingMethodComboBox.getItems().addAll("সমতল চাষ", "বেড প্ল্যান্টিং", "রিজ অ্যান্ড ফারো");
        unitComboBox.getItems().addAll("একর", "হেক্টর", "শতাংশ", "বিঘা");
        unitComboBox.setValue("একর");
        soilTypeComboBox.getItems().addAll("দো-আঁশ", "বেলে দো-আঁশ", "এঁটেল", "বেলে");
        landTypeComboBox.getItems().addAll("উঁচু", "মাঝারি", "নিম্ন");
        seasonComboBox.getItems().addAll("খরিফ-1", "খরিফ-2", "রবি", "গ্রীষ্ম", "বর্ষা", "শীত");

        waterSourceComboBox.getItems().addAll("গভীর নলকূপ", "অগভীর নলকূপ", "খাল/নদী", "পুকুর");
        pumpPowerComboBox.getItems().addAll("নেই", "2 এইচপি", "3 এইচপি", "4 এইচপি", "5 এইচপি", "10 এইচপি", "20 এইচপি");
        pumpPowerComboBox.setValue("নেই");
        pipeTypeComboBox.getItems().addAll("ফিতা পাইপ", "পিভিসি পাইপ", "জিআই পাইপ", "মাটির নালা");
        pipeLengthUnitComboBox.getItems().addAll("ফুট", "মিটার");
        pipeLengthUnitComboBox.setValue("ফুট");
    }

    private void updateCropDetails() {
        CropWaterRequirement data = cropDataMap.get(cropComboBox.getValue());
        if (data != null) {
            varietyComboBox.getItems().setAll(data.varieties);
            varietyComboBox.getSelectionModel().selectFirst();
            growthStageComboBox.getItems().setAll(data.stages);
            growthStageComboBox.getSelectionModel().selectFirst();
        }
    }

    private void setupActions() {
        calculateBtn.setOnAction(e -> calculate());
        resetBtn.setOnAction(e -> reset());
        recentRainCheck.setOnAction(e -> rainfallAmount.setDisable(!recentRainCheck.isSelected()));
    }

    private void calculate() {
        if (!validateInputs()) return;

        double area = Double.parseDouble(landAreaField.getText().trim());
        double landHa = convertAreaToHa(area, unitComboBox.getValue());

        CropWaterRequirement crop = cropDataMap.get(cropComboBox.getValue());
        double waterMM = calculateWaterRequirement(crop);

        double litersPerIrrigation = waterMM * landHa * 10000;
        double totalLiters = litersPerIrrigation * crop.totalIrrigations;

        double eff = calculateEfficiency();
        double actualPerIrrigation = litersPerIrrigation / (eff / 100.0);
        double actualTotal = totalLiters / (eff / 100.0);

        double flowRate = getPumpFlowRate(pumpPowerComboBox.getValue());
        double hoursPerIrrigation = (flowRate > 0) ? actualPerIrrigation / flowRate : 0;
        double[] costs = calculateCosts(actualTotal, area, crop.totalIrrigations);

        displayResults(actualPerIrrigation, hoursPerIrrigation, eff, costs, crop);
    }

    private double calculateWaterRequirement(CropWaterRequirement crop) {
        double base = crop.baseWaterMM;

        if (strongSunCheck.isSelected()) base *= 1.25;
        if (windyCheck.isSelected()) base *= 1.15;
        if (cloudyCheck.isSelected()) base *= 0.9;
        if (tempHot.isSelected()) base *= 1.3; else if (tempCold.isSelected()) base *= 0.8;

        String soil = soilTypeComboBox.getValue();
        if (soil != null && soil.contains("বেলে")) base *= 1.25;
        if (soil != null && soil.contains("এঁটেল")) base *= 0.85;

        if (recentRainCheck.isSelected()) {
            try {
                base -= Double.parseDouble(rainfallAmount.getText().trim()) * 0.7;
            } catch (Exception ignored) {
            }
        }

        int freq = crop.frequency + (rainForecastYes.isSelected() ? 2 : 0);
        return Math.max(0, base * freq);
    }

    private double calculateEfficiency() {
        double eff = methodDrip.isSelected() ? 90 : methodSprinkler.isSelected() ? 75 : methodFurrow.isSelected() ? 60 : 50;

        String pipe = pipeTypeComboBox.getValue();
        if (pipe != null) {
            if (pipe.contains("পিভিসি") || pipe.contains("ফিতা")) eff += 5;
            else if (pipe.contains("মাটির")) eff -= 10;
        }

        try {
            double len = Double.parseDouble(pipeLengthField.getText().trim());
            if ("ফুট".equals(pipeLengthUnitComboBox.getValue())) len *= 0.3048;
            eff -= (len / 100.0);
        } catch (Exception ignored) {
        }

        return Math.max(30, Math.min(95, eff));
    }

    private double[] calculateCosts(double liters, double area, int rounds) {
        double fuelCost = (liters / 1000.0) * (pumpPowerComboBox.getValue().contains("10") ? 3.0 : 2.5);
        double laborCost = 300 * area * rounds;
        return new double[]{fuelCost, laborCost, (fuelCost + laborCost) * 0.1};
    }

    private void displayResults(double water, double hours, double eff, double[] costs, CropWaterRequirement crop) {
        emptyResultState.setVisible(false);
        emptyResultState.setManaged(false);
        resultContentContainer.setVisible(true);
        resultContentContainer.setManaged(true);

        if (waterPerIrrigationLabel != null) waterPerIrrigationLabel.setText(formatVolume(water));
        if (timePerIrrigationLabel != null) timePerIrrigationLabel.setText(formatTime(hours));
        if (efficiencyLabel != null) efficiencyLabel.setText(String.format("%.1f%%", eff));

        if (fuelCostLabel != null) fuelCostLabel.setText("৳ " + df.format(costs[0]));
        if (laborCostLabel != null) laborCostLabel.setText("৳ " + df.format(costs[1]));
        if (maintenanceCostLabel != null) maintenanceCostLabel.setText("৳ " + df.format(costs[2]));
        if (totalCostLabel != null) totalCostLabel.setText("৳ " + df.format(costs[0] + costs[1] + costs[2]));

        renderSchedule(crop);
    }

    private void renderSchedule(CropWaterRequirement crop) {
        if (scheduleContainer == null) return;
        scheduleContainer.getChildren().clear();
        int[] milestones = {1, crop.totalIrrigations / 2, crop.totalIrrigations};
        String[] titles = {"প্রথম সেচ", "মাঝ পর্যায়", "শেষ সেচ"};

        for (int i = 0; i < milestones.length; i++) {
            if (milestones[i] <= 0) continue;
            HBox row = new HBox(15);
            row.getStyleClass().add("schedule-row");
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10));

            Label num = new Label(String.valueOf(milestones[i]));
            num.setStyle("-fx-background-color: #0f7aa7; -fx-text-fill: white; -fx-padding: 4 10; -fx-background-radius: 15;");

            VBox details = new VBox(2, new Label(titles[i]), new Label("দিন: " + (milestones[i] * crop.frequency)));
            row.getChildren().addAll(num, details);
            scheduleContainer.getChildren().add(row);
        }
    }

    private boolean validateInputs() {
        if (cropComboBox.getValue() == null || landAreaField.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "দয়া করে ফসল এবং জমির পরিমাণ দিন।").show();
            return false;
        }
        return true;
    }

    private void reset() {
        cropComboBox.setValue(null);
        landAreaField.clear();
        resultContentContainer.setVisible(false);
        resultContentContainer.setManaged(false);
        emptyResultState.setVisible(true);
        emptyResultState.setManaged(true);
    }

    private double convertAreaToHa(double val, String unit) {
        if ("হেক্টর".equals(unit)) return val;
        if ("শতাংশ".equals(unit)) return val * 0.004046;
        if ("বিঘা".equals(unit)) return val * 0.1338;
        return val * 0.4046;
    }

    private double getPumpFlowRate(String p) {
        if (p == null || p.contains("নেই")) return 0;
        if (p.contains("2")) return 15000;
        if (p.contains("5")) return 45000;
        if (p.contains("10")) return 85000;
        return 25000;
    }

    private String formatVolume(double l) {
        return l >= 1000000 ? df.format(l / 1000000) + " M লিটার" : df.format(l) + " লিটার";
    }

    private String formatTime(double h) {
        if (h <= 0) return "N/A";
        int hr = (int) h;
        int min = (int) ((h - hr) * 60);
        return hr > 0 ? hr + " ঘন্টা " + min + " মিনিট" : min + " মিনিট";
    }

    private static class CropWaterRequirement {
        String[] varieties, stages;
        double baseWaterMM;
        int frequency, totalIrrigations;

        CropWaterRequirement(String[] v, String[] s, double w, int f, int t) {
            varieties = v;
            stages = s;
            baseWaterMM = w;
            frequency = f;
            totalIrrigations = t;
        }
    }
}
