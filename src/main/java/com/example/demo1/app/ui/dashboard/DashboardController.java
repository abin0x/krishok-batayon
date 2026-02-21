package com.example.demo1.app.ui.dashboard;

import com.example.demo1.app.util.NavigationHelper;
import com.example.demo1.app.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.fxml.FXMLLoader; // Ã°Å¸â€ºâ€˜ Ã Â¦Â¨Ã Â¦Â¤Ã Â§ÂÃ Â¦Â¨ Ã Â¦â€ Ã Â¦Â®Ã Â¦Â¦Ã Â¦Â¾Ã Â¦Â¨Ã Â¦Â¿ Ã°Å¸â€ºâ€˜
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane; // Ã°Å¸â€ºâ€˜ Ã Â¦Â¨Ã Â¦Â¤Ã Â§ÂÃ Â¦Â¨ Ã Â¦â€ Ã Â¦Â®Ã Â¦Â¦Ã Â¦Â¾Ã Â¦Â¨Ã Â¦Â¿ Ã°Å¸â€ºâ€˜
import javafx.scene.Parent; // Ã°Å¸â€ºâ€˜ Ã Â¦Â¨Ã Â¦Â¤Ã Â§ÂÃ Â¦Â¨ Ã Â¦â€ Ã Â¦Â®Ã Â¦Â¦Ã Â¦Â¾Ã Â¦Â¨Ã Â¦Â¿ Ã°Å¸â€ºâ€˜

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

    // Ã°Å¸â€ºâ€˜ Ã Â¦Â«Ã Â¦Â¿Ã Â¦â€¢Ã Â§ÂÃ Â¦Â¸: FXML Ã Â¦Â¥Ã Â§â€¡Ã Â¦â€¢Ã Â§â€¡ Ã Â¦Â²Ã Â§â€¹Ã Â¦Â¡ Ã Â¦Â¹Ã Â¦â€œÃ Â¦Â¯Ã Â¦Â¼Ã Â¦Â¾ Ã Â¦â€¢Ã Â¦Â¨Ã Â§ÂÃ Â¦Å¸Ã Â§â€¡Ã Â¦Â¨Ã Â§ÂÃ Â¦Å¸ Ã Â¦ÂÃ Â¦Â°Ã Â¦Â¿Ã Â¦Â¯Ã Â¦Â¼Ã Â¦Â¾ Ã°Å¸â€ºâ€˜
    @FXML private StackPane contentArea;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        System.out.println("Ã¢Å“â€¦ Dashboard Initialized");

        // 1. Setup Main Sidebar Navigation
        // Ã Â¦â€ Ã Â¦ÂªÃ Â¦Â¨Ã Â¦Â¿ Ã Â¦Å¡Ã Â¦Â¾Ã Â¦â€¡Ã Â¦Â²Ã Â§â€¡ Ã Â¦ÂÃ Â¦â€¡ Ã Â¦Â¸Ã Â§â€¡Ã Â¦Å¸Ã Â¦â€ Ã Â¦ÂªÃ Â§â€¡Ã Â¦Â° Ã Â¦Â­Ã Â§â€¡Ã Â¦Â¤Ã Â¦Â°Ã Â§â€¡Ã Â¦â€¡ loadContent Ã Â¦â€¢Ã Â¦Â² Ã Â¦â€¢Ã Â¦Â°Ã Â¦Â¤Ã Â§â€¡ Ã Â¦ÂªÃ Â¦Â¾Ã Â¦Â°Ã Â§â€¡Ã Â¦Â¨Ã Â¥Â¤
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);

        // Ã°Å¸â€ºâ€˜ Ã Â¦Â«Ã Â¦Â¿Ã Â¦â€¢Ã Â§ÂÃ Â¦Â¸ Ã Â§Â§: initialize-Ã Â¦Â Ã Â¦ÂªÃ Â§ÂÃ Â¦Â°Ã Â¦Â¥Ã Â¦Â® Ã Â¦â€¢Ã Â¦Â¨Ã Â§ÂÃ Â¦Å¸Ã Â§â€¡Ã Â¦Â¨Ã Â§ÂÃ Â¦Å¸ (Ã Â¦Â¹Ã Â§â€¹Ã Â¦Â®) Ã Â¦Â²Ã Â§â€¹Ã Â¦Â¡ Ã Â¦â€¢Ã Â¦Â°Ã Â§ÂÃ Â¦Â¨ Ã°Å¸â€ºâ€˜
        // Ã Â¦ÂÃ Â¦â€¡ Ã Â¦Â¹Ã Â§â€¹Ã Â¦Â® FXML Ã Â¦Å¸Ã Â¦Â¿Ã Â¦â€¡ Ã Â¦â€ Ã Â¦ÂªÃ Â¦Â¨Ã Â¦Â¾Ã Â¦Â° Ã Â¦ÂªÃ Â§ÂÃ Â¦Â°Ã Â¦Â¨Ã Â§â€¹ Ã Â¦Â¡Ã Â§ÂÃ Â¦Â¯Ã Â¦Â¾Ã Â¦Â¶Ã Â¦Â¬Ã Â§â€¹Ã Â¦Â°Ã Â§ÂÃ Â¦Â¡ Ã Â¦â€¢Ã Â¦Â¨Ã Â§ÂÃ Â¦Å¸Ã Â§â€¡Ã Â¦Â¨Ã Â§ÂÃ Â¦Å¸ Ã Â¦Â§Ã Â¦Â¾Ã Â¦Â°Ã Â¦Â£ Ã Â¦â€¢Ã Â¦Â°Ã Â¦Â¬Ã Â§â€¡Ã Â¥Â¤
        loadContent("/com/example/demo1/app/fxml/dashboard/dashboard.fxml");

        // 2. Setup Profile Navigation
        // Ã Â¦Â¯Ã Â§â€¡Ã Â¦Â¹Ã Â§â€¡Ã Â¦Â¤Ã Â§Â Ã Â¦â€ Ã Â¦ÂªÃ Â¦Â¨Ã Â¦Â¿ FXML-Ã Â¦Â onAction="#handleProfileClick" Ã Â¦Â¸Ã Â§â€¡Ã Â¦Å¸ Ã Â¦â€¢Ã Â¦Â°Ã Â§â€¡Ã Â¦â€ºÃ Â§â€¡Ã Â¦Â¨, Ã Â¦Â¤Ã Â¦Â¾Ã Â¦â€¡ Ã Â¦ÂÃ Â¦â€¡ Ã Â¦Â²Ã Â¦Å“Ã Â¦Â¿Ã Â¦â€¢Ã Â¦Å¸Ã Â¦Â¿ (setOnAction) Ã Â¦â€¦Ã Â¦ÂªÃ Â§ÂÃ Â¦Â°Ã Â¦Â¯Ã Â¦Â¼Ã Â§â€¹Ã Â¦Å“Ã Â¦Â¨Ã Â§â‚¬Ã Â¦Â¯Ã Â¦Â¼, Ã Â¦Â¤Ã Â¦Â¬Ã Â§â€¡ Ã Â¦ÂÃ Â¦Å¸Ã Â¦Â¿ Ã Â¦Â¥Ã Â¦Â¾Ã Â¦â€¢Ã Â¦Â²Ã Â§â€¡Ã Â¦â€œ Ã Â¦Â¸Ã Â¦Â¾Ã Â¦Â§Ã Â¦Â¾Ã Â¦Â°Ã Â¦Â£Ã Â¦Â¤ Ã Â¦Â¸Ã Â¦Â®Ã Â¦Â¸Ã Â§ÂÃ Â¦Â¯Ã Â¦Â¾ Ã Â¦Â¹Ã Â¦Â¯Ã Â¦Â¼ Ã Â¦Â¨Ã Â¦Â¾Ã Â¥Â¤
        if (btnProfile != null) {
            btnProfile.setOnAction(this::handleProfileClick);
        }
        if (btnEmergencyHelp != null) {
            btnEmergencyHelp.setOnAction(event -> {
                System.out.println("DEBUG: Loading Emergency Help view...");
                loadContent("/com/example/demo1/app/fxml/features/emergency-help.fxml");
            });
        }

        // 3. Setup Placeholder Features (Ã Â¦Â¬Ã Â¦Â¾Ã Â¦â€¢Ã Â¦Â¿ Ã Â¦Â¬Ã Â¦Â¾Ã Â¦Å¸Ã Â¦Â¨Ã Â¦â€”Ã Â§ÂÃ Â¦Â²Ã Â§â€¹Ã Â¦â€œ loadContent Ã Â¦Â¬Ã Â§ÂÃ Â¦Â¯Ã Â¦Â¬Ã Â¦Â¹Ã Â¦Â¾Ã Â¦Â° Ã Â¦â€¢Ã Â¦Â°Ã Â¦Â¤Ã Â§â€¡ Ã Â¦ÂªÃ Â¦Â¾Ã Â¦Â°Ã Â§â€¡)
        setupPlaceholder(btnAiHelper, "AI Ã Â¦Â¸Ã Â¦Â¹Ã Â¦Â¾Ã Â¦Â¯Ã Â¦Â¼Ã Â¦â€¢");
        setupPlaceholder(btnVideoEducation, "Ã Â¦Â­Ã Â¦Â¿Ã Â¦Â¡Ã Â¦Â¿Ã Â¦â€œ Ã Â¦Â¶Ã Â¦Â¿Ã Â¦â€¢Ã Â§ÂÃ Â¦Â·Ã Â¦Â¾");
        // ... Ã Â¦â€¦Ã Â¦Â¨Ã Â§ÂÃ Â¦Â¯Ã Â¦Â¾Ã Â¦Â¨Ã Â§ÂÃ Â¦Â¯ Ã Â¦Â¬Ã Â¦Â¾Ã Â¦Å¸Ã Â¦Â¨ ...
        btnGovtSchemes.setOnAction(event -> {
            setPage("govt-schemes.fxml"); // Ã Â¦â€ Ã Â¦ÂªÃ Â¦Â¨Ã Â¦Â¾Ã Â¦Â° FXML Ã Â¦Â«Ã Â¦Â¾Ã Â¦â€¡Ã Â¦Â²Ã Â§â€¡Ã Â¦Â° Ã Â¦Â¨Ã Â¦Â¾Ã Â¦Â®
        });


    }

    // Ã°Å¸â€ºâ€˜ Ã Â¦Â¨Ã Â¦Â¤Ã Â§ÂÃ Â¦Â¨ Ã Â¦Â®Ã Â§â€¡Ã Â¦Â¥Ã Â¦Â¡: StackPane Ã Â¦Â FXML Ã Â¦Â²Ã Â§â€¹Ã Â¦Â¡ Ã Â¦â€¢Ã Â¦Â°Ã Â¦Â¾Ã Â¦Â° Ã Â¦Å“Ã Â¦Â¨Ã Â§ÂÃ Â¦Â¯ Ã°Å¸â€ºâ€˜
    private void loadContent(String fxmlPath) {
        if (contentArea == null) {
            System.err.println("Ã¢ÂÅ’ ERROR: contentArea StackPane is NULL. FXML might not be correctly linked.");
            return;
        }
        try {
            // FXML Ã Â¦Â²Ã Â§â€¹Ã Â¦Â¡ Ã Â¦â€¢Ã Â¦Â°Ã Â§ÂÃ Â¦Â¨
            Parent content = FXMLLoader.load(Objects.requireNonNull(getClass().getResource(fxmlPath)));

            // contentArea Ã Â¦ÂÃ Â¦Â° Ã Â¦Â­Ã Â§â€¡Ã Â¦Â¤Ã Â¦Â°Ã Â§â€¡Ã Â¦Â° Ã Â¦ÂªÃ Â§ÂÃ Â¦Â°Ã Â¦Â¾Ã Â¦Â¤Ã Â¦Â¨ Ã Â¦â€¢Ã Â¦Â¨Ã Â§ÂÃ Â¦Å¸Ã Â§â€¡Ã Â¦Â¨Ã Â§ÂÃ Â¦Å¸ Ã Â¦Â¸Ã Â¦Â°Ã Â¦Â¿Ã Â¦Â¯Ã Â¦Â¼Ã Â§â€¡ Ã Â¦Â¨Ã Â¦Â¤Ã Â§ÂÃ Â¦Â¨ FXML Ã Â¦Â¯Ã Â§ÂÃ Â¦â€¢Ã Â§ÂÃ Â¦Â¤ Ã Â¦â€¢Ã Â¦Â°Ã Â§ÂÃ Â¦Â¨
            contentArea.getChildren().setAll(content);

            System.out.println("Page loaded successfully into contentArea: " + fxmlPath);

        } catch (IOException e) {
            System.err.println("Failed to load FXML content: " + fxmlPath + e.getMessage());
            showErrorAlert("Ã Â¦ÂªÃ Â§â€¡Ã Â¦Å“ Ã Â¦Â²Ã Â§â€¹Ã Â¦Â¡ Ã Â¦â€¢Ã Â¦Â°Ã Â¦Â¾ Ã Â¦Â¯Ã Â¦Â¾Ã Â¦Â¯Ã Â¦Â¼Ã Â¦Â¨Ã Â¦Â¿: " + fxmlPath);
        } catch (Exception e) {
            System.err.println("General error loading content: " + e.getMessage());
        }
    }
    private void setPage(String fxmlFile) {
        try {
            // Ã Â¦â€ Ã Â¦ÂªÃ Â¦Â¨Ã Â¦Â¾Ã Â¦Â° FXML Ã Â¦Â«Ã Â¦Â¾Ã Â¦â€¡Ã Â¦Â²Ã Â§â€¡Ã Â¦Â° Ã Â¦ÂªÃ Â¦Â¾Ã Â¦Â¥ Ã Â¦â€¦Ã Â¦Â¨Ã Â§ÂÃ Â¦Â¯Ã Â¦Â¾Ã Â§Å¸Ã Â§â‚¬ Ã Â¦Â¨Ã Â¦Â¿Ã Â¦Å¡Ã Â§â€¡Ã Â¦Â° Ã Â¦Â²Ã Â¦Â¾Ã Â¦â€¡Ã Â¦Â¨Ã Â¦Å¸Ã Â¦Â¿ Ã Â¦ÂªÃ Â¦Â°Ã Â¦Â¿Ã Â¦Â¬Ã Â¦Â°Ã Â§ÂÃ Â¦Â¤Ã Â¦Â¨ Ã Â¦â€¢Ã Â¦Â°Ã Â§ÂÃ Â¦Â¨
            Parent fxml = FXMLLoader.load(getClass().getResource("/com/example/demo1/app/fxml/features/" + fxmlFile));
            contentArea.getChildren().clear(); // Ã Â¦â€ Ã Â¦â€”Ã Â§â€¡Ã Â¦Â° Ã Â¦â€¢Ã Â¦Â¨Ã Â§ÂÃ Â¦Å¸Ã Â§â€¡Ã Â¦Â¨Ã Â§ÂÃ Â¦Å¸ Ã Â¦Â®Ã Â§ÂÃ Â¦â€ºÃ Â§â€¡ Ã Â¦Â«Ã Â§â€¡Ã Â¦Â²Ã Â¦Â¾
            contentArea.getChildren().add(fxml); // Ã Â¦Â¨Ã Â¦Â¤Ã Â§ÂÃ Â¦Â¨ Ã Â¦â€¢Ã Â¦Â¨Ã Â§ÂÃ Â¦Å¸Ã Â§â€¡Ã Â¦Â¨Ã Â§ÂÃ Â¦Å¸ Ã Â¦Â¯Ã Â§â€¹Ã Â¦â€” Ã Â¦â€¢Ã Â¦Â°Ã Â¦Â¾
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading page: " + fxmlFile);
        }
    }

    // Ã°Å¸â€ºâ€˜ Ã Â¦Â«Ã Â¦Â¿Ã Â¦â€¢Ã Â§ÂÃ Â¦Â¸ Ã Â§Â¨: Profile Ã Â¦â€¢Ã Â§ÂÃ Â¦Â²Ã Â¦Â¿Ã Â¦â€¢ Ã Â¦Â¹Ã Â§ÂÃ Â¦Â¯Ã Â¦Â¾Ã Â¦Â¨Ã Â§ÂÃ Â¦Â¡Ã Â¦Â²Ã Â¦Â¾Ã Â¦Â° Ã Â¦ÂªÃ Â¦Â°Ã Â¦Â¿Ã Â¦Â¬Ã Â¦Â°Ã Â§ÂÃ Â¦Â¤Ã Â¦Â¨ Ã Â¦â€¢Ã Â¦Â°Ã Â§ÂÃ Â¦Â¨ Ã°Å¸â€ºâ€˜
    @FXML
    private void handleProfileClick(ActionEvent event) {
        System.out.println("DEBUG: Attempting to load Profile into contentArea. Session status: " + (SessionManager.getLoggedInUser() != null ? "Active" : "NULL"));

        // Ã¢ÂÅ’ NavigationHelper.navigateTo Ã Â¦â€¢Ã Â¦Â² Ã Â¦â€¢Ã Â¦Â°Ã Â¦Â¬Ã Â§â€¡Ã Â¦Â¨ Ã Â¦Â¨Ã Â¦Â¾, Ã Â¦â€¢Ã Â¦Â¾Ã Â¦Â°Ã Â¦Â£ Ã Â¦ÂÃ Â¦Å¸Ã Â¦Â¿ Ã Â¦ÂªÃ Â§ÂÃ Â¦Â°Ã Â§â€¹ Scene Ã Â¦ÂªÃ Â¦Â°Ã Â¦Â¿Ã Â¦Â¬Ã Â¦Â°Ã Â§ÂÃ Â¦Â¤Ã Â¦Â¨ Ã Â¦â€¢Ã Â¦Â°Ã Â§â€¡!
        // Ã¢ÂÅ’ Ã Â¦ÂÃ Â¦Â° Ã Â¦ÂªÃ Â¦Â°Ã Â¦Â¿Ã Â¦Â¬Ã Â¦Â°Ã Â§ÂÃ Â¦Â¤Ã Â§â€¡ loadContent Ã Â¦Â®Ã Â§â€¡Ã Â¦Â¥Ã Â¦Â¡ Ã Â¦â€¢Ã Â¦Â² Ã Â¦â€¢Ã Â¦Â°Ã Â§ÂÃ Â¦Â¨Ã Â¥Â¤

        loadContent("/com/example/demo1/app/fxml/features/profile-view.fxml");

        // Ã Â¦Â¹Ã Â§â€¹Ã Â¦Â® Ã Â¦Â¬Ã Â¦Â¾Ã Â¦Å¸Ã Â¦Â¨Ã Â§â€¡ Ã Â¦â€¢Ã Â§ÂÃ Â¦Â²Ã Â¦Â¿Ã Â¦â€¢ Ã Â¦Â¹Ã Â¦Â²Ã Â§â€¡, Ã Â¦â€ Ã Â¦ÂªÃ Â¦Â¨Ã Â¦Â¿ Ã Â¦Å¡Ã Â¦Â¾Ã Â¦â€¡Ã Â¦Â²Ã Â§â€¡ Ã Â¦ÂÃ Â¦Å¸Ã Â¦Â¿Ã Â¦â€œ Ã Â¦Â¯Ã Â§â€¹Ã Â¦â€” Ã Â¦â€¢Ã Â¦Â°Ã Â¦Â¤Ã Â§â€¡ Ã Â¦ÂªÃ Â¦Â¾Ã Â¦Â°Ã Â§â€¡Ã Â¦Â¨:
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
        alert.setTitle("Ã Â¦Â²Ã Â§â€¹Ã Â¦Â¡Ã Â¦Â¿Ã Â¦â€š Ã Â¦Â¤Ã Â§ÂÃ Â¦Â°Ã Â§ÂÃ Â¦Å¸Ã Â¦Â¿");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}




