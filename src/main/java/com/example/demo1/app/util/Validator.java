package com.example.demo1.app.util;

public final class Validator {
    private Validator() {}

    public static boolean isBlank(String value) {
        return value == null || value.isBlank();
    }
}

