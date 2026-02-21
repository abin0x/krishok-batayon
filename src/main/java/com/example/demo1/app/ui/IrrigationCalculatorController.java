package com.example.demo1.app.ui;

import com.example.demo1.app.util.NavigationHelper; // Ensure this exists
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.*;

public class IrrigationCalculatorController implements Initializable {

    // --- Navigation Buttons ---
    @FXML private Button btnHome, btnAdvisory, btnAiHelper, btnLocalManagement, btnStorage,btnMachinery;
    @FXML private Button btnGuide, btnFertilizer, btnIrrigation, btnCropRotation;

    // --- Inputs ---
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

    // --- Results ---
    @FXML private VBox emptyResultState, resultContentContainer, scheduleContainer;
    @FXML private Label waterPerIrrigationLabel, efficiencyLabel, timePerIrrigationLabel;
    @FXML private Label fuelCostLabel, laborCostLabel, maintenanceCostLabel, totalCostLabel;

    // --- Data ---
    private final Map<String, CropWaterRequirement> cropDataMap = new HashMap<>();
    private final DecimalFormat df = new DecimalFormat("#,##0.00");

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setupNavigation();
        initData();
        setupInputs();
        setupActions();
    }

    // ---------------------------------------------------------
    // 1. NAVIGATION
    // ---------------------------------------------------------
    private void setupNavigation() {
        // Sidebar
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement,btnMachinery);
        // à§¨. à¦‰à¦ªà¦°à§‡à¦° à¦•à§à¦¯à¦¾à¦Ÿà¦¾à¦—à¦°à¦¿ à¦¬à¦¾à¦Ÿà¦¨ à¦¸à§‡à¦Ÿà¦†à¦ª (à¦¨à¦¤à§à¦¨ à¦«à¦¾à¦‚à¦¶à¦¨ à¦¬à§à¦¯à¦¬à¦¹à¦¾à¦° à¦•à¦°à§‡)
        NavigationHelper.setupAdvisoryNav(btnGuide, btnFertilizer, btnIrrigation, btnCropRotation);
    }

    // ---------------------------------------------------------
    // 2. DATA INITIALIZATION
    // ---------------------------------------------------------
    private void initData() {
        addCrop("à¦§à¦¾à¦¨ (Boro)", 5.0, 3, 25, new String[]{"à¦šà¦¾à¦°à¦¾ à¦°à§‹à¦ªà¦£", "à¦•à§à¦¶à¦¿", "à¦¥à§‹à¦¡à¦¼", "à¦ªà¦¾à¦•à¦¾"}, "à¦¬à§à¦°à¦¿ à¦§à¦¾à¦¨-à§¨à§®", "à¦¬à§à¦°à¦¿ à¦§à¦¾à¦¨-à§¨à§¯");
        addCrop("à¦§à¦¾à¦¨ (Aman)", 4.5, 4, 20, new String[]{"à¦šà¦¾à¦°à¦¾ à¦°à§‹à¦ªà¦£", "à¦•à§à¦¶à¦¿", "à¦¥à§‹à¦¡à¦¼", "à¦ªà¦¾à¦•à¦¾"}, "à¦¬à§à¦°à¦¿ à¦§à¦¾à¦¨-à§ªà§¯", "à¦¬à§à¦°à¦¿ à¦§à¦¾à¦¨-à§­à§§");
        addCrop("à¦—à¦® (Wheat)", 4.0, 7, 5, new String[]{"à¦¬à¦ªà¦¨", "à¦•à§à¦¶à¦¿", "à¦¶à§€à¦·", "à¦¦à¦¾à¦¨à¦¾ à¦ªà§‚à¦°à§à¦£à¦¤à¦¾"}, "à¦¬à¦¾à¦°à¦¿ à¦—à¦®-à§©à§¦", "à¦ªà§à¦°à¦¦à§€à¦ª");
        addCrop("à¦†à¦²à§ (Potato)", 5.5, 4, 12, new String[]{"à¦—à¦œà¦¾à¦¨à§‹", "à¦¬à¦¾à¦¡à¦¼à¦¨à§à¦¤", "à¦•à¦¨à§à¦¦ à¦—à¦ à¦¨", "à¦ªà¦°à¦¿à¦ªà¦•à§à¦•à¦¤à¦¾"}, "à¦¡à¦¾à¦¯à¦¼à¦®à¦¨à§à¦Ÿ", "à¦•à¦¾à¦°à§à¦¡à¦¿à¦¨à¦¾à¦²");
        addCrop("à¦­à§à¦Ÿà§à¦Ÿà¦¾ (Maize)", 5.0, 5, 10, new String[]{"à¦—à¦œà¦¾à¦¨à§‹", "à¦¬à§ƒà¦¦à§à¦§à¦¿", "à¦«à§à¦²", "à¦ªà¦°à¦¿à¦ªà¦•à§à¦•à¦¤à¦¾"}, "à¦¬à¦¾à¦°à¦¿ à¦¹à¦¾à¦‡à¦¬à§à¦°à¦¿à¦¡-à§¯", "à¦à¦¨à¦•à§‡-à§ªà§¦");
        // Add more crops as needed...
    }

    private void addCrop(String name, double water, int freq, int total, String[] stages, String... vars) {
        cropDataMap.put(name, new CropWaterRequirement(vars, stages, water, freq, total));
    }

    private void setupInputs() {
        cropComboBox.getItems().addAll(cropDataMap.keySet());
        cropComboBox.setOnAction(e -> updateCropDetails());

        farmingMethodComboBox.getItems().addAll("à¦¸à¦®à¦¤à¦² à¦šà¦¾à¦·", "à¦¬à§‡à¦¡ à¦ªà§à¦²à¦¾à¦¨à§à¦Ÿà¦¿à¦‚", "à¦°à¦¿à¦œ à¦…à§à¦¯à¦¾à¦¨à§à¦¡ à¦«à¦¾à¦°à§‹");
        unitComboBox.getItems().addAll("à¦à¦•à¦°", "à¦¹à§‡à¦•à§à¦Ÿà¦°", "à¦¶à¦¤à¦¾à¦‚à¦¶", "à¦¬à¦¿à¦˜à¦¾"); unitComboBox.setValue("à¦à¦•à¦°");
        soilTypeComboBox.getItems().addAll("à¦¦à§‹à¦†à¦à¦¶", "à¦¬à§‡à¦²à§‡ à¦¦à§‹à¦†à¦à¦¶", "à¦à¦à¦Ÿà§‡à¦²", "à¦¬à§‡à¦²à§‡");
        seasonComboBox.getItems().addAll("à¦–à¦°à¦¿à¦«-à§§", "à¦–à¦°à¦¿à¦«-à§¨", "à¦°à¦¬à¦¿", "à¦—à§à¦°à§€à¦·à§à¦®", "à¦¬à¦°à§à¦·à¦¾", "à¦¶à§€à¦¤");
        pumpPowerComboBox.getItems().addAll("à¦¨à§‡à¦‡", "à§¨ à¦à¦‡à¦šà¦ªà¦¿", "à§© à¦à¦‡à¦šà¦ªà¦¿", "à§ª à¦à¦‡à¦šà¦ªà¦¿", "à§« à¦à¦‡à¦šà¦ªà¦¿", "à§§à§¦ à¦à¦‡à¦šà¦ªà¦¿", "à§¨à§¦ à¦à¦‡à¦šà¦ªà¦¿"); pumpPowerComboBox.setValue("à¦¨à§‡à¦‡");
        pipeTypeComboBox.getItems().addAll("à¦«à¦¿à¦¤à¦¾ à¦ªà¦¾à¦‡à¦ª", "à¦ªà¦¿à¦­à¦¿à¦¸à¦¿ à¦ªà¦¾à¦‡à¦ª", "à¦œà¦¿à¦†à¦‡ à¦ªà¦¾à¦‡à¦ª", "à¦®à¦¾à¦Ÿà¦¿à¦° à¦¨à¦¾à¦²à¦¾");
        pipeLengthUnitComboBox.getItems().addAll("à¦«à§à¦Ÿ", "à¦®à¦¿à¦Ÿà¦¾à¦°"); pipeLengthUnitComboBox.setValue("à¦«à§à¦Ÿ");
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

    // ---------------------------------------------------------
    // 3. CALCULATION LOGIC
    // ---------------------------------------------------------
    private void calculate() {
        if (!validateInputs()) return;

        double area = Double.parseDouble(landAreaField.getText().trim());
        double landHa = convertAreaToHa(area, unitComboBox.getValue());

        CropWaterRequirement crop = cropDataMap.get(cropComboBox.getValue());
        double waterMM = calculateWaterRequirement(crop);

        // Final Water Volume
        double litersPerIrrigation = waterMM * landHa * 10000;
        double totalLiters = litersPerIrrigation * crop.totalIrrigations;

        // Efficiency
        double eff = calculateEfficiency();
        double actualPerIrrigation = litersPerIrrigation / (eff / 100.0);
        double actualTotal = totalLiters / (eff / 100.0);

        // Time & Cost
        double flowRate = getPumpFlowRate(pumpPowerComboBox.getValue());
        double hoursPerIrrigation = (flowRate > 0) ? actualPerIrrigation / flowRate : 0;
        double[] costs = calculateCosts(actualTotal, area, crop.totalIrrigations);

        displayResults(actualPerIrrigation, hoursPerIrrigation, eff, costs, crop);
    }

    private double calculateWaterRequirement(CropWaterRequirement crop) {
        double base = crop.baseWaterMM;

        // Environmental Adjustments
        if (strongSunCheck.isSelected()) base *= 1.25;
        if (windyCheck.isSelected()) base *= 1.15;
        if (cloudyCheck.isSelected()) base *= 0.9;
        if (tempHot.isSelected()) base *= 1.3; else if (tempCold.isSelected()) base *= 0.8;

        // Soil & Land
        String soil = soilTypeComboBox.getValue();
        if (soil != null && soil.contains("à¦¬à§‡à¦²à§‡")) base *= 1.25;
        if (soil != null && soil.contains("à¦à¦à¦Ÿà§‡à¦²")) base *= 0.85;

        // Rainfall
        if (recentRainCheck.isSelected()) {
            try { base -= Double.parseDouble(rainfallAmount.getText().trim()) * 0.7; } catch (Exception ignored){}
        }

        // Frequency Adjustment
        int freq = crop.frequency + (rainForecastYes.isSelected() ? 2 : 0);
        return Math.max(0, base * freq);
    }

    private double calculateEfficiency() {
        double eff = methodDrip.isSelected() ? 90 : methodSprinkler.isSelected() ? 75 : methodFurrow.isSelected() ? 60 : 50;

        String pipe = pipeTypeComboBox.getValue();
        if (pipe != null) {
            if (pipe.contains("à¦ªà¦¿à¦­à¦¿à¦¸à¦¿") || pipe.contains("à¦«à¦¿à¦¤à¦¾")) eff += 5;
            else if (pipe.contains("à¦®à¦¾à¦Ÿà¦¿à¦°")) eff -= 10;
        }

        // Pipe length loss
        try {
            double len = Double.parseDouble(pipeLengthField.getText().trim());
            if ("à¦«à§à¦Ÿ".equals(pipeLengthUnitComboBox.getValue())) len *= 0.3048;
            eff -= (len / 100.0);
        } catch (Exception ignored) {}

        return Math.max(30, Math.min(95, eff));
    }

    private double[] calculateCosts(double liters, double area, int rounds) {
        double fuelCost = (liters / 1000.0) * (pumpPowerComboBox.getValue().contains("à§§à§¦") ? 3.0 : 2.5);
        double laborCost = 300 * area * rounds;
        return new double[]{fuelCost, laborCost, (fuelCost + laborCost) * 0.1};
    }

    // ---------------------------------------------------------
    // 4. UI HELPERS
    // ---------------------------------------------------------
    private void displayResults(double water, double hours, double eff, double[] costs, CropWaterRequirement crop) {
        emptyResultState.setVisible(false); emptyResultState.setManaged(false);
        resultContentContainer.setVisible(true); resultContentContainer.setManaged(true);

        if (waterPerIrrigationLabel != null) waterPerIrrigationLabel.setText(formatVolume(water));
        if (timePerIrrigationLabel != null) timePerIrrigationLabel.setText(formatTime(hours));
        if (efficiencyLabel != null) efficiencyLabel.setText(String.format("%.1f%%", eff));

        if (fuelCostLabel != null) fuelCostLabel.setText("à§³ " + df.format(costs[0]));
        if (laborCostLabel != null) laborCostLabel.setText("à§³ " + df.format(costs[1]));
        if (maintenanceCostLabel != null) maintenanceCostLabel.setText("à§³ " + df.format(costs[2]));
        if (totalCostLabel != null) totalCostLabel.setText("à§³ " + df.format(costs[0] + costs[1] + costs[2]));

        renderSchedule(crop);
    }

    private void renderSchedule(CropWaterRequirement crop) {
        scheduleContainer.getChildren().clear();
        int[] milestones = {1, crop.totalIrrigations / 2, crop.totalIrrigations};
        String[] titles = {"à¦ªà§à¦°à¦¥à¦® à¦¸à§‡à¦š", "à¦®à¦§à§à¦¯ à¦ªà¦°à§à¦¯à¦¾à¦¯à¦¼", "à¦¶à§‡à¦· à¦¸à§‡à¦š"};

        for (int i = 0; i < milestones.length; i++) {
            if (milestones[i] <= 0) continue;
            HBox row = new HBox(15);
            row.getStyleClass().add("schedule-row"); // Use CSS for styling
            row.setAlignment(Pos.CENTER_LEFT);
            row.setPadding(new Insets(10));

            Label num = new Label(String.valueOf(milestones[i]));
            num.setStyle("-fx-background-color: #0284c7; -fx-text-fill: white; -fx-padding: 5 10; -fx-background-radius: 15;");

            VBox details = new VBox(2, new Label(titles[i]), new Label("à¦¦à¦¿à¦¨: " + (milestones[i] * crop.frequency)));
            row.getChildren().addAll(num, details);
            scheduleContainer.getChildren().add(row);
        }
    }

    private boolean validateInputs() {
        if (cropComboBox.getValue() == null || landAreaField.getText().isEmpty()) {
            new Alert(Alert.AlertType.ERROR, "à¦¦à¦¯à¦¼à¦¾ à¦•à¦°à§‡ à¦«à¦¸à¦² à¦à¦¬à¦‚ à¦œà¦®à¦¿à¦° à¦ªà¦°à¦¿à¦®à¦¾à¦£ à¦¨à¦¿à¦°à§à¦¬à¦¾à¦šà¦¨ à¦•à¦°à§à¦¨").show();
            return false;
        }
        return true;
    }

    private void reset() {
        cropComboBox.setValue(null); landAreaField.clear();
        resultContentContainer.setVisible(false); emptyResultState.setVisible(true);
    }

    // --- Utilities ---
    private double convertAreaToHa(double val, String unit) {
        if ("à¦¹à§‡à¦•à§à¦Ÿà¦°".equals(unit)) return val;
        if ("à¦¶à¦¤à¦¾à¦‚à¦¶".equals(unit)) return val * 0.004046;
        if ("à¦¬à¦¿à¦˜à¦¾".equals(unit)) return val * 0.1338;
        return val * 0.4046; // Acre default
    }

    private double getPumpFlowRate(String p) {
        if (p == null || p.contains("à¦¨à§‡à¦‡")) return 0;
        if (p.contains("à§¨")) return 15000;
        if (p.contains("à§«")) return 45000;
        if (p.contains("à§§à§¦")) return 85000;
        return 25000; // Default
    }

    private String formatVolume(double l) {
        return l >= 1000000 ? df.format(l/1000000) + " M à¦²à¦¿à¦Ÿà¦¾à¦°" : df.format(l) + " à¦²à¦¿à¦Ÿà¦¾à¦°";
    }

    private String formatTime(double h) {
        if (h <= 0) return "N/A";
        int hr = (int) h;
        int min = (int) ((h - hr) * 60);
        return hr > 0 ? hr + " h " + min + " min" : min + " min";
    }

    // --- Model ---
    private static class CropWaterRequirement {
        String[] varieties, stages;
        double baseWaterMM;
        int frequency, totalIrrigations;
        CropWaterRequirement(String[] v, String[] s, double w, int f, int t) {
            varieties = v; stages = s; baseWaterMM = w; frequency = f; totalIrrigations = t;
        }
    }
}


