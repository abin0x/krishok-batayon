package com.example.demo1.app.controller;

import com.example.demo1.app.model.User;
import com.example.demo1.app.service.JsonDbService;
import com.example.demo1.app.util.NavigationHelper;
import com.example.demo1.app.util.UiAlerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;


public class RegisterController {
    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField mobileField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;

    @FXML
    protected void handleRegister(ActionEvent event) {
        String name = fullNameField.getText();
        String email = emailField.getText();
        String mobile = mobileField.getText();
        String pass = passwordField.getText();
        String confirmPass = confirmPasswordField.getText();

        if (name.isBlank() || email.isBlank() || mobile.isBlank() || pass.isBlank()) {
            UiAlerts.error("Error", "Please fill all fields.");
            return;
        }

        if (!pass.equals(confirmPass)) {
            UiAlerts.error("Error", "Passwords do not match.");
            return;
        }

        try {
            JsonDbService dbService = new JsonDbService();
            String hashedPassword = JsonDbService.hashPassword(pass);
            User newUser = new User(name, email, mobile, mobile, hashedPassword);

            if (!dbService.registerUser(newUser)) {
                UiAlerts.error("Error", "User already exists.");
                return;
            }

            UiAlerts.info("Success", "Registration completed.");
            switchToLoginScreen(event);
        } catch (Exception e) {
            UiAlerts.error("System Error", e.getMessage());
        }
    }

    public void switchToLoginScreen(ActionEvent event) {
        NavigationHelper.switchScene(event,
                "/fxml/features/hello-view.fxml",
                "Login",
                "/css/style.css");
    }


}
