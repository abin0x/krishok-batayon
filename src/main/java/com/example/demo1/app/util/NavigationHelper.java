package com.example.demo1.app.util;
import com.example.demo1.app.controller.MainLayoutController;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
//ei class a navigation related utility methods rakha hoi, jate code reuse o maintainability barano jai, sidebar button gulo te action assign kora, scene switch kora, o app er vitor navigate kora method gulo ekhane centralized vabe rakha hoi

public final class NavigationHelper {
    private NavigationHelper() {} // ei class private kore deya hoi jate instance create na kora jai
    private static final String HOME_FXML = "/fxml/layout/main-layout.fxml";
    private static final String ADVISORY_FXML = "/fxml/features/planning-content.fxml";
    private static final String MACHINERY_FXML = "/fxml/features/MachineryView.fxml";
    private static final String FERTILIZER_FXML = "/fxml/features/FertilizerCalculator.fxml";
    private static final String IRRIGATION_FXML = "/fxml/features/IrrigationCalculator.fxml";
    private static final String ROTATION_FXML = "/fxml/features/CropRotation.fxml";
    private static final String STORAGE_FXML = "/fxml/features/WarehouseView.fxml";
    private static final String LOCAL_FXML = "/fxml/features/LocalManagement.fxml";
    private static final String GLOBAL_CSS = "/css/dashboard.css";

    // sidebar button gulo te action assign kora method, jate click korle specific fxml file load hoi, ei method a button o tar corresponding fxml path pass kora hoi, jate easily manage kora jai
    public static void setupSidebar(Button btnHome, Button btnAdvisory, Button btnStorage, Button btnLocal, Button btnMachinery) {
        assignAction(btnHome, HOME_FXML);
        assignAction(btnAdvisory, ADVISORY_FXML);
        assignAction(btnStorage, STORAGE_FXML);
        assignAction(btnLocal, LOCAL_FXML);
        assignAction(btnMachinery, MACHINERY_FXML);
    }
    //advisory section a internal navigation setup kora method, jate advisory related button gulo te action assign kora jai, ei method a button o tar corresponding fxml path pass kora hoi, jate easily manage kora jai
    public static void setupAdvisoryNav(Button btnGuide, Button btnFertilizer, Button btnIrrigation, Button btnRotation) {
        assignAction(btnGuide, ADVISORY_FXML);
        assignAction(btnFertilizer, FERTILIZER_FXML);
        assignAction(btnIrrigation, IRRIGATION_FXML);
        assignAction(btnRotation, ROTATION_FXML);
    }
    // helper method jate button te action assign kora jai, jate click korle specific fxml file load hoi, ei method a button o tar corresponding fxml path pass kora hoi, jate easily manage kora jai
    private static void assignAction(Button btn, String fxmlPath) {
        if (btn == null) return;
        btn.setOnAction(event -> {
            try {
                navigateWithinApp(((Node) event.getSource()).getScene(), fxmlPath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
    // app er vitor navigate kora method, jate current scene theke specific fxml file load kora jai, ei method a current scene o target fxml path pass kora hoi, jate easily manage kora jai, main layout controller check kora hoi jate sidebar maintain thake, jodi main layout controller available thake tahole sidebar maintain kore view change kora hoi, na hole full scene switch kora hoi
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
    // scene switch kora method, jate event trigger hole specific fxml file load kora jai, ei method a event o target fxml path pass kora hoi, jate easily manage kora jai, title o css paths optional vabe pass kora jai, jodi title provide kora hoi tahole stage title update kora hoi, css paths provide kora hole scene e apply kora hoi
    public static void switchScene(ActionEvent event, String fxmlPath, String title, String... cssPaths) {
        try {
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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
    //ei method a current scene o target fxml path pass kora hoi, jate easily manage kora jai, main layout controller check kora hoi jate sidebar maintain thake, jodi main layout controller available thake tahole sidebar maintain kore view change kora hoi, na hole full scene switch kora hoi
    public static void navigateTo(Scene currentScene, String fxmlPath) throws IOException {
        if (currentScene == null) return;
        Stage stage = (Stage) currentScene.getWindow();
        Scene newScene = SceneLoader.loadScene(fxmlPath, GLOBAL_CSS);
        stage.setScene(newScene);
        stage.show();
    }
}
