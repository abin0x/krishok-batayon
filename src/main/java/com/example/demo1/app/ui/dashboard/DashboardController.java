package com.example.demo1.app.ui.dashboard;

import com.example.demo1.app.util.SessionManager;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    // ডিফল্ট ড্যাশবোর্ড ও ফিচার পেইজগুলোর FXML পাথ
    private static final String DASHBOARD_VIEW = "/com/example/demo1/app/fxml/dashboard/dashboard.fxml";
    private static final String PROFILE_VIEW = "/com/example/demo1/app/fxml/features/profile-view.fxml";
    private static final String EMERGENCY_VIEW = "/com/example/demo1/app/fxml/features/emergency-help.fxml";
    private static final String FEATURES_PREFIX = "/com/example/demo1/app/fxml/features/";

    @FXML private StackPane contentArea;
    @FXML private Button btnProfile;
    @FXML private Button btnEmergencyHelp;
    @FXML private Button btnGovtSchemes;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // প্রথমে ড্যাশবোর্ড কনটেন্ট লোড করি
        loadContent(DASHBOARD_VIEW);

        // প্রয়োজনীয় বাটনগুলোর ক্লিক ইভেন্ট এখানে সেট করা হচ্ছে
        if (btnProfile != null) {
            btnProfile.setOnAction(this::handleProfileClick);
        }
        if (btnEmergencyHelp != null) {
            btnEmergencyHelp.setOnAction(event -> loadContent(EMERGENCY_VIEW));
        }
        if (btnGovtSchemes != null) {
            btnGovtSchemes.setOnAction(event -> setFeaturePage("govt-schemes.fxml"));
        }
    }

    @FXML
    private void handleProfileClick(ActionEvent event) {
        // প্রোফাইল পেইজে যাওয়ার আগে সেশন স্ট্যাটাস লগ করি
        String sessionStatus = SessionManager.getLoggedInUser() == null ? "NULL" : "ACTIVE";
        System.out.println("Profile navigation requested. Session: " + sessionStatus);
        loadContent(PROFILE_VIEW);
    }

    private void setFeaturePage(String fxmlFileName) {
        // features ফোল্ডারের নির্দিষ্ট পেইজ লোড করার শর্টকাট মেথড
        loadContent(FEATURES_PREFIX + fxmlFileName);
    }

    private void loadContent(String fxmlPath) {
        // contentArea না থাকলে FXML বাইন্ডিং সমস্যা হয়েছে
        if (contentArea == null) {
            System.err.println("Dashboard content area is not connected in FXML.");
            return;
        }

        // রিসোর্স না পেলে ব্যবহারকারীকে বাংলা বার্তা দেখাই
        URL resource = getClass().getResource(fxmlPath);
        if (resource == null) {
            showErrorAlert("\u09AA\u09C7\u099C \u0996\u09C1\u0981\u099C\u09C7 \u09AA\u09BE\u0993\u09DF\u09BE \u09AF\u09BE\u09AF\u09BC\u09A8\u09BF: " + fxmlPath);
            return;
        }

        try {
            // নতুন FXML লোড করে contentArea-তে replace করি
            Parent content = FXMLLoader.load(resource);
            contentArea.getChildren().setAll(content);
        } catch (Exception e) {
            System.err.println("Failed to load view: " + fxmlPath);
            e.printStackTrace();
            showErrorAlert("\u09AA\u09C7\u099C \u09B2\u09CB\u09A1 \u0995\u09B0\u09BE \u09AF\u09BE\u09AF\u09BC\u09A8\u09BF\u0964");
        }
    }

    private void showErrorAlert(String message) {
        // সাধারণ বাংলা এরর এলার্ট দেখানোর কমন মেথড
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("\u09B2\u09CB\u09A1\u09BF\u0982 \u09A4\u09CD\u09B0\u09C1\u099F\u09BF");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
