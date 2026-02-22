package com.example.demo1.app;

import com.example.demo1.app.config.AppConfig;
import com.example.demo1.app.util.Constants;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(MainApp.class.getResource(Constants.MAIN_LAYOUT_FXML));
        Scene scene = new Scene(loader.load());
        scene.getStylesheets().add(MainApp.class.getResource("/css/dashboard.css").toExternalForm());
        scene.getStylesheets().add(MainApp.class.getResource("/css/dashboard.css").toExternalForm());

        stage.setTitle(AppConfig.APP_TITLE);
        stage.setScene(scene);
        stage.setWidth(AppConfig.DEFAULT_WIDTH);
        stage.setHeight(AppConfig.DEFAULT_HEIGHT);
        stage.setMinWidth(1200);
        stage.setMinHeight(700);
        stage.show();
    }
    // fdjf 

    public static void main(String[] args) {
        launch(args);
    }
}
