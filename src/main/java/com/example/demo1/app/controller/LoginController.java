package com.example.demo1.app.controller;

import com.example.demo1.app.model.User;
import com.example.demo1.app.service.JsonDbService;
import com.example.demo1.app.util.NavigationHelper;
import com.example.demo1.app.util.SessionManager;
import com.example.demo1.app.util.UiAlerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            UiAlerts.warning("Warning", "Please enter username/mobile and password.");
            return;
        }

        try {
            JsonDbService dbService = new JsonDbService();
            User loggedInUser = dbService.loginUser(username, password);

            if (loggedInUser == null) {
                UiAlerts.error("Login Failed", "Invalid credentials.");
                return;
            }

            SessionManager.setLoggedInUser(loggedInUser);
            NavigationHelper.switchScene(event,
                    "/fxml/layout/main-layout.fxml",
                    "???? ???????",
                    "/css/dashboard.css");
        } catch (Exception e) {
            UiAlerts.error("Error", "Could not open dashboard.");
            e.printStackTrace();
        }
    }

    @FXML
    public void onOpenRegisterClick(ActionEvent event) {
        NavigationHelper.switchScene(event,
                "/fxml/features/register-view.fxml",
                "Register",
                "/css/dashboard.css");
    }
}
