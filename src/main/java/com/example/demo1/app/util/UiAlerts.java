package com.example.demo1.app.util;

import javafx.scene.control.Alert;

public final class UiAlerts {
    private UiAlerts() {}

    public static void info(String title, String message) {
        show(Alert.AlertType.INFORMATION, title, message);
    }

    public static void warning(String title, String message) {
        show(Alert.AlertType.WARNING, title, message);
    }

    public static void error(String title, String message) {
        show(Alert.AlertType.ERROR, title, message);
    }

    public static void errorWithCause(String title, String message, Throwable throwable) {
        Throwable root = throwable;
        while (root.getCause() != null) {
            root = root.getCause();
        }

        String details = root.getClass().getSimpleName();
        if (root.getMessage() != null && !root.getMessage().isBlank()) {
            details += ": " + root.getMessage();
        }
        show(Alert.AlertType.ERROR, title, message + System.lineSeparator() + details);
    }

    private static void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
