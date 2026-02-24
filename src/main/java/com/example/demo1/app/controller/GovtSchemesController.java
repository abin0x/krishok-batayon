package com.example.demo1.app.controller;
import com.example.demo1.app.util.UiAlerts;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GovtSchemesController {
    // Category constants,jate kore easily manage kora jai and typo error kom hoi,like CAT_LOAN mane loan
    private static final String CAT_ALL = "all";
    private static final String CAT_LOAN = "loan";
    private static final String CAT_INSURANCE = "insurance";
    private static final String CAT_SUBSIDY = "subsidy";
    private static final String CAT_GRANT = "grant";

    @FXML private TextField searchField;// Search field for filtering schemes
    @FXML private Button btnCatAll,btnCatLoan,btnCatInsurance,btnCatSubsidy,btnCatGrant;// Category buttons for filtering schemes
    @FXML private VBox cardInsurance, cardLoan, cardSubsidy, cardGrant;// Scheme cards to display scheme details

    private final List<SchemeItem> schemes = new ArrayList<>();
    private String activeCategory = CAT_ALL;//default category is all, mane shob show korbe

    @FXML//page ti load holei initialize method call hobe
    public void initialize() {
        schemes.add(new SchemeItem(cardInsurance, CAT_INSURANCE,
                "ফসল বীমা সরকারি কৃষি বীমা প্রিমিয়াম ক্ষতি প্রাকৃতিক দুর্যোগ"));
        schemes.add(new SchemeItem(cardLoan, CAT_LOAN,
                "কৃষি ঋণ স্বল্প সুদ ব্যাংক"));
        schemes.add(new SchemeItem(cardSubsidy, CAT_SUBSIDY,
                "ভর্তুকি সার বীজ সেচ ডিজেল"));
        schemes.add(new SchemeItem(cardGrant, CAT_GRANT,
                "অনুদান ক্ষুদ্র কৃষক নারী কৃষি যন্ত্র"));

        if (searchField != null) {// Search field is optional
            searchField.textProperty().addListener((obs, oldV, newV) -> applyFilters());// Search field e text change holei filter apply hobe, niche ai func define kora ache
        }

        setCategory(CAT_ALL);
    }

    @FXML
    private void handleCatAll() { setCategory(CAT_ALL); }

    @FXML
    private void handleCatLoan() { setCategory(CAT_LOAN); }

    @FXML
    private void handleCatInsurance() { setCategory(CAT_INSURANCE); }

    @FXML
    private void handleCatSubsidy() { setCategory(CAT_SUBSIDY); }

    @FXML
    private void handleCatGrant() { setCategory(CAT_GRANT); }


    //card er viotre "বিস্তারিত দেখুন" button click holei ai method gulo call hobe, and UiAlerts class er info method call kore alert box show korbe, jekhane scheme er details thakbe
    @FXML
    private void handleInsuranceDetails() {
        UiAlerts.info("ফসল বীমা", "প্রাকৃতিক দুর্যোগে ফসল ক্ষতির জন্য সরকারি বীমা সুরক্ষা।");
    }

    @FXML
    private void handleLoanDetails() {
        UiAlerts.info("কৃষি ঋণ", "স্বল্প সুদে মৌসুমি কৃষি ঋণ। প্রয়োজনীয় কাগজপত্রসহ আবেদন করুন।");
    }

    @FXML
    private void handleSubsidyDetails() {
        UiAlerts.info("ভর্তুকি সহায়তা", "সার, বীজ, সেচ ও ডিজেল ভর্তুকি সুবিধা প্রযোজ্য শর্তে প্রদান করা হয়।");
    }

    @FXML
    private void handleGrantDetails() {
        UiAlerts.info("কৃষি অনুদান", "ক্ষুদ্র ও প্রান্তিক কৃষকদের জন্য নির্দিষ্ট খাতে অনুদান সহায়তা।");
    }

    @FXML
    private void handleApplyGuide() {
        UiAlerts.info("আবেদন নির্দেশনা", "NID, কৃষি কার্ড, ছবি ও প্রয়োজনীয় ফরম নিয়ে উপজেলা কৃষি অফিসে যোগাযোগ করুন।");
    }

    //mane jekono category button click holei ai method call hobe, and oi category set hobe, button er style update hobe, and filter apply hobe
    private void setCategory(String category) {
        activeCategory = category;
        updateCategoryButtonStyles();// ata func call hobe ,ar ei func ta niche define kora ache
        applyFilters();
    }

    //atai sei func,jei ta color change ar kaj kore
    private void updateCategoryButtonStyles() {
        setActiveStyle(btnCatAll, CAT_ALL.equals(activeCategory));
        setActiveStyle(btnCatLoan, CAT_LOAN.equals(activeCategory));
        setActiveStyle(btnCatInsurance, CAT_INSURANCE.equals(activeCategory));
        setActiveStyle(btnCatSubsidy, CAT_SUBSIDY.equals(activeCategory));
        setActiveStyle(btnCatGrant, CAT_GRANT.equals(activeCategory));
    }

    private void setActiveStyle(Button button, boolean active) {
        if (button == null) return;
        button.getStyleClass().remove("cat-btn-active");
        if (active) button.getStyleClass().add("cat-btn-active");
    }

    
    private void applyFilters() {
        String query = searchField == null ? "" : searchField.getText().toLowerCase(Locale.ROOT).trim();// Search query ke lowercase e convert kore and trim kore rakha, jate search case-insensitive hoy and extra spaces remove hoy

        for (SchemeItem item : schemes) {//schemes list er prottek item er jonno check kora hobe
            if (item.card == null) continue;// If card is not defined, skip this item

            boolean categoryMatch = CAT_ALL.equals(activeCategory) || activeCategory.equals(item.category);// Category filter check, mane jodi active category all hoy tahole sob show korbe, na hole jei category select kora ache shei category er item gulo show korbe
            boolean textMatch = query.isEmpty() || item.searchBlob.contains(query);// jodi search query empty hoy tahole sob show korbe, na hole jei item er searchBlob e query ta ache shei item gulo show korbe
            boolean visible = categoryMatch && textMatch;// Item visible hobe jodi category match kore and text match kore

            item.card.setVisible(visible);
            item.card.setManaged(visible);
        }
    }

    // SchemeItem class ta ekta simple data holder class, jekhane scheme er card, category, and searchBlob thake. searchBlob ta ai scheme er related text gulo ke lowercase e rakhe, jate search filter easily apply kora jai
    private static class SchemeItem {
        final VBox card;
        final String category;
        final String searchBlob;

        SchemeItem(VBox card, String category, String searchBlob) {
            this.card = card;
            this.category = category;
            this.searchBlob = searchBlob.toLowerCase(Locale.ROOT);
        }
    }
}
