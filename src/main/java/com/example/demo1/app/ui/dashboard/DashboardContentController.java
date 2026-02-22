package com.example.demo1.app.ui.dashboard;

import com.example.demo1.app.model.User;
import com.example.demo1.app.util.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class DashboardContentController {

    @FXML
    private Label lblWelcomeUser;

    @FXML
    public void initialize() {
        User user = SessionManager.getLoggedInUser();
        String name = (user != null && user.getName() != null && !user.getName().isBlank())
                ? user.getName()
                : "\u0995\u09C3\u09B7\u0995";
        lblWelcomeUser.setText("\u09B8\u09CD\u09AC\u09BE\u0997\u09A4\u09AE, " + name);
    }
}
