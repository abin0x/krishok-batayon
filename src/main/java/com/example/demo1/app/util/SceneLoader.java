package com.example.demo1.app.util;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import java.io.IOException;
import java.net.URL;
// ei class ar madhome amra fxml file load korte parbo, tar sathe css o apply korte parbo
public final class SceneLoader {
    private SceneLoader() {}

    // fxml file load korar method, jekhane path parameter hobe fxml file er location
    public static Parent load(String path) throws IOException {
        URL url = SceneLoader.class.getResource(path);
        if (url == null) {
            throw new IOException("Resource not found: " + path);
        }
        return FXMLLoader.load(url);
    }

// fxml file load korar method, jekhane path parameter hobe fxml file er location, ar cssPaths parameter hobe css file er location
    public static Scene loadScene(String path, String... cssPaths) throws IOException {
        Parent root = load(path);// fxml file load kore root node pawa jabe
        Scene scene = new Scene(root);// scene create kora jabe root node diye
        applyStyles(scene, cssPaths);
        return scene;
    }
    
    // css file apply korar method, jekhane scene parameter hobe scene object, ar cssPaths parameter hobe css file er location
    public static void applyStyles(Scene scene, String... cssPaths) {
        if (scene == null || cssPaths == null) {
            return;
        }
        for (String cssPath : cssPaths) {
            if (cssPath == null || cssPath.isBlank()) {
                continue;
            }
            URL cssUrl = SceneLoader.class.getResource(cssPath);// css file er url pawa jabe
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
        }
    }
}

