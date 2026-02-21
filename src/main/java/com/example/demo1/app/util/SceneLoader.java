package com.example.demo1.app.util;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

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
}

