package com.example.demo1.app.util;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

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


    public static boolean confirm(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    public static void infoWithHeader(String title, String header, String message, double width) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(header);
        alert.setContentText(message);
        if (width > 0) {
            alert.setResizable(true);
            alert.getDialogPane().setPrefWidth(width);
        }
        alert.showAndWait();
    }
    private static void show(Alert.AlertType type, String title, String message) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
