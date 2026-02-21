package com.example.demo1.app.ui.dashboard;

import com.example.demo1.app.util.NavigationHelper;
import com.example.demo1.app.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader; // ðŸ›‘ à¦¨à¦¤à§à¦¨ à¦†à¦®à¦¦à¦¾à¦¨à¦¿ ðŸ›‘
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane; // ðŸ›‘ à¦¨à¦¤à§à¦¨ à¦†à¦®à¦¦à¦¾à¦¨à¦¿ ðŸ›‘
import javafx.scene.Parent; // ðŸ›‘ à¦¨à¦¤à§à¦¨ à¦†à¦®à¦¦à¦¾à¦¨à¦¿ ðŸ›‘

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;
import java.io.IOException;

public class DashboardController implements Initializable {

    public Button btnGovtSchemes;
    // --- Core Navigation Buttons ---
    @FXML private Button btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery, btnProfile;

    // --- All Other Feature Buttons ---
    @FXML private Button btnAiHelper, btnVideoEducation, btnFarmWeather, btnAgriAnalysis;
    @FXML private Button btnAgriNews, btnCropPlanning, btnProfitLoss, btnWeather, btnMarket, btnFarmerMarket, btnSoilHealth, btnPestDetection, btnComments, btnWamService, btnNewsTraffic, btnEmergencyHelp, btnCommunity, btnStudy;

    // ðŸ›‘ à¦«à¦¿à¦•à§à¦¸: FXML à¦¥à§‡à¦•à§‡ à¦²à§‹à¦¡ à¦¹à¦“à¦¯à¦¼à¦¾ à¦•à¦¨à§à¦Ÿà§‡à¦¨à§à¦Ÿ à¦à¦°à¦¿à¦¯à¦¼à¦¾ ðŸ›‘
    @FXML private StackPane contentArea;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("âœ… Dashboard Initialized");

