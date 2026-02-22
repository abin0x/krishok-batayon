package com.example.demo1.app.ui;

import com.example.demo1.app.util.UiAlerts;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class GovtSchemesController implements Initializable {

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Static emergency/help dashboard page
    }

    @FXML
    private void handleCallAgriInfo() {
        UiAlerts.info("কৃষি তথ্য সেবা", "হটলাইন: ১৬১২৩\nসময়: ২৪/৭");
    }

    @FXML
    private void handleCallWeather() {
        UiAlerts.info("আবহাওয়া তথ্য", "হটলাইন: ১০৯০\nসময়: ২৪/৭");
    }

    @FXML
    private void handleCallDisaster() {
        UiAlerts.info("দুর্যোগ ব্যবস্থাপনা", "হটলাইন: ১০৯০\nসময়: ২৪/৭");
    }

    @FXML
    private void handleCallExtension() {
        UiAlerts.info("কৃষি সম্প্রসারণ অধিদপ্তর", "ফোন: ০২-৯১৩৫০৮২\nসময়: রবি-বৃহঃ (অফিস সময়)");
    }

    @FXML
    private void handleOfficeSupport() {
        UiAlerts.info("কৃষি পুনর্বাসন সহায়তা", "যোগাযোগ: উপজেলা কৃষি অফিস");
    }

    @FXML
    private void handleInsuranceClaim() {
        UiAlerts.info("ফসল বীমা দাবি", "বীমা দাবির জন্য কল করুন: ১৬১২৩");
    }
}
