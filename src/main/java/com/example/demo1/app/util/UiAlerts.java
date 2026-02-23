package com.example.demo1.app.util;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
// Utility class for showing various types of alerts in JavaFX applications.
public final class UiAlerts 
{
    private UiAlerts() {}

    // sadaronto kuno info type alert  like "Operation successful" or "Data saved" dekhanur jonno 
    public static void info(String title, String message) 
    {
        show(Alert.AlertType.INFORMATION, title, message);
    }

    // sadaronto kuno warning type alert like "Low disk space" or "Unsaved changes" dekhanur jonno
    public static void warning(String title, String message) 
    {
        show(Alert.AlertType.WARNING, title, message);
    }

     // sadaronto kuno error type alert like "Failed to load file" or "An unexpected error occurred" dekhanur jonno
    public static void error(String title, String message)
    {
        show(Alert.AlertType.ERROR, title, message);
    }

    // error alert dekhanur jonno jekhane exception er details o dekhano hobe, like "Failed to load file: FileNotFoundException: config.txt not found" dekhanur jonno
    public static void errorWithCause(String title, String message, Throwable throwable) 
    {
        Throwable root = throwable;
        while (root.getCause() != null) // root cause ber korar jonno loop
        {
            root = root.getCause();
        }
        String details = root.getClass().getSimpleName();// exception er class name ta details hishebe nibe
        if (root.getMessage() != null && !root.getMessage().isBlank()) {
            details += ": " + root.getMessage();
        }
        show(Alert.AlertType.ERROR, title, message + System.lineSeparator() + details);
    }

    // confirmation alert dekhanur jonno jekhane user ke yes or no option deya hobe, like "Are you sure you want to delete this file?" dekhanur jonno
    public static boolean confirm(String title, String message) 
    {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.YES, ButtonType.NO);
        alert.setTitle(title);
        alert.setHeaderText(null);
        return alert.showAndWait().orElse(ButtonType.NO) == ButtonType.YES;
    }

    // info alert dekhanur jonno jekhane header text o dekhano hobe, like "Update Available" title, "Version 2.0 is now available!" header, and "Please update to the latest version for new features and improvements." message dekhanur jonno
    public static void infoWithHeader(String title, String header, String message, double width) 
    {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);// alert er title set korbe
        alert.setHeaderText(header);// alert er header text set korbe
        alert.setContentText(message);// alert er content text set korbe
        if (width > 0) {
            alert.setResizable(true);
            alert.getDialogPane().setPrefWidth(width);
        }
        alert.showAndWait();
    }

    // ei method ta private, karon eta sudhu UiAlerts class er modhye use hobe, ar eta alert create kore show korbe, type, title, ar message parameter hishebe nibe
    private static void show(Alert.AlertType type, String title, String message) 
    {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
