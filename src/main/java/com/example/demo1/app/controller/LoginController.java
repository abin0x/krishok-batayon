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
    // FXML paths and CSS
    private static final String MAIN_LAYOUT_FXML = "/fxml/layout/main-layout.fxml";
    private static final String REGISTER_FXML = "/fxml/features/register-view.fxml";
    private static final String APP_CSS = "/css/dashboard.css";

    @FXML private TextField usernameField;//user theke input nibe username/mobile number
    @FXML private PasswordField passwordField;//user theke input nibe password
    private final JsonDbService dbService = new JsonDbService();//ata JsonDbService class er object create koreche, jar maddhome user login korte parbe,ata JSOn file a giye user er data check korbe

    @FXML//login button click event handler
    protected void onLoginButtonClick(ActionEvent event) {
        String username = usernameField.getText().trim();//input nibe username,trim() method diye extra space remove koreche
        String password = passwordField.getText().trim();

        // Check if username or password is empty
        if (username.isBlank() || password.isBlank()) {
            UiAlerts.warning("Warning", "Please enter username/mobile and password.");
            return;
        }


        //jodi username and password thik thake tahole user ke dashboard a niye jabe,na hole error message dekhabe and infromation ene loggedInUser object a rakhbe
        try {
            User loggedInUser = dbService.loginUser(username, password);
            if (loggedInUser == null) {
                UiAlerts.error("Login Failed", "Invalid credentials.");
                return;
            }


            // Store logged-in user information in session and navigate to dashboard
            SessionManager.setLoggedInUser(loggedInUser);
            NavigationHelper.switchScene(event, MAIN_LAYOUT_FXML, "Dashboard", APP_CSS);//dashboard a niye jabe
        } catch (Exception e) {
            UiAlerts.error("Error", "Could not open dashboard.");//jodi kuno karone dashboard open na hoy tahole error message dekhabe
        }
    }


    //register button click event handler, jodi user register korte chay tahole register page a niye jabe
    @FXML
    public void onOpenRegisterClick(ActionEvent event) {
        NavigationHelper.switchScene(event, REGISTER_FXML, "Register", APP_CSS);
    }
}
