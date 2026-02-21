package com.example.demo1.app.ui;

import com.example.demo1.app.util.UiAlerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class GovtSchemesController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        System.out.println("Govt Schemes page loaded.");
    }

    @FXML
    private void handleABBankInfo() {
        showDetails("AB Bank স্মার্ট কার্ড লোন / Smart Card Loan",
                "প্রান্তিক ও ভূমিহীন কৃষকদের জন্য কৃষি কার্ডভিত্তিক সহায়তা। Suitable for small and landless farmers.");
    }

    @FXML
    private void handleBankAsiaInfo() {
        showDetails("Bank Asia 4% সুদে ঋণ / 4% Interest Loan",
                "ডাল, তেলবীজ ও মসলা চাষের জন্য স্বল্পসুদে ঋণ সুবিধা। Low-interest support for selected crops.");
    }

    @FXML
    private void handleDhakaBankInfo() {
        showDetails("Dhaka Bank সুফলা লোন / Sufola Loan",
                "ক্ষুদ্র অঙ্কের দ্রুত ঋণ সুবিধা। Fast micro-loan support for urgent farm activities.");
    }

    @FXML
    private void handleCityBankInfo() {
        showDetails("City Bank ডিজিটাল লোন / Digital Loan",
                "ডিজিটাল আবেদন ও দ্রুত অনুমোদনভিত্তিক কৃষি ঋণ। Digital-first loan flow with faster processing.");
    }

    private void showDetails(String title, String content) {
        UiAlerts.info(title, content);
    }
}
