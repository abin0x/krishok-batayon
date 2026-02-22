package com.example.demo1.app.util;

import com.example.demo1.app.controller.MainLayoutController;
import com.example.demo1.app.util.SceneLoader;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.io.IOException;

public final class NavigationHelper {
    private NavigationHelper() {
    }

    private static final String HOME_FXML = "/fxml/layout/main-layout.fxml";
    private static final String ADVISORY_FXML = "/fxml/features/planning-content.fxml";
    private static final String MACHINERY_FXML = "/fxml/features/MachineryView.fxml";
    private static final String FERTILIZER_FXML = "/fxml/features/FertilizerCalculator.fxml";
    private static final String IRRIGATION_FXML = "/fxml/features/IrrigationCalculator.fxml";
    private static final String ROTATION_FXML = "/fxml/features/CropRotation.fxml";
    private static final String STORAGE_FXML = "/fxml/features/WarehouseView.fxml";
    private static final String LOCAL_FXML = "/fxml/features/LocalManagement.fxml";
    private static final String GLOBAL_CSS = "/css/dashboard.css";

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
            btn.setOnAction(event -> {
                try {
                    Node source = (Node) event.getSource();
                    navigateWithinApp(source.getScene(), fxmlPath);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }

    public static void navigateWithinApp(Scene currentScene, String fxmlPath) throws IOException {
        MainLayoutController mainLayoutController = MainLayoutController.getInstance();
        if (mainLayoutController != null && mainLayoutController.loadView(fxmlPath)) {
            return;
        }
        navigateTo(currentScene, fxmlPath);
    }

    public static void switchScene(ActionEvent event, String fxmlPath) {
        switchScene(event, fxmlPath, null, GLOBAL_CSS);
    }

    public static void switchScene(ActionEvent event, String fxmlPath, String title, String... cssPaths) {
        try {
            Node source = (Node) event.getSource();
            Stage stage = (Stage) source.getScene().getWindow();
            Scene scene = SceneLoader.loadScene(fxmlPath, cssPaths);
            if (title != null && !title.isBlank()) {
                stage.setTitle(title);
            }
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void navigateTo(Scene currentScene, String fxmlPath) throws IOException {
        Stage stage = (Stage) currentScene.getWindow();
        Scene newScene = SceneLoader.loadScene(fxmlPath, GLOBAL_CSS);
        stage.setScene(newScene);
        stage.show();
    }
}
