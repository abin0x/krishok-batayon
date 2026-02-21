package com.example.demo1.app.ui;

import com.example.demo1.app.model.User;
import com.example.demo1.app.util.NavigationHelper;
import com.example.demo1.app.util.SessionManager;
import com.example.demo1.app.util.UiAlerts;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

public class ProfileController {

    @FXML private Label lblUsernameInitial;
    @FXML private TextField txtName;
    @FXML private TextField txtMobile;
    @FXML private TextField txtEmail;
    @FXML private TextField txtUsername;

    public void initialize() {
        loadUserProfile();
    }

    private void loadUserProfile() {
        User user = SessionManager.getLoggedInUser();
        if (user == null) {
            UiAlerts.warning("সেশন শেষ / Session Expired", "কোনো সক্রিয় লগইন সেশন নেই। Please login again.");
            handleLogout();
            return;
        }

        txtName.setText(user.getName());
        txtMobile.setText(user.getMobile());
        txtEmail.setText(user.getEmail());
        txtUsername.setText(user.getUsername());

        if (user.getName() != null && !user.getName().isBlank()) {
            lblUsernameInitial.setText(user.getName().substring(0, 1).toUpperCase());
        } else {
            lblUsernameInitial.setText("?");
        }
    }

    @FXML
    private void handleUploadPicture() {
        UiAlerts.info("ফিচার আসছে / Coming Soon", "প্রোফাইল ছবি আপলোড ফিচার শিগগিরই যুক্ত হবে।");
    }

    @FXML
    private void handleLogout() {
        SessionManager.clearSession();
        try {
            NavigationHelper.navigateTo(txtName.getScene(), "/com/example/demo1/app/fxml/features/hello-view.fxml");
        } catch (IOException e) {
            UiAlerts.errorWithCause("লোডিং ত্রুটি / Loading Error", "লগইন পেজ লোড করা যায়নি।", e);
        }
    }
}
