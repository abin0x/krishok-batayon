package com.example.demo1.app.controller;
import com.example.demo1.app.config.NavigationManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.util.LinkedHashMap;
import java.util.Map;

public class SidebarController {

    @FXML private Button btnHome,btnAdvisory,btnStorage,btnLocalManagement,btnMachinery,btnSmartDiagnostic,btnDigitalHat,btnMarketFinance,btnGovtSchemes,btnEmergencyHelp,btnProfile;

    private final Map<String, Button> buttonById = new LinkedHashMap<>();//eta ekta dictionary ja button er id er sathe button object map kore rakhe, jate poroborti te id diye button access kora jai
    private final Map<Button, String> viewByButton = new LinkedHashMap<>();//eta ekta dictionary ja button object er sathe view path map kore rakhe, jate poroborti te button click korle tar corresponding view load kora jai
    private MainLayoutController mainLayoutController;//eta main layout controller er reference rakhe, jate sidebar theke main layout er view change kora jai
    private Button activeButton;//eta currently active button er reference rakhe, jate active button ke highlight kora jai

    @FXML
    private void initialize() {
        registerButtons();//sidebar er button gulo ke register kore rakhe, jate poroborti te button id diye button access kora jai
        applyLabelsAndRoutes();//sidebar er button gulo ke label set kore rakhe ebong tar corresponding view path map kore rakhe
        setActiveButton(btnHome);//default active button set kore rakhe, jate application start hole home view dekhay ebong home button active thake
    }

    @FXML
    private void handleNavigation(ActionEvent event) {
        if (mainLayoutController == null) {//first a check kore je main layout controller set ache kina, jodi na thake tahole navigation handle kora jabe na
            return;
        }

        if (!(event.getSource() instanceof Button clicked)) {//user jei tai click korche seta ki button kina check kore, jodi na hoy tahole navigation handle kora jabe na
            return;
        }
        if (clicked == activeButton) {//jodi already dashboard page a thake and abr dashboard page a click kore thaole abr page load hobe na
            return;
        }

        String viewPath = viewByButton.get(clicked);
        if (viewPath != null && mainLayoutController.loadView(viewPath)) {//jodi clicked button er corresponding view path thake ebong successfully view load hoy tahole active button update kora hobe
            setActiveButton(clicked);
        }
    }

    public void setMainLayoutController(MainLayoutController controller) {
        this.mainLayoutController = controller;
        if (controller != null) {
            syncActiveView(controller.getActiveViewPath());
        }
    }

    public void syncActiveView(String activeViewPath) {//atar maddhome ami jodi croopadviosry page a thaki and abr jodi irrgation page a jai tahole sidebar ar active button ta automatically update hoye jabe
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


    //button gula ke register kore rakhe, jate poroborti te button id diye button access kora jai
    private void registerButtons() {
        addButton("btnHome", btnHome);
        addButton("btnAdvisory", btnAdvisory);
        addButton("btnStorage", btnStorage);
        addButton("btnLocalManagement", btnLocalManagement);
        addButton("btnMachinery", btnMachinery);
        addButton("btnSmartDiagnostic", btnSmartDiagnostic);
        addButton("btnDigitalHat", btnDigitalHat);
        addButton("btnMarketFinance", btnMarketFinance);
        addButton("btnGovtSchemes", btnGovtSchemes);
        addButton("btnEmergencyHelp", btnEmergencyHelp);
        addButton("btnProfile", btnProfile);
    }

    private void addButton(String id, Button button) {
        if (button != null) {
            buttonById.put(id, button);
        }
    }


    //sidebar er button gulo ke label set kore rakhe ebong tar corresponding view path map kore rakhe, jate poroborti te button click korle tar corresponding view load kora jai
    private void applyLabelsAndRoutes() {
        Map<String, NavigationManager.NavItem> routes = NavigationManager.sidebarRoutes();
        for (Map.Entry<String, NavigationManager.NavItem> route : routes.entrySet()) {
            Button button = buttonById.get(route.getKey());
            if (button == null) {
                continue;
            }
            button.setText(route.getValue().displayName());
            viewByButton.put(button, route.getValue().viewPath());
        }
    }


    //eti sidebar er sob gula btn theke active namer css style remove kore,and then just clk kora btn active style jukto kore
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
