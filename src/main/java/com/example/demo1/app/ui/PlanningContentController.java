package com.example.demo1.app.ui;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.util.Map;

public class PlanningContentController {

    private static final int DEFAULT_VISIBLE_CARDS = 8;
    private static final int TOTAL_CARDS = 10;

    @FXML
    private VBox extraCardsContainer;

    @FXML
    private Button btnShowMore;

    private boolean expanded;

    private static final Map<String, String> CROP_DETAILS = Map.of(
            "rice", "ফসল: ধান (Rice)\n• রোপণ: এপ্রিল-জুন, নভেম্বর-ডিসেম্বর\n• সেচ: ৫-৭ দিন অন্তর\n• ফলন: ৪০-৫০ মন/একর",
            "wheat", "ফসল: গম (Wheat)\n• রোপণ: নভেম্বর-ডিসেম্বর\n• সেচ: ১২-১৫ দিন অন্তর\n• ফলন: ৩০-৩৫ মন/একর",
            "potato", "ফসল: আলু (Potato)\n• রোপণ: অক্টোবর-নভেম্বর\n• কন্দ হার: ৬০০-৮০০ কেজি/একর\n• ফলন: ১৫০-২০০ মন/একর",
            "jute", "ফসল: পাট (Jute)\n• রোপণ: মার্চ-এপ্রিল\n• বীজ: ২-৩ কেজি/একর\n• ফলন: ২৫-৩০ মন/একর",
            "maize", "ফসল: ভুট্টা (Maize)\n• রোপণ: ফেব্রুয়ারি-মার্চ\n• বীজ: ৮-১০ কেজি/একর\n• ফলন: ৬০-৭০ মন/একর",
            "mustard", "ফসল: সরিষা (Mustard)\n• রোপণ: অক্টোবর-নভেম্বর\n• বীজ: ২-৩ কেজি/একর\n• ফলন: ১২-১৫ মন/একর",
            "lentil", "ফসল: মসুর (Lentil)\n• রোপণ: নভেম্বর\n• বীজ: ১২-১৫ কেজি/একর\n• ফলন: ৮-১০ মন/একর",
            "chili", "ফসল: মরিচ (Chili)\n• রোপণ: বছরজুড়ে\n• দূরত্ব: ১৮-২৪ ইঞ্চি\n• ফলন: ৪০-৬০ মন/একর",
            "onion", "ফসল: পেঁয়াজ (Onion)\n• রোপণ: নভেম্বর-ডিসেম্বর\n• চারা: ৪-৬ সপ্তাহ পরে রোপণ\n• ফলন: ৮০-১০০ মন/একর",
            "tomato", "ফসল: টমেটো (Tomato)\n• রোপণ: অক্টোবর-ডিসেম্বর\n• সেচ: ৬-৮ দিন অন্তর\n• ফলন: ১২০-১৬০ মন/একর"
    );

    @FXML
    private void initialize() {
        boolean needsShowMore = TOTAL_CARDS > DEFAULT_VISIBLE_CARDS;
        btnShowMore.setVisible(needsShowMore);
        btnShowMore.setManaged(needsShowMore);
        setExtraCardsVisible(false);
    }

    @FXML
    private void toggleShowMore() {
        expanded = !expanded;
        setExtraCardsVisible(expanded);
    }

    private void setExtraCardsVisible(boolean visible) {
        extraCardsContainer.setVisible(visible);
        extraCardsContainer.setManaged(visible);
        btnShowMore.setText(visible ? "কম দেখুন" : "আরও দেখুন");
    }

    @FXML
    private void showCropDetails(ActionEvent event) {
        if (!(event.getSource() instanceof Button source)) {
            return;
        }

        String key = source.getUserData() == null ? "" : source.getUserData().toString();
        String details = CROP_DETAILS.getOrDefault(key, "এই ফসলের বিস্তারিত তথ্য শীঘ্রই যোগ করা হবে।");

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("ফসলের বিস্তারিত তথ্য");
        alert.setHeaderText("বিস্তারিত নির্দেশনা");
        alert.setContentText(details);
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(560);
        alert.showAndWait();
    }
}
