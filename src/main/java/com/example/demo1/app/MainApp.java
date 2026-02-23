package com.example.demo1.app;
import com.example.demo1.app.config.AppConfig;
import com.example.demo1.app.util.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
//main class ja javafx er appilication class k inherit kore,ar avabei kuno desktop app banate hole main class k avabei application theke extended korte hobe
public class MainApp extends Application {

    // start method override kore, jekhane amra main layout load korbo, scene set korbo, and stage show korbo
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(Constants.MAIN_LAYOUT_FXML));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(MainApp.class.getResource("/css/dashboard.css").toExternalForm());

        stage.setTitle(AppConfig.APP_TITLE);
        stage.setScene(scene);
        stage.setWidth(AppConfig.DEFAULT_WIDTH);
        stage.setHeight(AppConfig.DEFAULT_HEIGHT);
        stage.setMinWidth(1200);
        stage.setMinHeight(700);
        stage.show();// stage show korbe
    }

   // Java er main method, jeta program run korlei prothome call hoy, ekhane amra launch(args) call kore JavaFX application start korbo
    public static void main(String[] args) {
        launch(args); // JavaFX application ta ehan theke launch/start kora hocche
    }
}
