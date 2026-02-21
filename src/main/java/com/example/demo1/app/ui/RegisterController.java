package com.example.demo1.app.ui;

import com.example.demo1.app.model.User;
import com.example.demo1.app.service.JsonDbService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;

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
            showAlert(Alert.AlertType.ERROR, "Error", "Please fill all fields.");
            return;
        }

        if (!pass.equals(confirmPass)) {
            showAlert(Alert.AlertType.ERROR, "Error", "Passwords do not match.");
            return;
        }

        try {
            JsonDbService dbService = new JsonDbService();
            String hashedPassword = JsonDbService.hashPassword(pass);
            User newUser = new User(name, email, mobile, mobile, hashedPassword);

            if (!dbService.registerUser(newUser)) {
                showAlert(Alert.AlertType.ERROR, "Error", "User already exists.");
                return;
            }

            showAlert(Alert.AlertType.INFORMATION, "Success", "Registration completed.");
            switchToLoginScreen(event);
        } catch (Exception e) {
            showAlert(Alert.AlertType.ERROR, "System Error", e.getMessage());
        }
    }

    public void switchToLoginScreen(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/demo1/app/fxml/features/hello-view.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/demo1/app/css/style.css").toExternalForm());

            stage.setTitle("Login");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ignored) {
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
