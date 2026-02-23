package com.example.demo1.app.controller;

import com.example.demo1.app.util.SceneLoader;
import com.example.demo1.app.util.SessionManager;
import com.example.demo1.app.util.UiAlerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

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
        loadContent(DASHBOARD_VIEW);

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
        String sessionStatus = SessionManager.getLoggedInUser() == null ? "NULL" : "ACTIVE";
        System.out.println("Profile navigation requested. Session: " + sessionStatus);
        loadContent(PROFILE_VIEW);
    }

    private void setFeaturePage(String fxmlFileName) {
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
            UiAlerts.error("Load Failed", "Could not open this page.");
        }
    }
}