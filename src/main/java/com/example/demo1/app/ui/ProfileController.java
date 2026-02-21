package com.example.demo1.app.ui;

import com.example.demo1.app.model.User;
import com.example.demo1.app.service.JsonDbService;
import com.example.demo1.app.util.NavigationHelper;
import com.example.demo1.app.util.SessionManager;
import com.example.demo1.app.util.UiAlerts;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ProfileController {

    @FXML private Label lblUsernameInitial;
    @FXML private Label lblCompletionPct;
    @FXML private Label lblLastSaved;
    @FXML private ProgressBar profileCompletionBar;
    @FXML private TextField txtName;
    @FXML private TextField txtPhone;
    @FXML private TextField txtLandAmount;
    @FXML private ComboBox<String> cmbDivision;
    @FXML private ComboBox<String> cmbDistrict;
    @FXML private ComboBox<String> cmbUpazila;
    @FXML private ComboBox<String> cmbSoilType;
    @FXML private ImageView profileImageView;

    private final Map<String, Map<String, List<String>>> divisionMap = new LinkedHashMap<>();
    private String selectedImagePath;

    public void initialize() {
        setupLocationMap();
        setupDropdowns();
        setupRealtimeCompletion();
        loadUserProfile();
    }

    private void setupRealtimeCompletion() {
        txtName.textProperty().addListener((obs, oldValue, newValue) -> updateCompletion());
        txtPhone.textProperty().addListener((obs, oldValue, newValue) -> updateCompletion());
        txtLandAmount.textProperty().addListener((obs, oldValue, newValue) -> updateCompletion());
        cmbDivision.valueProperty().addListener((obs, oldValue, newValue) -> updateCompletion());
        cmbDistrict.valueProperty().addListener((obs, oldValue, newValue) -> updateCompletion());
        cmbUpazila.valueProperty().addListener((obs, oldValue, newValue) -> updateCompletion());
        cmbSoilType.valueProperty().addListener((obs, oldValue, newValue) -> updateCompletion());
    }

    private void setupLocationMap() {
        Map<String, List<String>> dhaka = new LinkedHashMap<>();
        dhaka.put("ঢাকা", List.of("ধামরাই", "সাভার", "দোহার", "কেরানীগঞ্জ"));
        dhaka.put("গাজীপুর", List.of("কালিয়াকৈর", "শ্রীপুর", "কাপাসিয়া"));

        Map<String, List<String>> chattogram = new LinkedHashMap<>();
        chattogram.put("কুমিল্লা", List.of("দেবিদ্বার", "লাকসাম", "চান্দিনা"));
        chattogram.put("চট্টগ্রাম", List.of("রাউজান", "হাটহাজারী", "পটিয়া"));

        Map<String, List<String>> rajshahi = new LinkedHashMap<>();
        rajshahi.put("রাজশাহী", List.of("পবা", "বাগমারা", "চারঘাট"));
        rajshahi.put("বগুড়া", List.of("শিবগঞ্জ", "সোনাতলা", "শেরপুর"));

        divisionMap.put("ঢাকা", dhaka);
        divisionMap.put("চট্টগ্রাম", chattogram);
        divisionMap.put("রাজশাহী", rajshahi);
    }

    private void setupDropdowns() {
        cmbDivision.setItems(FXCollections.observableArrayList(divisionMap.keySet()));
        cmbSoilType.setItems(FXCollections.observableArrayList("দোআঁশ", "এঁটেল", "বেলে", "পলি"));

        cmbDivision.setOnAction(event -> {
            String division = cmbDivision.getValue();
            Map<String, List<String>> districtMap = divisionMap.get(division);
            if (districtMap == null) {
                cmbDistrict.getItems().clear();
                cmbUpazila.getItems().clear();
                return;
            }
            cmbDistrict.setItems(FXCollections.observableArrayList(districtMap.keySet()));
            cmbDistrict.getSelectionModel().clearSelection();
            cmbUpazila.getItems().clear();
            updateCompletion();
        });

        cmbDistrict.setOnAction(event -> {
            String division = cmbDivision.getValue();
            String district = cmbDistrict.getValue();
            Map<String, List<String>> districtMap = divisionMap.get(division);
            if (districtMap == null || district == null) {
                cmbUpazila.getItems().clear();
                return;
            }
            List<String> upazilas = districtMap.get(district);
            cmbUpazila.setItems(FXCollections.observableArrayList(upazilas));
            cmbUpazila.getSelectionModel().clearSelection();
            updateCompletion();
        });

        cmbUpazila.setOnAction(event -> updateCompletion());
        cmbSoilType.setOnAction(event -> updateCompletion());
    }

    private void loadUserProfile() {
        User user = SessionManager.getLoggedInUser();
        if (user == null) {
            UiAlerts.warning("সেশন শেষ", "কোনো সক্রিয় লগইন সেশন নেই। অনুগ্রহ করে আবার লগইন করুন।");
            handleLogout();
            return;
        }

        txtName.setText(user.getName());
        txtPhone.setText(user.getMobile());
        txtLandAmount.setText(user.getLandAmount());
        cmbSoilType.setValue(user.getSoilType());
        selectedImagePath = user.getProfileImagePath();

        if (user.getDivision() != null) {
            cmbDivision.setValue(user.getDivision());
            cmbDivision.fireEvent(new ActionEvent());
        }
        if (user.getDistrict() != null) {
            cmbDistrict.setValue(user.getDistrict());
            cmbDistrict.fireEvent(new ActionEvent());
        }
        if (user.getUpazila() != null) {
            cmbUpazila.setValue(user.getUpazila());
        }

        if (user.getName() != null && !user.getName().isBlank()) {
            lblUsernameInitial.setText(user.getName());
        } else {
            lblUsernameInitial.setText("User");
        }

        loadProfileImage(user.getProfileImagePath());
        updateCompletion();
    }

    @FXML
    private void handleUploadPicture() {
        FileChooser chooser = new FileChooser();
        chooser.setTitle("ছবি নির্বাচন করুন");
        chooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg"));

        File selectedFile = chooser.showOpenDialog(txtName.getScene().getWindow());
        if (selectedFile != null) {
            selectedImagePath = selectedFile.getAbsolutePath();
            loadProfileImage(selectedImagePath);
            updateCompletion();
        }
    }

    @FXML
    private void handleSaveProfile() {
        User user = SessionManager.getLoggedInUser();
        if (user == null) {
            UiAlerts.error("সংরক্ষণ ব্যর্থ", "লগইন সেশন পাওয়া যায়নি।");
            return;
        }

        user.setName(txtName.getText());
        user.setMobile(txtPhone.getText());
        user.setLandAmount(txtLandAmount.getText());
        user.setDivision(cmbDivision.getValue());
        user.setDistrict(cmbDistrict.getValue());
        user.setUpazila(cmbUpazila.getValue());
        user.setSoilType(cmbSoilType.getValue());
        user.setProfileImagePath(selectedImagePath);

        try {
            JsonDbService dbService = new JsonDbService();
            boolean updated = dbService.updateUser(user);
            if (updated) {
                SessionManager.setLoggedInUser(user);
                if (user.getName() != null && !user.getName().isBlank()) {
                    lblUsernameInitial.setText(user.getName());
                }
                lblLastSaved.setText("শেষ আপডেট: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
                updateCompletion();
                UiAlerts.info("সফল", "প্রোফাইল তথ্য সংরক্ষণ করা হয়েছে।");
            } else {
                UiAlerts.warning("সংরক্ষণ হয়নি", "ইউজার তথ্য আপডেট করা যায়নি।");
            }
        } catch (IOException e) {
            UiAlerts.errorWithCause("সংরক্ষণ ব্যর্থ", "JSON ফাইলে তথ্য লিখতে সমস্যা হয়েছে।", e);
        }
    }

    private void updateCompletion() {
        int total = 8;
        int filled = 0;

        if (!isBlank(txtName.getText())) filled++;
        if (!isBlank(txtPhone.getText())) filled++;
        if (!isBlank(cmbDivision.getValue())) filled++;
        if (!isBlank(cmbDistrict.getValue())) filled++;
        if (!isBlank(cmbUpazila.getValue())) filled++;
        if (!isBlank(cmbSoilType.getValue())) filled++;
        if (!isBlank(txtLandAmount.getText())) filled++;
        if (!isBlank(selectedImagePath)) filled++;

        double progress = (double) filled / total;
        profileCompletionBar.setProgress(progress);
        lblCompletionPct.setText(Math.round(progress * 100) + "%");
    }

    private boolean isBlank(String value) {
        return value == null || value.isBlank();
    }

    private void loadProfileImage(String imagePath) {
        if (imagePath == null || imagePath.isBlank()) {
            profileImageView.setImage(null);
            return;
        }

        File imageFile = new File(imagePath);
        if (!imageFile.exists()) {
            profileImageView.setImage(null);
            return;
        }

        profileImageView.setImage(new Image(imageFile.toURI().toString()));
    }

    @FXML
    private void handleLogout() {
        SessionManager.clearSession();
        try {
            NavigationHelper.navigateTo(txtName.getScene(), "/com/example/demo1/app/fxml/features/hello-view.fxml");
        } catch (IOException e) {
            UiAlerts.errorWithCause("লোডিং ত্রুটি", "লগইন পেজ লোড করা যায়নি।", e);
        }
    }
}
