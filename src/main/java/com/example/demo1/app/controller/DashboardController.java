package com.example.demo1.app.controller;

import com.example.demo1.app.util.SessionManager;
import com.example.demo1.app.util.SceneLoader;
import com.example.demo1.app.util.UiAlerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.util.ResourceBundle;
import java.net.URL;

public class DashboardController implements Initializable {

    // ডিফল্ট ড্যাশবোর্ড ও ফিচার পেইজগুলোর FXML পাথ
    private static final String DASHBOARD_VIEW = "/fxml/dashboard/dashboard.fxml";
    private static final String PROFILE_VIEW = "/fxml/features/profile-view.fxml";
    private static final String EMERGENCY_VIEW = "/fxml/features/emergency-help.fxml";
    private static final String FEATURES_PREFIX = "/fxml/features/";

    @FXML private StackPane contentArea;
    @FXML private Button btnProfile;
    @FXML private Button btnEmergencyHelp;
    @FXML private Button btnGovtSchemes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // প্রথমে ড্যাশবোর্ড কনটেন্ট লোড করি
        loadContent(DASHBOARD_VIEW);

        // প্রয়োজনীয় বাটনগুলোর ক্লিক ইভেন্ট এখানে সেট করা হচ্ছে
        if (btnProfile != null) {
            btnProfile.setOnAction(this::handleProfileClick);
        }
        if (btnEmergencyHelp != null) {
            btnEmergencyHelp.setOnAction(event -> loadContent(EMERGENCY_VIEW));
        }
        if (btnGovtSchemes != null) {
            btnGovtSchemes.setOnAction(event -> setFeaturePage("govt-schemes.fxml"));
        }
    }

    @FXML
    private void handleProfileClick(ActionEvent event) {
        // প্রোফাইল পেইজে যাওয়ার আগে সেশন স্ট্যাটাস লগ করি
        String sessionStatus = SessionManager.getLoggedInUser() == null ? "NULL" : "ACTIVE";
        System.out.println("Profile navigation requested. Session: " + sessionStatus);
        loadContent(PROFILE_VIEW);
    }

    private void setFeaturePage(String fxmlFileName) {
        // features ফোল্ডারের নির্দিষ্ট পেইজ লোড করার শর্টকাট মেথড
        loadContent(FEATURES_PREFIX + fxmlFileName);
    }

    private void loadContent(String fxmlPath) {
        if (contentArea == null) {
            System.err.println("Dashboard content area is not connected in FXML.");
            return;
        }

        try {
            Parent content = SceneLoader.load(fxmlPath);
            contentArea.getChildren().setAll(content);
        } catch (Exception e) {
            System.err.println("Failed to load view: " + fxmlPath);
            e.printStackTrace();
            UiAlerts.error("????? ??????", "??? ??? ??? ??????");
        }
    }


}
