package com.example.demo1.app.ui.sidebar;

import com.example.demo1.app.config.NavigationManager;
import com.example.demo1.app.controller.MainLayoutController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class SidebarController implements Initializable {

    @FXML private Button btnHome;
    @FXML private Button btnAdvisory;
    @FXML private Button btnStorage;
    @FXML private Button btnLocalManagement;
    @FXML private Button btnMachinery;
    @FXML private Button btnFarmManagement;
    @FXML private Button btnSmartDiagnostic;
    @FXML private Button btnDigitalHat;
    @FXML private Button btnCropInsurance;
    @FXML private Button btnMarketFinance;
    @FXML private Button btnGovtSchemes;
    @FXML private Button btnExpertChat;
    @FXML private Button btnLearningHub;
    @FXML private Button btnEmergencyHelp;
    @FXML private Button btnProfile;

    private final Map<String, Button> buttonById = new LinkedHashMap<>();
    private final Map<Button, String> viewByButton = new LinkedHashMap<>();
    private MainLayoutController mainLayoutController;
    private Button activeButton;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        registerButtons();
        applyLabelsAndRoutes();
        setActiveButton(btnHome);
    }

    @FXML
    private void handleNavigation(ActionEvent event) {
        if (mainLayoutController == null) {
            return;
        }

        Button clicked = (Button) event.getSource();
        if (clicked == activeButton) {
            return;
        }

        String viewPath = viewByButton.get(clicked);
        if (viewPath != null && mainLayoutController.loadView(viewPath)) {
            setActiveButton(clicked);
        }
    }

    public void setMainLayoutController(MainLayoutController controller) {
        this.mainLayoutController = controller;
        if (controller != null) {
            syncActiveView(controller.getActiveViewPath());
        }
    }

    public void syncActiveView(String activeViewPath) {
        if (activeViewPath == null) {
            return;
        }

        for (Map.Entry<Button, String> entry : viewByButton.entrySet()) {
            if (activeViewPath.equals(entry.getValue())) {
                setActiveButton(entry.getKey());
                return;
            }
        }
    }

    private void registerButtons() {
        buttonById.put("btnHome", btnHome);
        buttonById.put("btnAdvisory", btnAdvisory);
        buttonById.put("btnStorage", btnStorage);
        buttonById.put("btnLocalManagement", btnLocalManagement);
        buttonById.put("btnMachinery", btnMachinery);
        buttonById.put("btnFarmManagement", btnFarmManagement);
        buttonById.put("btnSmartDiagnostic", btnSmartDiagnostic);
        buttonById.put("btnDigitalHat", btnDigitalHat);
        buttonById.put("btnCropInsurance", btnCropInsurance);
        buttonById.put("btnMarketFinance", btnMarketFinance);
        buttonById.put("btnGovtSchemes", btnGovtSchemes);
        buttonById.put("btnExpertChat", btnExpertChat);
        buttonById.put("btnLearningHub", btnLearningHub);
        buttonById.put("btnEmergencyHelp", btnEmergencyHelp);
        buttonById.put("btnProfile", btnProfile);
    }

    private void applyLabelsAndRoutes() {
        Map<String, NavigationManager.NavItem> routes = NavigationManager.sidebarRoutes();
        for (Map.Entry<String, NavigationManager.NavItem> route : routes.entrySet()) {
            Button button = buttonById.get(route.getKey());
            if (button == null) {
                continue;
            }
            button.setText(route.getValue().label());
            viewByButton.put(button, route.getValue().viewPath());
        }
    }

    private void setActiveButton(Button button) {
        if (button == null) {
            return;
        }

        for (Button navButton : viewByButton.keySet()) {
            navButton.getStyleClass().remove("active");
        }
        button.getStyleClass().add("active");
        activeButton = button;
    }
}
