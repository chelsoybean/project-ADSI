package com.example.proyekadsi.util;

import com.example.proyekadsi.model.AkunPengguna;

public class SessionManager {
    private static AkunPengguna loggedInUser;

    public static void setLoggedInUser(AkunPengguna user) {
        loggedInUser = user;
    }

    public static AkunPengguna getLoggedInUser() {
        return loggedInUser;
    }
}