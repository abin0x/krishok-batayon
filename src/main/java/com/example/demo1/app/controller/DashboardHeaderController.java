package com.example.demo1.app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class DashboardHeaderController {

    private static final Locale LOCALE_BN = new Locale("bn", "BD");
    private static final DateTimeFormatter DAY_FORMATTER = DateTimeFormatter.ofPattern("EEEE", LOCALE_BN);

    @FXML
    private Label lblTodayWeekday;

    @FXML
    public void initialize() {
        if (lblTodayWeekday != null) {
            String dayName = LocalDate.now().format(DAY_FORMATTER);
            lblTodayWeekday.setText("আজ " + dayName);
        }
    }
}
