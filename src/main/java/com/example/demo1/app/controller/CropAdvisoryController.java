package com.example.demo1.app.controller;
import com.example.demo1.app.util.NavigationHelper;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

// Controller for the Crop Advisory page
public class CropAdvisoryController {

    @FXML private Button btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery,btnAiHelper,btnGuide, btnFertilizer, btnIrrigation, btnCropRotation;

    // Page ta load howar sathe sathe automatically ei method ta run hobe
    @FXML
    public void initialize() {
        
        // Set up navigation for sidebar buttons
        NavigationHelper.setupSidebar(btnHome, btnAdvisory, btnStorage, btnLocalManagement, btnMachinery);

        // Set up navigation for advisory buttons
        NavigationHelper.setupAdvisoryNav(btnGuide, btnFertilizer, btnIrrigation, btnCropRotation);
        if (btnAiHelper != null) {
            btnAiHelper.setOnAction(e -> System.out.println("AI Helper Feature Coming Soon..."));
        }
    }
}
