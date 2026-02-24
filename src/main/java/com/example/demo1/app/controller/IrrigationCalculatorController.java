package com.example.demo1.app.controller;

import com.example.demo1.app.util.NavigationHelper;
import com.example.demo1.app.util.UiAlerts;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class IrrigationCalculatorController {

    @FXML private Button btnHome, btnAdvisory, btnLocalManagement, btnStorage, btnMachinery,calculateBtn, resetBtn, btnGuide, btnFertilizer, btnIrrigation, btnCropRotation;

    // Input fields for users
    @FXML private ComboBox<String> cropComboBox,unitComboBox, soilTypeComboBox, pumpPowerComboBox, pipeTypeComboBox, pipeLengthUnitComboBox;
    @FXML private TextField landAreaField,pipeLengthField, rainfallAmount;
    @FXML private CheckBox strongSunCheck, windyCheck, cloudyCheck, recentRainCheck;
    @FXML private RadioButton tempCold, tempHot, rainForecastYes,methodSprinkler, methodDrip, methodFlood, methodFurrow;

    // Result display components for koto ltr pani,koto cost hobe, efficiency, time etc
    @FXML private VBox emptyResultState, resultContentContainer;
    @FXML private Label waterPerIrrigationLabel, efficiencyLabel, timePerIrrigationLabel,fuelCostLabel, totalCostLabel;

    // kun fosole koto pani lagbe, frequency, total irrigation etc data rakhbe
    private final Map<String, CropWaterRequirement> cropDataMap = new HashMap<>();
    private final DecimalFormat df = new DecimalFormat("#,##0.00");// 2 decimal place e format korar jonno

    @FXML// initialize method jeta FXML load howar por call hobe
    public void initialize() {
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);
        NavigationHelper.setupAdvisoryNav(btnGuide, btnFertilizer, btnIrrigation, btnCropRotation);
        initData();// fosoler data initialize korar jonno
        setupInputs();// input fields setup korar jonno
        setupActions();// button click er jonno action setup korar jonno
    }

    private void initData() {
    // Boro ধান (শীতকালীন উচ্চ পানি চাহিদা)
    addCrop("ধান (Boro)", 6.0, 4, 18);//weekly 4 irrigation, মোট 18 session
    // Aman ধান (বর্ষাকালীন ধান, বৃষ্টির ওপর নির্ভর)
    addCrop("ধান (Aman)", 4.5, 3, 12);// weekly 3 irrigation, মোট 12 session
    // গম (শীতকালীন ফসল)
    addCrop("গম (Wheat)", 4.0, 2, 10);//weekly 2 irrigation, মোট 10 session
    // আলু (Potato)
    addCrop("আলু (Potato)", 5.5, 3, 14);//weekly 3 irrigation, মোট 14 session
    // ভুট্টা (Maize)
    addCrop("ভুট্টা (Maize)", 5.0, 3, 12);//weekly 3 irrigation, মোট 12 session
}

