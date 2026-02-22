package com.example.demo1.app.ui;

import com.example.demo1.app.model.User;
import com.example.demo1.app.service.JsonDbService;
import com.example.demo1.app.util.SessionManager;
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

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;

    @FXML
    protected void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.isBlank() || password.isBlank()) {
            showAlert(Alert.AlertType.WARNING, "Warning", "Please enter username/mobile and password.");
            return;
        }

        try {
            JsonDbService dbService = new JsonDbService();
            User loggedInUser = dbService.loginUser(username, password);

            if (loggedInUser == null) {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid credentials.");
                return;
            }

            SessionManager.setLoggedInUser(loggedInUser);

            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/demo1/app/fxml/layout/main-layout.fxml"));
            Parent root = fxmlLoader.load();

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            scene.getStylesheets().add(getClass().getResource("/com/example/demo1/app/css/dashboard.css").toExternalForm());

            stage.setTitle("\u0995\u09C3\u09B7\u0995 \u09AC\u09BE\u09A4\u09BE\u09AF\u09BC\u09A8");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open dashboard.");
            e.printStackTrace();
        }
    }

    @FXML
    public void onOpenRegisterClick(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/example/demo1/app/fxml/features/register-view.fxml"));
            Parent root = fxmlLoader.load();

            Scene scene = new Scene(root, 800, 600);
            scene.getStylesheets().add(getClass().getResource("/com/example/demo1/app/css/style.css").toExternalForm());

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setTitle("Register");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Error", "Could not open register page.");
            e.printStackTrace();
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
