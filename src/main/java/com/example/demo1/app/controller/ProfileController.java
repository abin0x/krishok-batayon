package com.example.demo1.app.controller;
import com.example.demo1.app.model.User;
import com.example.demo1.app.service.JsonDbService;
import com.example.demo1.app.util.NavigationHelper;
import com.example.demo1.app.util.SessionManager;
import com.example.demo1.app.util.UiAlerts;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ProfileController {

    @FXML private Label lblUsernameInitial,lblLastSaved;
    @FXML private TextField txtName,txtPhone,txtDistrict;
    @FXML private ImageView profileImageView;

    private String selectedImagePath;

    @FXML//akahne user ar profile data load hocche
    public void initialize() {
        loadUserProfile();
    }
    //akhane user ar profile data load hocche, jodi session expire hoy tahole warning dekhabe and login page e niye jabe
    private void loadUserProfile() {
        User user = SessionManager.getLoggedInUser();
        if (user == null) {
            UiAlerts.warning("Session Expired", "No active login session found. Please login again.");
            handleLogout();
            return;
        }

        txtName.setText(user.getName());
        txtPhone.setText(user.getMobile());
        txtDistrict.setText(user.getDistrict());
        selectedImagePath = user.getProfileImagePath();
        updateDisplayName(user.getName());
        loadProfileImage(selectedImagePath);
    }

    @FXML//akhane user profile picture upload korar jonno file chooser open hobe
    private void handleUploadPicture() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("Select Profile Image");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = chooser.showOpenDialog(txtName.getScene().getWindow());
        if (selectedFile != null) {//jodi user kono file select kore tahole tar path save hobe and profile image load hobe
            selectedImagePath = selectedFile.getAbsolutePath();
            loadProfileImage(selectedImagePath);
        }
    }

    @FXML
    private void handleSaveProfile() {
        User user = SessionManager.getLoggedInUser();
        if (user == null) {
            UiAlerts.error("Save Failed", "Login session not found.");
            return;
        }

        user.setName(txtName.getText());
        user.setMobile(txtPhone.getText());
        user.setDistrict(txtDistrict.getText());
        user.setProfileImagePath(selectedImagePath);

        try {
            JsonDbService dbService = new JsonDbService();
            if (!dbService.updateUser(user)) {
                UiAlerts.warning("Not Saved", "Could not update user information.");
                return;
            }

            SessionManager.setLoggedInUser(user);
            updateDisplayName(user.getName());
            if (lblLastSaved != null) {
                lblLastSaved.setText("Last updated: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
            }
            UiAlerts.info("Success", "Profile information has been saved.");
        } catch (IOException e) {
            UiAlerts.errorWithCause("Save Failed", "Could not write profile data to JSON file.", e);
        }
    }

    private void updateDisplayName(String name) {
        lblUsernameInitial.setText(name == null || name.isBlank() ? "User" : name);
    }

    private void loadProfileImage(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            profileImageView.setImage(null);
            return;
        }

        File imageFile = new File(imagePath);
        profileImageView.setImage(imageFile.exists() ? new Image(imageFile.toURI().toString()) : null);
    }

    @FXML
    private void handleLogout() {
        SessionManager.clearSession();
        try {
            NavigationHelper.navigateTo(txtName.getScene(), "/fxml/features/hello-view.fxml");
        } catch (IOException e) {
            UiAlerts.errorWithCause("Loading Error", "Could not open login page.", e);
        }
    }
}