// ekhane cropDataMap e crop er name, water requirement (mm), frequency (weekly irrigation), total irrigation session rakhbo
    private void addCrop(String name, double water, int freq, int total) {
        cropDataMap.put(name, new CropWaterRequirement(water, freq, total));
    }

    // input fields setup korar jonno, crop combo box e crop name add korbo, unit combo box e area unit add korbo, soil type, pump power, pipe type etc setup korbo
    private void setupInputs() {
        cropComboBox.getItems().addAll(cropDataMap.keySet());// crop combo box e crop name add korbo

        unitComboBox.getItems().addAll("একর", "হেক্টর", "শতাংশ", "বিঘা");// area unit add korbo
        unitComboBox.setValue("একর");// default value set korbo
        soilTypeComboBox.getItems().addAll("দো-আঁশ", "বেলে দো-আঁশ", "এঁটেল", "বেলে");

        pumpPowerComboBox.getItems().addAll("নেই", "2 এইচপি", "3 এইচপি", "4 এইচপি", "5 এইচপি", "10 এইচপি", "20 এইচপি");
        pumpPowerComboBox.setValue("নেই");
        pipeTypeComboBox.getItems().addAll("ফিতা পাইপ", "পিভিসি পাইপ", "জিআই পাইপ", "মাটির নালা");
        pipeLengthUnitComboBox.getItems().addAll("ফুট", "মিটার");
        pipeLengthUnitComboBox.setValue("ফুট");
    }

    //calculate button e calculate method call korbo, reset button e reset method call korbo, recent rain check box e rainfall amount field enable/disable korbo
    private void setupActions() {
        calculateBtn.setOnAction(e -> calculate());//niche calculate method e calculation logic ache
        resetBtn.setOnAction(e -> reset());// reset method e input fields clear kore result hide korbo
        recentRainCheck.setOnAction(e -> rainfallAmount.setDisable(!recentRainCheck.isSelected()));// recent rain check box select korle rainfall amount field enable hobe, deselect korle disable hobe
    }

    private void calculate() {
        if (!validateInputs()) return;// input validation korbo

        double area = Double.parseDouble(landAreaField.getText().trim());// user input theke area value nibo
        double landHa = convertAreaToHa(area, unitComboBox.getValue());// user input theke area value ke hectare e convert korbo

        CropWaterRequirement crop = cropDataMap.get(cropComboBox.getValue());// user selected crop er water requirement data nibo
        double waterMM = calculateWaterRequirement(crop);// crop er water requirement calculate korbo, niche calculateWaterRequirement method e logic ache

        double litersPerIrrigation = waterMM * landHa * 10000;// water requirement (mm) * area (ha) * 10000 (1 ha = 10000 m2) diye total liters per irrigation session calculate korbo
        double totalLiters = litersPerIrrigation * crop.totalIrrigations;// total liters for all irrigation sessions calculate korbo

        double eff = calculateEfficiency();//niche calculateEfficiency method e logic ache
        double actualPerIrrigation = litersPerIrrigation / (eff / 100.0);
        double actualTotal = totalLiters / (eff / 100.0);

        // pump flow rate nibo user selected pump power er basis e, niche getPumpFlowRate method e logic ache
        double flowRate = getPumpFlowRate(pumpPowerComboBox.getValue());
        double hoursPerIrrigation = (flowRate > 0) ? actualPerIrrigation / flowRate : 0;
        double fuelCost = calculateFuelCost(actualTotal);//niche calculateFuelCost method e logic ache

        displayResults(actualPerIrrigation, hoursPerIrrigation, eff, fuelCost);
    }

    // crop er base water requirement calculate korar jonno, weather condition, soil type, recent rain etc factor consider kore water requirement adjust korbo
    private double calculateWaterRequirement(CropWaterRequirement crop) {
        double base = crop.baseWaterMM;

        if (strongSunCheck.isSelected()) base *= 1.25;//kora rod a 25% beshi pani lagbe, tai base ke 1.25 diye gun korbo
        if (windyCheck.isSelected()) base *= 1.15;// batahs a 15% beshi pani lagbe, tai base ke 1.15 diye gun korbo
        if (cloudyCheck.isSelected()) base *= 0.9;// meghla din a 10% kom pani lagbe, tai base ke 0.9 diye gun korbo
        if (tempHot.isSelected()) base *= 1.3; else if (tempCold.isSelected()) base *= 0.8;// gorom din a 30% beshi pani lagbe, thanda din a 20% kom pani lagbe, tai base ke 1.3 diye gun korbo gorom din er jonno, 0.8 diye gun korbo thanda din er jonno


        //matir dhoron onujayi panir hisab kora hocche
        String soil = soilTypeComboBox.getValue();
        if (soil != null && soil.contains("বেলে")) base *= 1.25;// bele matir jonno 25% beshi pani lagbe, tai base ke 1.25 diye gun korbo
        if (soil != null && soil.contains("এঁটেল")) base *= 0.85;// etel matir jonno 15% kom pani lagbe, tai base ke 0.85 diye gun korbo

        if (recentRainCheck.isSelected()) {
            try {
                base -= Double.parseDouble(rainfallAmount.getText().trim()) * 0.7;// recent rain hole er jonno 70% pani lagbe, tai rainfall amount ke 0.7 diye gun kore base theke minus korbo
            } catch (Exception ignored) {
            }
        }

        
        int freq = crop.frequency + (rainForecastYes.isSelected() ? 2 : 0);// rain forecast hole weekly irrigation frequency 2 diye barabe, tai crop er base frequency te 2 add korbo jodi rain forecast yes select kora thake
        return Math.max(0, base * freq);
    }


    //pipe type, pipe length etc factor consider kore efficiency calculate korbo
    private double calculateEfficiency() {
        double eff = methodDrip.isSelected() ? 90 : methodSprinkler.isSelected() ? 75 : methodFurrow.isSelected() ? 60 : 50;// drip irrigation er efficiency 90%, sprinkler irrigation er efficiency 75%, furrow irrigation er efficiency 60%, flood irrigation er efficiency 50% dhore neya hocche

        String pipe = pipeTypeComboBox.getValue();
        if (pipe != null) {
            if (pipe.contains("পিভিসি") || pipe.contains("ফিতা")) eff += 5;// PVC or fita pipe hole efficiency 5% barabe, tai eff e 5 add korbo
            else if (pipe.contains("মাটির")) eff -= 10;// matir nala hole efficiency 10% komabe, tai eff theke 10 minus korbo
        }


        // pipe length onujayi efficiency adjust korbo, jodi pipe length beshi hoy tahole efficiency kombe, karon pani pipe er modhye jete jete energy loss hobe, tai pipe length ke 100 diye vag kore eff theke minus korbo
        try {
            double len = Double.parseDouble(pipeLengthField.getText().trim());
            if ("ফুট".equals(pipeLengthUnitComboBox.getValue())) len *= 0.3048;//foot to meter convert
            eff -= (len / 100.0);// pipe length ke 100 diye vag kore eff theke minus korbo
        } catch (Exception ignored) {
        }

        return Math.max(30, Math.min(95, eff));// efficiency ke 30% theke 95% er moddhe limit kore dichi, karon beshi kom efficiency hole calculation er mane thakbe na, ar beshi beshi efficiency holeo real life e possible na
    }

    //total liters onujayi fuel cost calculate korbo, dhore neya hocche 1000 liter pani irrigate korar jonno 2.5 taka current lagbe jodi pump power 10 HP er niche hoy, ar 3 taka fuel lagbe jodi pump power 10 HP ba tar beshi hoy
    private double calculateFuelCost(double liters) {
        return (liters / 1000.0) * (pumpPowerComboBox.getValue().contains("10") ? 3.0 : 2.5);
    }

    // calculation er por result display korar jonno, water requirement, time, efficiency, fuel cost etc result label e set korbo, ar result container ke visible kore debo
    private void displayResults(double water, double hours, double eff, double fuelCost) {
        toggleResultState(true);

        waterPerIrrigationLabel.setText(formatVolume(water));
        timePerIrrigationLabel.setText(formatTime(hours));
        efficiencyLabel.setText(String.format("%.1f%%", eff));

        fuelCostLabel.setText("৳ " + df.format(fuelCost));
        totalCostLabel.setText("৳ " + df.format(fuelCost));
    }

    // input validation korar jonno, check korbo crop select kora ache kina, land area input kora ache kina, ar jodi recent rain check kora thake tahole rainfall amount input kora ache kina, jodi kono input missing thake tahole error alert show korbo
    private boolean validateInputs() {
        if (cropComboBox.getValue() == null || landAreaField.getText().isEmpty()) {
            UiAlerts.error("তথ্য ভুল", "দয়া করে ফসল এবং জমির পরিমাণ দিন।");
            return false;
        }
        return true;
    }


    // reset method e input fields clear kore
    private void reset() {
        cropComboBox.setValue(null);
        landAreaField.clear();
        toggleResultState(false);
    }


    // result state toggle korar jonno, jodi showResults true hoy tahole result container visible korbo ar empty state hide korbo, ar jodi showResults false hoy tahole result container hide korbo ar empty state visible korbo
    private void toggleResultState(boolean showResults) {
        resultContentContainer.setVisible(showResults);
        resultContentContainer.setManaged(showResults);
        emptyResultState.setVisible(!showResults);
        emptyResultState.setManaged(!showResults);
    }


    //convert area to hectare
    private double convertAreaToHa(double val, String unit) {
        if ("হেক্টর".equals(unit)) return val;// hectare hole value ke change korar dorkar nei
         if ("একর".equals(unit)) return val * 0.4046;// acre hole 0.4046 diye gun korbo
        if ("শতাংশ".equals(unit)) return val * 0.004046;// percentage hole 0.004046 diye gun korbo
        if ("বিঘা".equals(unit)) return val * 0.1338;// bigha hole 0.1338 diye gun korbo
        return val * 0.4046;// default hisebe acre dhore neya hocche
    }


    
    private double getPumpFlowRate(String p) {
        if (p == null || p.contains("নেই")) return 0;// jodi pump na thake tahole flow rate 0 hobe
        if (p.contains("2")) return 15000;// 2 HP pump er flow rate 15000 liter per hour 
        if (p.contains("3")) return 25000;// 3 HP pump er flow rate 25000 liter per hour 
        if (p.contains("5")) return 45000;// 5 HP pump er flow rate 45000 liter per hour 
        if (p.contains("10")) return 85000;// 10 HP pump er flow rate 85000 liter per hour 
        if (p.contains("20")) return 150000;// 20 HP pump er flow rate 150000 liter per hour 
        return 25000;// default flow rate 25000 liter per hour dhore neya hocche
    }


    // volume ke format korar jonno, jodi volume 1 million liter er beshi hoy tahole M liter e format korbo, ar jodi kom hoy tahole normal liter e format korbo
    private String formatVolume(double l) {
        return l >= 1000000 ? df.format(l / 1000000) + " M লিটার" : df.format(l) + " লিটার";
    }

    // time ke format korar jonno, jodi time 1 hour er beshi hoy tahole hour ar minute e format korbo, ar jodi kom hoy tahole minute e format korbo
    private String formatTime(double h) {
        if (h <= 0) return "N/A";
        int hr = (int) h;
        int min = (int) ((h - hr) * 60);
        return hr > 0 ? hr + " ঘন্টা " + min + " মিনিট" : min + " মিনিট";
    }

    private static class CropWaterRequirement {
        double baseWaterMM;
        int frequency, totalIrrigations;

        CropWaterRequirement(double w, int f, int t) {
            baseWaterMM = w;
            frequency = f;
            totalIrrigations = t;
        }
    }
}
