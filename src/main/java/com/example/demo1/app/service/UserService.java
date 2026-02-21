package com.example.demo1.app.service;

import com.example.demo1.app.model.User;

import java.io.IOException;

public class UserService {
    private final JsonDbService dbService;

    public UserService() throws IOException {
        this.dbService = new JsonDbService();
    }

    public boolean registerUser(User user) throws IOException {
        return dbService.registerUser(user);
    }

    public User authenticate(String input, String password) throws IOException {
        return dbService.loginUser(input, password);
    }
}

