package com.example.demo1.app.ui;

import com.example.demo1.app.controller.MainLayoutController;
import com.example.demo1.app.model.User;
import com.example.demo1.app.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class TopbarController {

    private MainLayoutController mainLayoutController;

    @FXML
    private Label lblProfileName;

    @FXML
    public void initialize() {
        updateProfileLabel();
    }

    public void setMainLayoutController(MainLayoutController mainLayoutController) {
        this.mainLayoutController = mainLayoutController;
        updateProfileLabel();
    }

    @FXML
    private void handleLogout() {
        if (mainLayoutController != null) {
            mainLayoutController.handleLogout();
        }
    }

    private void updateProfileLabel() {
        if (lblProfileName == null) {
            return;
        }

        User user = SessionManager.getLoggedInUser();
        if (user == null) {
            lblProfileName.setText("👤 ব্যবহারকারী");
            return;
        }

        String rawName = user.getName();
        if (rawName == null || rawName.isBlank()) {
            rawName = user.getUsername();
        }

        if (rawName == null || rawName.isBlank()) {
            lblProfileName.setText("👤 ব্যবহারকারী");
            return;
        }

        String[] parts = rawName.trim().split("\\s+");
        String lastNamePart = parts[parts.length - 1];
        lblProfileName.setText("👤 " + lastNamePart);
    }
}
