package com.example.demo1.app.controller;
import com.example.demo1.app.util.NavigationHelper;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import com.example.demo1.app.util.UiAlerts;
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

//class declare kora hoiche and implements Initializeable mane holo page ti scene a load howar sathe sathe aer vitorer initialize method ta call hobe and tar vitorer code gulo execute hobe
public class CropRotationController implements Initializable {

    @FXML private Button btnHome, btnAdvisory, btnGuide, btnFertilizer, btnIrrigation, btnCropRotation, btnLocalManagement, btnStorage, btnMachinery,generateBtn, resetBtn;

    @FXML private ComboBox<String> districtComboBox, landTypeComboBox, soilTypeComboBox, currentSeasonComboBox, prevCropComboBox;

    @FXML private RadioButton irrigationYes,irrigationRain;
    @FXML private VBox resultsContainer, emptyState;

    //initialize method ta automatically call hobe jokhon page ti load hobe
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);
        NavigationHelper.setupAdvisoryNav(btnGuide, btnFertilizer, btnIrrigation, btnCropRotation);

        populateDropdowns();//dropdown gulo te option add kora hoiche
        generateBtn.setOnAction(e -> calculateRotation());//generate button e click korle calculateRotation method ta call hobe
        resetBtn.setOnAction(e -> resetForm());//reset button e click korle resetForm method ta call hobe
    }
    //dropdown gulo te option add kora method
    private void populateDropdowns() {
        districtComboBox.getItems().addAll("ঢাকা", "কুমিল্লা", "বগুড়া", "রাজশাহী", "রংপুর", "দিনাজপুর", "যশোর", "বরিশাল");
        landTypeComboBox.getItems().addAll("উঁচু জমি", "মাঝারি উঁচু জমি", "মাঝারি নিচু জমি", "নিচু জমি");
        soilTypeComboBox.getItems().addAll("দোআঁশ", "বেলে দোআঁশ", "এঁটেল দোআঁশ", "এঁটেল");
        currentSeasonComboBox.getItems().addAll("রবি (শীত)", "খরিফ-১ (গ্রীষ্ম)", "খরিফ-২ (বর্ষা)");
        prevCropComboBox.getItems().addAll("আমন ধান", "বোরো ধান", "গম", "ভুট্টা", "আলু", "সরিষা", "মসুর ডাল", "পাট", "সবজি");
    }
    // ata diye check korteche je user ki sob input diyeche kina, jodi na dey tahole warning dekhabe
    private void calculateRotation() {
        if (!validateInputs()) {
            return;
        }

        // user input gulo ke variable a store kora hoiche
        String land = landTypeComboBox.getValue();
        String soil = soilTypeComboBox.getValue();
        String season = currentSeasonComboBox.getValue();
        String prevCrop = prevCropComboBox.getValue();
        boolean hasIrrigation = irrigationYes.isSelected();

        resultsContainer.getChildren().clear();// previous results clear kora hoiche
        emptyState.setVisible(false);
        emptyState.setManaged(false);
        
        // user input er upor base kore different crop rotation model suggest kora hoiche
        if (prevCrop.contains("আমন") || season.contains("রবি")) {// jodi user er previous crop amon dhan hoy ba current season rabi hoy tahole ei model gulo suggest kora hobe
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

            if (land.contains("উঁচু")) {// jodi user er land type uchu hoy tahole aro ekta model suggest kora hobe
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
        } else if (prevCrop.contains("বোরো") || season.contains("খরিফ-১")) {// jodi user er previous crop boro dhan hoy ba current season kharif-1 hoy tahole ei model gulo suggest kora hobe
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
        } else {// jodi user er previous crop onno kichu hoy tahole ei model gulo suggest kora hobe
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

    // user input gulo check kora hoiche je sob input deya hoyeche kina, jodi na dey tahole warning dekhabe
    private boolean validateInputs() {
        if (landTypeComboBox.getValue() == null
                || soilTypeComboBox.getValue() == null
                || currentSeasonComboBox.getValue() == null
                || prevCropComboBox.getValue() == null) {
            UiAlerts.warning("Please fill all required fields", "দয়া করে সব বাধ্যতামূলক তথ্য পূরণ করুন।");
            return false;
        }
        return true;
    }
    
    // crop rotation model gulo ke card akare show korar jonno method ta create kora hoiche, jekhane title, badge, step gulo ke parameter hisebe neya hoiche
    private void addCard(String title, String badge, String badgeIcon, Step s1, Step s2, Step s3, String tip) {
        VBox card = new VBox(10);// card er vitorer element gulo modhye 10 pixel gap thakbe
        card.getStyleClass().add("rotation-card");// card er style class set kora hoiche

        Label titleLabel = new Label(title);
        titleLabel.getStyleClass().add("option-title");// title label er style class set kora hoiche

        Label badgeLabel = new Label(badgeIcon + " " + badge);
        badgeLabel.getStyleClass().add("option-badge");

        Region spacer = new Region();// spacer create kora hoiche jate title ar badge er modhye space thake
        HBox.setHgrow(spacer, Priority.ALWAYS);
        HBox header = new HBox(10, titleLabel, spacer, badgeLabel);// header create kora hoiche jekhane title ar badge ek line a thakbe ar modhye space thakbe

        // crop rotation cycle create kora hoiche jekhane step gulo ar modhye arrow thakbe
        HBox cycle = new HBox(8, createStep(s1), createArrow(), createStep(s2), createArrow(), createStep(s3));
        cycle.setAlignment(Pos.CENTER);
        cycle.getStyleClass().add("cycle-container");

        // tip section create kora hoiche jekhane user ke crop rotation er benefit gulo bola hobe
        HBox footer = new HBox(10, new Label("💡"), new Label(tip));
        footer.getStyleClass().add("benefit-box");
        ((Label) footer.getChildren().get(1)).setWrapText(true);

        card.getChildren().addAll(header, cycle, footer);
        resultsContainer.getChildren().add(card);// card ke results container a add kora hoiche jate user ke show kora jay
    }


    // step create kora hoiche jekhane crop er name, season ar icon show kora hobe
    private VBox createStep(Step s) {
        Label iconLabel = new Label(getCropIcon(s.name));// crop er name er upor base kore icon set kora hoiche
        Label cropLabel = new Label(s.name);// crop er name show kora hoiche
        cropLabel.getStyleClass().add("step-crop");
        cropLabel.setWrapText(true);// crop er name wrap kora hoiche jate long name gulo o properly show kore
        Label seasonLabel = new Label(s.season);// season show kora hoiche
        seasonLabel.getStyleClass().add("step-season");

        VBox box = new VBox(3, iconLabel, cropLabel, seasonLabel);// step er element gulo modhye 3 pixel gap thakbe
        box.setAlignment(Pos.CENTER);// step er element gulo center a align kora hoiche
        box.getStyleClass().add("cycle-step");
        box.setPrefWidth(96);
        return box;
    }

    // arrow create kora hoiche jekhane "➜" symbol use kora hoiche ar style class set kora hoiche
    private Label createArrow() {
        Label arrow = new Label("➜");
        arrow.getStyleClass().add("arrow-icon");
        return arrow;
    }

    // crop er name er upor base kore icon set kora hoiche, jodi name er modhye "ধান" thake tahole dhan er icon show hobe, jodi "আলু" ba "সবজি" thake tahole alu/sobji er icon show hobe.
    private String getCropIcon(String name) {
        if (name.contains("ধান")) return "🌾";
        if (name.contains("আলু") || name.contains("সবজি")) return "🥔";
        if (name.contains("ভুট্টা")) return "🌽";
        if (name.contains("পাট") || name.contains("ঢেঁইচা")) return "🌿";
        if (name.contains("সরিষা")) return "🌼";
        if (name.contains("ডাল")) return "🥜";
        return "🌱";
    }

    // reset method ta create kora hoiche jate user form er input gulo ke reset korte pare ar previous results gulo clear hoye jay
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

    // step class ta create kora hoiche jekhane crop er name ar season store kora hobe, jeta card create korar somoy use kora hobe
    private static class Step {
        private final String name;
        private final String season;

        Step(String name, String season) {
            this.name = name;
            this.season = season;
        }
    }
}
