package com.example.demo1.app.util;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public final class NavigationHelper {
    private NavigationHelper() {
    }

    private static final String HOME_FXML = "/com/example/demo1/app/fxml/layout/main-layout.fxml";
    private static final String ADVISORY_FXML = "/com/example/demo1/app/fxml/features/CropAdvisory.fxml";
    private static final String MACHINERY_FXML = "/com/example/demo1/app/fxml/features/MachineryView.fxml";
    private static final String FERTILIZER_FXML = "/com/example/demo1/app/fxml/features/FertilizerCalculator.fxml";
    private static final String IRRIGATION_FXML = "/com/example/demo1/app/fxml/features/IrrigationCalculator.fxml";
    private static final String ROTATION_FXML = "/com/example/demo1/app/fxml/features/CropRotation.fxml";
    private static final String STORAGE_FXML = "/com/example/demo1/app/fxml/features/WarehouseView.fxml";
    private static final String LOCAL_FXML = "/com/example/demo1/app/fxml/features/LocalManagement.fxml";

    public static void setupSidebar(Button btnHome, Button btnAdvisory, Button btnStorage, Button btnLocal, Button btnMachinery) {
        assignAction(btnHome, HOME_FXML);
        assignAction(btnAdvisory, ADVISORY_FXML);
        assignAction(btnStorage, STORAGE_FXML);
        assignAction(btnLocal, LOCAL_FXML);
        assignAction(btnMachinery, MACHINERY_FXML);
    }

    public static void setupAdvisoryNav(Button btnGuide, Button btnFertilizer, Button btnIrrigation, Button btnRotation) {
        assignAction(btnGuide, ADVISORY_FXML);
        assignAction(btnFertilizer, FERTILIZER_FXML);
        assignAction(btnIrrigation, IRRIGATION_FXML);
        assignAction(btnRotation, ROTATION_FXML);
    }

    private static void assignAction(Button btn, String fxmlPath) {
        if (btn != null) {
            btn.setOnAction(event -> switchScene(event, fxmlPath));
        }
    }

    public static void switchScene(ActionEvent event, String fxmlPath) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();

            FXMLLoader loader = new FXMLLoader(NavigationHelper.class.getResource(fxmlPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);

            URL globalCssUrl = NavigationHelper.class.getResource("/com/example/demo1/app/css/dashboard.css");
            if (globalCssUrl != null) {
                scene.getStylesheets().add(globalCssUrl.toExternalForm());
            }

            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void navigateTo(Scene currentScene, String fxmlPath) throws IOException {
        Stage stage = (Stage) currentScene.getWindow();
        FXMLLoader loader = new FXMLLoader(NavigationHelper.class.getResource(fxmlPath));
        Parent root = loader.load();
        Scene newScene = new Scene(root);

        URL globalCssUrl = NavigationHelper.class.getResource("/com/example/demo1/app/css/dashboard.css");
        if (globalCssUrl != null) {
            newScene.getStylesheets().add(globalCssUrl.toExternalForm());
        }

        stage.setScene(newScene);
        stage.show();
    }
}
