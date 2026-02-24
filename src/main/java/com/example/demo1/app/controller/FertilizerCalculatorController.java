package com.example.demo1.app.controller;

import com.example.demo1.app.util.NavigationHelper;
import javafx.fxml.FXML;
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

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class FertilizerCalculatorController {
    // Sidebar & Navigation Buttons fxml er sathe link kora hocche
    @FXML private Button btnHome, btnAdvisory, btnLocalManagement, btnStorage, btnMachinery,btnGuide, btnFertilizer, btnIrrigation, btnCropRotation, calculateBtn, resetBtn;

    // egula dropdown input field,ja fxml er sathe link kora hocche
    @FXML private ComboBox<String> cropComboBox, varietyComboBox, seasonComboBox, unitComboBox, soilTypeComboBox, previousCropComboBox;

    // egula text field,radio button,checkbox input field,ja fxml er sathe link kora hocche
    @FXML private TextField landAreaField, organicManureAmount;
    @FXML private RadioButton nLow, nMedium, nHigh, pLow, pMedium, pHigh, kLow, kMedium, kHigh;
    @FXML private CheckBox organicManureCheck;

    // result  dekhanur jnoo variable,hisab korar por result show korbe
    @FXML private VBox emptyResultState, resultContentContainer,fertilizerResultsContainer, costBreakdownContainer, timelineContainer;
    @FXML private Label totalCostLabel, summaryCrop, summaryLand, summarySoil;

    private final Map<String, CropData> cropDatabase = new HashMap<>();// crop er data store korar jnoo map
    private final Map<String, Double> prices = new HashMap<>();// fertilizer er dam store korar jnoo map
    private final DecimalFormat df = new DecimalFormat("#,##0.00");// decimal format for price display

    @FXML
    public void initialize() {
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);
        NavigationHelper.setupAdvisoryNav(btnGuide, btnFertilizer, btnIrrigation, btnCropRotation);
        initData();// crop data and fertilizer prices initialize kora hocche nicher func er maddhome
        setupInputs();// dropdown input field setup kora hocche nicher func er maddhome
        setupActions();// button click and other input actions setup kora hocche nicher func er maddhome
    }

    private void initData() {
        addCrop("সরিষা (Mustard)", new String[]{"রবি"}, new double[]{90, 30, 40}, "BARI-14", "লোকাল");
        addCrop("ধান (Boro)", new String[]{"রবি"}, new double[]{140, 25, 60}, "BRRI-28", "হাইব্রিড");
        addCrop("গম (Wheat)", new String[]{"রবি"}, new double[]{110, 40, 50}, "BARI-30", "প্রদীপ");
        addCrop("আলু (Potato)", new String[]{"রবি"}, new double[]{150, 60, 120}, "ডায়মন্ড", "কার্ডিনাল");

        prices.put("Urea", 27.0);
        prices.put("TSP", 24.0);
        prices.put("MOP", 18.0);
        prices.put("Gypsum", 12.0);
        prices.put("Zinc", 180.0);
    }
    // crop er data add korar jnoo func, crop er name, season, npk requirement and variety gula parameter hisebe ney and map e store kore
    private void addCrop(String name, String[] seasons, double[] npk, String... vars) {
        cropDatabase.put(name, new CropData(vars, seasons, npk));
    }

    // dropdown input field setup korar jnoo func, crop, unit, soil type and previous crop er options set kore and crop select korle variety and season er options update kore
    private void setupInputs() {
        cropComboBox.getItems().addAll(cropDatabase.keySet());// crop name gulo dropdown e add kora hocche
        unitComboBox.getItems().addAll("একর", "হেক্টর", "শতাংশ", "বিঘা");
        unitComboBox.setValue("একর");
        soilTypeComboBox.getItems().addAll("দো-আঁশ", "বেলে দো-আঁশ", "এঁটেল", "পলি মাটি");//dropdown a add kora hocche
        previousCropComboBox.getItems().addAll("কিছু না", "ধান", "গম", "ডাল জাতীয়", "সবজি");//dropdown a add kora hocche

        
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
        calculateBtn.setOnAction(e -> calculate());// calculate button click korle calculate func call hobe
        resetBtn.setOnAction(e -> reset());// reset button click korle reset func call hobe
        organicManureCheck.setOnAction(e -> organicManureAmount.setDisable(!organicManureCheck.isSelected()));// gobor checkbox select korle amount input field enable hobe, na hole disable hobe

        //akhane niche 3 ta N,P,K er radio button er default value medium set kora hocche
        nMedium.setSelected(true);
        pMedium.setSelected(true);
        kMedium.setSelected(true);
    }

    private void calculate() {
        if (!validate()) return;//hisab shurur age check kore user sob input dise kina, jodi na dey tahole warning dekhabe and calculation korbe na

        try {
            double area = Double.parseDouble(landAreaField.getText().trim());
            // user input theke land area niye convertToHa func er maddhome hectare te convert kora hocche, karon hisab korar jnoo hectare use kora hobe
            double ha = convertToHa(area, unitComboBox.getValue());
            CropData data = cropDatabase.get(cropComboBox.getValue());
            double[] npk = data.npk.clone();// crop er npk requirement clone kore new array te rakha hocche, karon original data map e thakbe and hisab korar jnoo copy te change kora hobe

            // variety te hybrid select korle 20% beshi fertilizer lagbe, tai npk requirement 1.2 diye gun kora hocche
            if (varietyComboBox.getValue() != null && varietyComboBox.getValue().contains("হাইব্রিড")) {
                npk[0] *= 1.2;
                npk[1] *= 1.2;
                npk[2] *= 1.2;
            }
            adjustForSoil(npk);// soil type er radio button onujayi npk requirement adjust kora hocche
            adjustForManure(npk);// organic manure use korle npk requirement adjust kora hocche

            // hisab korar jnoo formula use kore fertilizer er amount calculate kora hocche, npk requirement ke hectare onujayi adjust kore fertilizer er kg ber kora hocche
            double urea = Math.max(0, npk[0] * 2.17 * ha);
            double tsp = Math.max(0, npk[1] * 2.17 * ha);
            double mop = Math.max(0, npk[2] * 1.67 * ha);
            double gyp = 60 * ha;
            double zinc = 8 * ha;

            // calculation er por result display korar jnoo func call kora hocche, area and fertilizer er amount pass kora hocche
            displayResults(area, urea, tsp, mop, gyp, zinc);
        } catch (NumberFormatException e) {
            UiAlerts.error("Please Enter Valid Input", "জমির পরিমাণ সঠিকভাবে লিখুন।");
        }
    }
    //mathite pusti beshi select kora hoiche
    private void adjustForSoil(double[] npk) {
        npk[0] = adjustNutrientLevel(npk[0], nLow.isSelected(), nHigh.isSelected());
        npk[1] = adjustNutrientLevel(npk[1], pLow.isSelected(), pHigh.isSelected());
        npk[2] = adjustNutrientLevel(npk[2], kLow.isSelected(), kHigh.isSelected());
    }
    //jodi mathite age thekei pusti thake thaole 40% kom sar deya lagbe,tai 0.6 diye gun kora hocche, ar jodi pusti kom thake thaole 25% beshi sar deya lagbe, tai 1.25 diye gun kora hocche, na hole original value return kora hocche
    private double adjustNutrientLevel(double baseValue, boolean lowSelected, boolean highSelected) {
        if (highSelected) return baseValue * 0.6;
        if (lowSelected) return baseValue * 1.25;
        return baseValue;
    }

    // organic manure use korle npk requirement adjust kora hocche, user input theke manure er amount niye npk requirement theke manure er contribution komano hocche, karon manure use korle fertilizer er amount kom lagbe
    private void adjustForManure(double[] npk) {
        if (!organicManureCheck.isSelected()) return;
        try {
            double ton = Double.parseDouble(organicManureAmount.getText().trim());
            npk[0] -= ton * 4;// 1 ton gobor use korle 4kg nitrogen kom lagbe 
            npk[1] -= ton * 1.5;// 1 ton gobor use korle 1.5kg phosphorus kom lagbe
            npk[2] -= ton * 4;// 1 ton gobor use korle 4kg potassium kom lagbe
        } catch (Exception ignored) {}
    }

    // hisab korar por result display korar jnoo func, fertilizer er amount, total cost and timeline show korbe, area and fertilizer er amount pass kora hocche
    private void displayResults(double area, double u, double t, double m, double g, double z) {
        summaryCrop.setText(cropComboBox.getValue());
        summaryLand.setText(area + " " + unitComboBox.getValue());
        summarySoil.setText(soilTypeComboBox.getValue());

        fertilizerResultsContainer.getChildren().clear();// fertilizer er amount show korar container clear kora hocche, karon notun hisab korle ager result gulo dekhabe na
        costBreakdownContainer.getChildren().clear();// cost breakdown show korar container clear kora hocche, karon notun hisab korle ager result gulo dekhabe na
        timelineContainer.getChildren().clear();// timeline show korar container clear kora hocche, karon notun hisab korle ager result gulo dekhabe na

        // fertilizer er amount show korar jnoo addCard func call kora hocche, fertilizer er name, amount, price and subtext pass kora hocche, and total cost calculate kora hocche
        double total = 0;
        total += addCard("ইউরিয়া (Urea)", u, prices.get("Urea"), "নাইট্রোজেন");
        total += addCard("টিএসপি (TSP)", t, prices.get("TSP"), "ফসফরাস");
        total += addCard("এমওপি (MoP)", m, prices.get("MOP"), "পটাশ");
        total += addCard("জিপসাম", g, prices.get("Gypsum"), "সালফার");
        total += addCard("জিংক", z, prices.get("Zinc"), "মাইক্রোনিউট্রিয়েন্ট");

        totalCostLabel.setText("৳ " + df.format(total));// total cost show korar label update kora hocche

        addTimeline("1", "জমি তৈরির শেষ চাষে TSP, MOP, জিপসাম ও জিংক প্রয়োগ করুন।");
        addTimeline("2", "চারা রোপণের ৭-১০ দিন পর ইউরিয়ার ১ম কিস্তি দিন।");
        addTimeline("3", "৩০ দিন পর ইউরিয়ার ২য় কিস্তি দিন।");

        emptyResultState.setVisible(false);// hisab korar por empty state hide kora hocche and result container show kora hocche
        emptyResultState.setManaged(false);
        resultContentContainer.setVisible(true);// result container show kora hocche
        resultContentContainer.setManaged(true);
    }

    // fertilizer er amount show korar card add korar jnoo func, fertilizer er name, amount, price and subtext pass kora hocche, and total cost calculate kora hocche, 
    private double addCard(String name, double kg, double price, String sub) {
        if (kg <= 0.1) return 0;//jodi amount 0.1 kg er kom hoy tahole card show korbe na, karon amount onek kom hole user ke dekhano mane hobe na

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
        return cost;// total cost calculate kora hocche, card add korar por cost breakdown e o show korbe and total cost er jnoo return kora hocche, jeta main func e add kora hobe
    }

    // hisab korar por timeline show korar jnoo func, step number and instruction text pass kora hocche, and timeline container e add kora hocche
    private void addTimeline(String num, String txt) {
        Label badge = new Label(num);
        badge.setStyle("-fx-background-color: #2e7d32; -fx-text-fill: white; -fx-padding: 2 8; -fx-background-radius: 10;");
        Label text = new Label(txt);
        text.setWrapText(true);
        timelineContainer.getChildren().add(new HBox(10, badge, text));
    }

    // user input validation korar jnoo func, check kore crop select kora hoyeche kina and land area input field blank na thakle true return kore, na hole warning dekhabe and false return kore
    private boolean validate() {
        if (cropComboBox.getValue() == null || landAreaField.getText().isBlank()) {
            UiAlerts.warning("Please Enter valid inputs", "প্রয়োজনীয় তথ্য পূরণ করুন।");
            return false;
        }
        return true;
    }

    // reset button click korle sob input field clear kore and result hide kore empty state show korar jnoo func
    private void reset() {
        landAreaField.clear();
        cropComboBox.setValue(null);
        resultContentContainer.setVisible(false);
        resultContentContainer.setManaged(false);
        emptyResultState.setVisible(true);
        emptyResultState.setManaged(true);
    }

    // user input theke land area niye convertToHa func er maddhome hectare te convert kora hocche, karon hisab korar jnoo hectare use kora hobe, unit onujayi conversion kora hocche
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
