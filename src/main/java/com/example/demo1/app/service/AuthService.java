package com.example.demo1.app.service;

import com.example.demo1.app.model.User;
import com.example.demo1.app.util.SessionManager;

import java.io.IOException;

public class AuthService {
    private final UserService userService;

    public AuthService() throws IOException {
        this.userService = new UserService();
    }

    public User login(String input, String password) throws IOException {
        User user = userService.authenticate(input, password);
        if (user != null) {
            SessionManager.setLoggedInUser(user);
        }
        return user;
    }

    public void logout() {
        SessionManager.clearSession();
    }
}

