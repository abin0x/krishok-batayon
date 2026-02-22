package com.example.demo1.app.controller;

import com.example.demo1.app.util.UiAlerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class GovtSchemesController implements Initializable {

    private static final String CAT_ALL = "all";
    private static final String CAT_LOAN = "loan";
    private static final String CAT_INSURANCE = "insurance";
    private static final String CAT_SUBSIDY = "subsidy";
    private static final String CAT_GRANT = "grant";

    @FXML private TextField searchField;

    @FXML private Button btnCatAll;
    @FXML private Button btnCatLoan;
    @FXML private Button btnCatInsurance;
    @FXML private Button btnCatSubsidy;
    @FXML private Button btnCatGrant;

    @FXML private Label lblStatLoanValue;
    @FXML private Label lblStatInsuranceValue;
    @FXML private Label lblStatSubsidyValue;
    @FXML private Label lblStatGrantValue;

    @FXML private Label lblInsuranceTitle;
    @FXML private Label lblInsuranceSubtitle;
    @FXML private Label lblInsurancePrice;
    @FXML private Label lblLoanTitle;
    @FXML private Label lblLoanSubtitle;
    @FXML private Label lblLoanPrice;
    @FXML private Label lblSubsidyTitle;
    @FXML private Label lblSubsidySubtitle;
    @FXML private Label lblSubsidyPrice;
    @FXML private Label lblGrantTitle;
    @FXML private Label lblGrantSubtitle;
    @FXML private Label lblGrantPrice;


    @FXML private VBox cardInsurance;
    @FXML private VBox cardLoan;
    @FXML private VBox cardSubsidy;
    @FXML private VBox cardGrant;

    private final List<SchemeItem> schemes = new ArrayList<>();
    private String activeCategory = CAT_ALL;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        schemes.add(new SchemeItem(cardInsurance, CAT_INSURANCE,
                "ফসল বীমা সরকারি কৃষি বীমা প্রিমিয়াম ক্ষতি প্রাকৃতিক দুর্যোগ"));
        schemes.add(new SchemeItem(cardLoan, CAT_LOAN,
                "কৃষি ঋণ স্বল্প সুদ ব্যাংক"));
        schemes.add(new SchemeItem(cardSubsidy, CAT_SUBSIDY,
                "ভর্তুকি সার বীজ সেচ ডিজেল"));
        schemes.add(new SchemeItem(cardGrant, CAT_GRANT,
                "অনুদান ক্ষুদ্র কৃষক নারী কৃষি যন্ত্র"));

        if (searchField != null) {
            searchField.textProperty().addListener((obs, oldV, newV) -> applyFilters());
        }

        setCategory(CAT_ALL);
    }

    @FXML
    private void handleCatAll() {
        setCategory(CAT_ALL);
    }

    @FXML
    private void handleCatLoan() {
        setCategory(CAT_LOAN);
    }

    @FXML
    private void handleCatInsurance() {
        setCategory(CAT_INSURANCE);
    }

    @FXML
    private void handleCatSubsidy() {
        setCategory(CAT_SUBSIDY);
    }

    @FXML
    private void handleCatGrant() {
        setCategory(CAT_GRANT);
    }

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

    private void setCategory(String category) {
        this.activeCategory = category;
        updateCategoryButtonStyles();
        applyFilters();
    }

    private void updateCategoryButtonStyles() {
        setActiveStyle(btnCatAll, CAT_ALL.equals(activeCategory));
        setActiveStyle(btnCatLoan, CAT_LOAN.equals(activeCategory));
        setActiveStyle(btnCatInsurance, CAT_INSURANCE.equals(activeCategory));
        setActiveStyle(btnCatSubsidy, CAT_SUBSIDY.equals(activeCategory));
        setActiveStyle(btnCatGrant, CAT_GRANT.equals(activeCategory));
    }

    private void setActiveStyle(Button button, boolean active) {
        if (button == null) {
            return;
        }
        button.getStyleClass().remove("cat-btn-active");
        if (active) {
            button.getStyleClass().add("cat-btn-active");
        }
    }

    private void applyFilters() {
        String query = searchField == null ? "" : searchField.getText().toLowerCase(Locale.ROOT).trim();

        for (SchemeItem item : schemes) {
            if (item.card == null) {
                continue;
            }

            boolean categoryMatch = CAT_ALL.equals(activeCategory) || activeCategory.equals(item.category);
            boolean textMatch = query.isEmpty() || item.searchBlob.contains(query);
            boolean visible = categoryMatch && textMatch;

            item.card.setVisible(visible);
            item.card.setManaged(visible);
        }
    }

    private void updateCardContent() {
        setLabelText(lblInsuranceTitle, "??? ????");
        setLabelText(lblInsuranceSubtitle, "???????? ?????? ???? ?????????");
        setLabelText(lblInsurancePrice, "? ?,??? / ????");

        setLabelText(lblLoanTitle, "???? ??");
        setLabelText(lblLoanSubtitle, "???????? ???? ?????? (BKB)");
        setLabelText(lblLoanPrice, "? ??,??? - ? ????");

        setLabelText(lblSubsidyTitle, "??? ? ??? ???????");
        setLabelText(lblSubsidySubtitle, "?????? ??????? ????????");
        setLabelText(lblSubsidyPrice, "???????? ??%");

        setLabelText(lblGrantTitle, "???? ??????? ??????");
        setLabelText(lblGrantSubtitle, "??????? ??????? ???????");
        setLabelText(lblGrantPrice, "???????????");
    }

    private void updateStatCards() {
        setLabelText(lblStatLoanValue, countByCategory(CAT_LOAN));
        setLabelText(lblStatInsuranceValue, countByCategory(CAT_INSURANCE));
        setLabelText(lblStatSubsidyValue, countByCategory(CAT_SUBSIDY));
        setLabelText(lblStatGrantValue, countByCategory(CAT_GRANT));
    }

    private int countByCategory(String category) {
        int count = 0;
        for (SchemeItem item : schemes) {
            if (item.category.equals(category)) {
                count++;
            }
        }
        return count;
    }

    private void setLabelText(Label label, int value) {
        if (label != null) {
            label.setText(toBanglaDigits(value));
        }
    }

    private void setLabelText(Label label, String value) {
        if (label != null) {
            label.setText(value);
        }
    }

    private String toBanglaDigits(int value) {
        String input = String.valueOf(value);
        StringBuilder out = new StringBuilder(input.length());
        for (char ch : input.toCharArray()) {
            if (ch >= '0' && ch <= '9') {
                out.append((char) ('০' + (ch - '0')));
            } else {
                out.append(ch);
            }
        }
        return out.toString();
    }

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
