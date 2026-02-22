package com.example.demo1.app.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import java.io.IOException;
import java.net.URL;

public final class SceneLoader {
    private SceneLoader() {}

    public static Parent load(String path) throws IOException {
        URL url = SceneLoader.class.getResource(path);
        if (url == null) {
            throw new IOException("Resource not found: " + path);
        }
        return FXMLLoader.load(url);
    }

    public static Scene loadScene(String path, String... cssPaths) throws IOException {
        Parent root = load(path);
        Scene scene = new Scene(root);
        applyStyles(scene, cssPaths);
        return scene;
    }

    public static void applyStyles(Scene scene, String... cssPaths) {
        if (scene == null || cssPaths == null) {
            return;
        }
        for (String cssPath : cssPaths) {
            if (cssPath == null || cssPath.isBlank()) {
                continue;
            }
            URL cssUrl = SceneLoader.class.getResource(cssPath);
            if (cssUrl != null) {
                scene.getStylesheets().add(cssUrl.toExternalForm());
            }
        }
    }
}