        // 1. Setup Main Sidebar Navigation
        // à¦†à¦ªà¦¨à¦¿ à¦šà¦¾à¦‡à¦²à§‡ à¦à¦‡ à¦¸à§‡à¦Ÿà¦†à¦ªà§‡à¦° à¦­à§‡à¦¤à¦°à§‡à¦‡ loadContent à¦•à¦² à¦•à¦°à¦¤à§‡ à¦ªà¦¾à¦°à§‡à¦¨à¥¤
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);

        // ðŸ›‘ à¦«à¦¿à¦•à§à¦¸ à§§: initialize-à¦ à¦ªà§à¦°à¦¥à¦® à¦•à¦¨à§à¦Ÿà§‡à¦¨à§à¦Ÿ (à¦¹à§‹à¦®) à¦²à§‹à¦¡ à¦•à¦°à§à¦¨ ðŸ›‘
        // à¦à¦‡ à¦¹à§‹à¦® FXML à¦Ÿà¦¿à¦‡ à¦†à¦ªà¦¨à¦¾à¦° à¦ªà§à¦°à¦¨à§‹ à¦¡à§à¦¯à¦¾à¦¶à¦¬à§‹à¦°à§à¦¡ à¦•à¦¨à§à¦Ÿà§‡à¦¨à§à¦Ÿ à¦§à¦¾à¦°à¦£ à¦•à¦°à¦¬à§‡à¥¤
        loadContent("/com/example/demo1/app/fxml/dashboard/dashboard.fxml");

        // 2. Setup Profile Navigation
        // à¦¯à§‡à¦¹à§‡à¦¤à§ à¦†à¦ªà¦¨à¦¿ FXML-à¦ onAction="#handleProfileClick" à¦¸à§‡à¦Ÿ à¦•à¦°à§‡à¦›à§‡à¦¨, à¦¤à¦¾à¦‡ à¦à¦‡ à¦²à¦œà¦¿à¦•à¦Ÿà¦¿ (setOnAction) à¦…à¦ªà§à¦°à¦¯à¦¼à§‹à¦œà¦¨à§€à¦¯à¦¼, à¦¤à¦¬à§‡ à¦à¦Ÿà¦¿ à¦¥à¦¾à¦•à¦²à§‡à¦“ à¦¸à¦¾à¦§à¦¾à¦°à¦£à¦¤ à¦¸à¦®à¦¸à§à¦¯à¦¾ à¦¹à¦¯à¦¼ à¦¨à¦¾à¥¤
        if (btnProfile != null) {
            btnProfile.setOnAction(this::handleProfileClick);
        }
        if (btnEmergencyHelp != null) {
            btnEmergencyHelp.setOnAction(event -> {
                System.out.println("DEBUG: Loading Emergency Help view...");
                loadContent("/com/example/demo1/app/fxml/features/emergency-help.fxml");
            });
        }

        // 3. Setup Placeholder Features (à¦¬à¦¾à¦•à¦¿ à¦¬à¦¾à¦Ÿà¦¨à¦—à§à¦²à§‹à¦“ loadContent à¦¬à§à¦¯à¦¬à¦¹à¦¾à¦° à¦•à¦°à¦¤à§‡ à¦ªà¦¾à¦°à§‡)
        setupPlaceholder(btnAiHelper, "AI à¦¸à¦¹à¦¾à¦¯à¦¼à¦•");
        setupPlaceholder(btnVideoEducation, "à¦­à¦¿à¦¡à¦¿à¦“ à¦¶à¦¿à¦•à§à¦·à¦¾");
        // ... à¦…à¦¨à§à¦¯à¦¾à¦¨à§à¦¯ à¦¬à¦¾à¦Ÿà¦¨ ...
        btnGovtSchemes.setOnAction(event -> {
            setPage("govt-schemes.fxml"); // à¦†à¦ªà¦¨à¦¾à¦° FXML à¦«à¦¾à¦‡à¦²à§‡à¦° à¦¨à¦¾à¦®
        });


    }

    // ðŸ›‘ à¦¨à¦¤à§à¦¨ à¦®à§‡à¦¥à¦¡: StackPane à¦ FXML à¦²à§‹à¦¡ à¦•à¦°à¦¾à¦° à¦œà¦¨à§à¦¯ ðŸ›‘
    private void loadContent(String fxmlPath) {
        if (contentArea == null) {
            System.err.println("âŒ ERROR: contentArea StackPane is NULL. FXML might not be correctly linked.");
            return;
        }
        try {
            // FXML à¦²à§‹à¦¡ à¦•à¦°à§à¦¨
            Parent content = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));

            // contentArea à¦à¦° à¦­à§‡à¦¤à¦°à§‡à¦° à¦ªà§à¦°à¦¾à¦¤à¦¨ à¦•à¦¨à§à¦Ÿà§‡à¦¨à§à¦Ÿ à¦¸à¦°à¦¿à¦¯à¦¼à§‡ à¦¨à¦¤à§à¦¨ FXML à¦¯à§à¦•à§à¦¤ à¦•à¦°à§à¦¨
            contentArea.getChildren().setAll(content);

            System.out.println("Page loaded successfully into contentArea: " + fxmlPath);

        } catch (IOException e) {
            System.err.println("Failed to load FXML content: " + fxmlPath + e.getMessage());
            showErrorAlert("à¦ªà§‡à¦œ à¦²à§‹à¦¡ à¦•à¦°à¦¾ à¦¯à¦¾à¦¯à¦¼à¦¨à¦¿: " + fxmlPath);
        } catch (Exception e) {
            System.err.println("General error loading content: " + e.getMessage());
        }
    }
    private void setPage(String fxmlFile) {
        try {
            // à¦†à¦ªà¦¨à¦¾à¦° FXML à¦«à¦¾à¦‡à¦²à§‡à¦° à¦ªà¦¾à¦¥ à¦…à¦¨à§à¦¯à¦¾à§Ÿà§€ à¦¨à¦¿à¦šà§‡à¦° à¦²à¦¾à¦‡à¦¨à¦Ÿà¦¿ à¦ªà¦°à¦¿à¦¬à¦°à§à¦¤à¦¨ à¦•à¦°à§à¦¨
            Parent fxml = FXMLLoader.load(getClass().getResource("/com/example/demo1/app/fxml/" + fxmlFile));
            contentArea.getChildren().clear(); // à¦†à¦—à§‡à¦° à¦•à¦¨à§à¦Ÿà§‡à¦¨à§à¦Ÿ à¦®à§à¦›à§‡ à¦«à§‡à¦²à¦¾
            contentArea.getChildren().add(fxml); // à¦¨à¦¤à§à¦¨ à¦•à¦¨à§à¦Ÿà§‡à¦¨à§à¦Ÿ à¦¯à§‹à¦— à¦•à¦°à¦¾
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading page: " + fxmlFile);
        }
    }

    // ðŸ›‘ à¦«à¦¿à¦•à§à¦¸ à§¨: Profile à¦•à§à¦²à¦¿à¦• à¦¹à§à¦¯à¦¾à¦¨à§à¦¡à¦²à¦¾à¦° à¦ªà¦°à¦¿à¦¬à¦°à§à¦¤à¦¨ à¦•à¦°à§à¦¨ ðŸ›‘
    @FXML
    private void handleProfileClick(ActionEvent event) {
        System.out.println("DEBUG: Attempting to load Profile into contentArea. Session status: " + (SessionManager.getLoggedInUser() != null ? "Active" : "NULL"));

        // âŒ NavigationHelper.navigateTo à¦•à¦² à¦•à¦°à¦¬à§‡à¦¨ à¦¨à¦¾, à¦•à¦¾à¦°à¦£ à¦à¦Ÿà¦¿ à¦ªà§à¦°à§‹ Scene à¦ªà¦°à¦¿à¦¬à¦°à§à¦¤à¦¨ à¦•à¦°à§‡!
        // âŒ à¦à¦° à¦ªà¦°à¦¿à¦¬à¦°à§à¦¤à§‡ loadContent à¦®à§‡à¦¥à¦¡ à¦•à¦² à¦•à¦°à§à¦¨à¥¤

        loadContent("/com/example/demo1/app/fxml/features/profile-view.fxml");

        // à¦¹à§‹à¦® à¦¬à¦¾à¦Ÿà¦¨à§‡ à¦•à§à¦²à¦¿à¦• à¦¹à¦²à§‡, à¦†à¦ªà¦¨à¦¿ à¦šà¦¾à¦‡à¦²à§‡ à¦à¦Ÿà¦¿à¦“ à¦¯à§‹à¦— à¦•à¦°à¦¤à§‡ à¦ªà¦¾à¦°à§‡à¦¨:
        // if (event.getSource() == btnHome) {
        //    loadContent("/com/example/demo1/app/fxml/dashboard/dashboard.fxml");
        // }
    }


    // --- Helper for Coming Soon Buttons ---
    private void setupPlaceholder(Button btn, String featureName) {
        if (btn != null) {
            btn.setOnAction(e -> showComingSoon(featureName));
        }
    }

    private void showComingSoon(String title) {
        System.out.println(title + " clicked.");
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Coming Soon");
        alert.setHeaderText(null);
        alert.setContentText(title + " feature is currently under development.");
        alert.show();
    }

    private void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("à¦²à§‹à¦¡à¦¿à¦‚ à¦¤à§à¦°à§à¦Ÿà¦¿");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}



