package com.example.demo1.app.util;
import com.example.demo1.app.model.User;
// Ei class user login session manage kore, login user details app er jekono jayga theke access kora jabe, ekta temporary memory moto kaj kore.
public class SessionManager {
    private static User loggedInUser;// logged in user details store korar variable

    public static void setLoggedInUser(User user) {
        loggedInUser = user;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void clearSession() {
        loggedInUser = null;
    }
}
