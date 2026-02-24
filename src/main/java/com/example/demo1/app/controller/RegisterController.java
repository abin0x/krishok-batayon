package com.example.demo1.app.controller;

import com.example.demo1.app.model.User;
import com.example.demo1.app.service.JsonDbService;
import com.example.demo1.app.util.NavigationHelper;
import com.example.demo1.app.util.UiAlerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;



// Controller for the registration screen
public class RegisterController {
    private static final String LOGIN_FXML = "/fxml/features/hello-view.fxml";
    private static final String APP_CSS = "/css/style.css";

    @FXML private TextField fullNameField,mobileField,emailField;
    @FXML private PasswordField passwordField,confirmPasswordField;

    @FXML// Handle the registration process when the user clicks the register button
    private void handleRegister(ActionEvent event) {
        String name = fullNameField.getText().trim();
        String email = emailField.getText().trim();
        String mobile = mobileField.getText().trim();
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

        try {// Create a new user and attempt to register them in the database
            JsonDbService dbService = new JsonDbService();
            User newUser = new User(name, email, mobile, mobile, JsonDbService.hashPassword(pass));

            // Check if the user already exists and show an error if they do
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

    @FXML//register hole user k login page a niye jabe
    public void switchToLoginScreen(ActionEvent event) {
        NavigationHelper.switchScene(event,
                LOGIN_FXML,
                "Login",
                APP_CSS);
    }
}
