package com.example.demo1.app.controller;

import com.example.demo1.app.util.NavigationHelper;
import com.example.demo1.app.util.UiAlerts;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.Map;

public class PlanningContentController {

    @FXML private VBox extraCardsContainer;//atar moddher card gula ke show/hide kora thake
    @FXML private Button btnShowMore;

    private boolean expanded;//show more button er state track korar jonno


    // Crop details data - in a real application, this would likely come from a database or API
    private static final Map<String, String> CROP_DETAILS = Map.of(
            "rice", "Crop: Rice\n- Planting: April-June, November-December\n- Irrigation: every 5-7 days\n- Yield: 40-50 mon/acre",
            "wheat", "Crop: Wheat\n- Planting: November-December\n- Irrigation: every 12-15 days\n- Yield: 30-35 mon/acre",
            "potato", "Crop: Potato\n- Planting: October-November\n- Seed tuber: 600-800 kg/acre\n- Yield: 150-200 mon/acre",
            "jute", "Crop: Jute\n- Planting: March-April\n- Seed: 2-3 kg/acre\n- Yield: 25-30 mon/acre",
            "maize", "Crop: Maize\n- Planting: February-March\n- Seed: 8-10 kg/acre\n- Yield: 60-70 mon/acre",
            "mustard", "Crop: Mustard\n- Planting: October-November\n- Seed: 2-3 kg/acre\n- Yield: 12-15 mon/acre",
            "lentil", "Crop: Lentil\n- Planting: November\n- Seed: 12-15 kg/acre\n- Yield: 8-10 mon/acre",
            "chili", "Crop: Chili\n- Planting: almost year-round\n- Spacing: 18-24 inches\n- Yield: 40-60 mon/acre",
            "onion", "Crop: Onion\n- Planting: November-December\n- Transplant: after 4-6 weeks\n- Yield: 80-100 mon/acre",
            "tomato", "Crop: Tomato\n- Planting: October-December\n- Irrigation: every 6-8 days\n- Yield: 120-160 mon/acre"
    );

    @FXML
    private void initialize() {
        boolean needsShowMore = extraCardsContainer != null && !extraCardsContainer.getChildren().isEmpty();//extra cards thakle show more button dekhabe, na thakle hide kore dibe
        if (btnShowMore != null) {
            btnShowMore.setVisible(needsShowMore);
            btnShowMore.setManaged(needsShowMore);
        }
        setExtraCardsVisible(false);
    }

    @FXML//show more button click handler
    private void toggleShowMore() {
        expanded = !expanded;
        setExtraCardsVisible(expanded);//extra cards show/hide kore button text update kore dibe
    }


    //extra cards show/hide kore button text update kore dibe
    private void setExtraCardsVisible(boolean visible) {
        if (extraCardsContainer == null || btnShowMore == null) {
            return;
        }
        extraCardsContainer.setVisible(visible);
        extraCardsContainer.setManaged(visible);
        btnShowMore.setText(visible ? "Show Less" : "Show More");//button text update kore dibe show more thakle show less dekhabe, show less thakle show more dekhabe
    }

    @FXML//crop card click handler - crop details alert show kore dibe
    private void showCropDetails(ActionEvent event) {
        if (!(event.getSource() instanceof Button source)) {//event source button na hole return kore dibe
            return;
        }

        String key = source.getUserData() instanceof String k ? k : "";//button er user data theke crop key niye asbe, jodi user data string na hoye thake tahole empty string nibe
        String details = CROP_DETAILS.getOrDefault(key, "Detailed information for this crop will be added soon.");//crop key theke crop details niye asbe, jodi crop key na thake tahole default message nibe

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Crop Details");
        alert.setHeaderText("Guidance");
        alert.setContentText(details);
        alert.setResizable(true);
        alert.getDialogPane().setPrefWidth(560);
        alert.showAndWait();
    }

    @FXML//fertilizer calculator card click handler - fertilizer calculator page open kore dibe
    private void openFertilizerPage(ActionEvent event) {
        openPage(event, "/fxml/features/FertilizerCalculator.fxml", "Could not load Fertilizer Calculator page.");
    }

    @FXML//irrigation calculator card click handler - irrigation calculator page open kore dibe
    private void openIrrigationPage(ActionEvent event) {
        openPage(event, "/fxml/features/IrrigationCalculator.fxml", "Could not load Irrigation Calculator page.");
    }

    //crop calendar card click handler - crop calendar page open kore dibe
    private void openPage(ActionEvent event, String fxmlPath, String errorMessage) {
        try {
            Node source = (Node) event.getSource();
            NavigationHelper.navigateWithinApp(source.getScene(), fxmlPath);
        } catch (IOException e) {
            UiAlerts.errorWithCause("Navigation Error", errorMessage, e);
        }
    }
}