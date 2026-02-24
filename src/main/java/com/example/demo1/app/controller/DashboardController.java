package com.example.demo1.app.controller;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DashboardController {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("EEEE", new Locale("bn", "BD"));

    @FXML
    private Label lblTodayDate,lblTodayWeekday;

    @FXML// ei method ta page er scene asar sathe sathe call hobe
    public void initialize() {
        LocalDate today = LocalDate.now();// ajker date ta niye asbe
        lblTodayDate.setText(today.format(DATE_FORMATTER));
        lblTodayWeekday.setText("আজ " + today.format(DAY_FORMATTER));
    }
}

