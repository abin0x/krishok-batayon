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
                : "কৃষক";
        lblWelcomeUser.setText("স্বাগতম, " + name);
    }
}
