module com.example.demo1.app {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;

    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.core;
    requires com.google.gson;

    requires org.controlsfx.controls;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;
    requires com.almasb.fxgl.all;
    requires java.desktop;

    opens com.example.demo1.app to javafx.fxml;
    opens com.example.demo1.app.controller to javafx.fxml;
    opens com.example.demo1.app.ui to javafx.fxml, com.google.gson;
    opens com.example.demo1.app.ui.sidebar to javafx.fxml;
    opens com.example.demo1.app.ui.dashboard to javafx.fxml;
    opens com.example.demo1.app.ui.users to javafx.fxml;
    opens com.example.demo1.app.ui.settings to javafx.fxml;
    opens com.example.demo1.app.model to com.google.gson, com.fasterxml.jackson.databind;

    exports com.example.demo1.app;
    exports com.example.demo1.app.config;
    exports com.example.demo1.app.controller;
    exports com.example.demo1.app.ui;
    exports com.example.demo1.app.ui.sidebar;
    exports com.example.demo1.app.ui.dashboard;
    exports com.example.demo1.app.ui.users;
    exports com.example.demo1.app.ui.settings;
    exports com.example.demo1.app.service;
    exports com.example.demo1.app.model;
    exports com.example.demo1.app.repository;
    exports com.example.demo1.app.util;
}
