package com.example.proyekadsi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    // Sesuaikan nama database jika perlu
    private static final String URL = "jdbc:mysql://localhost:5432/proyekADSI";
    private static final String USER = "root";
    private static final String PASS = "brenda2811";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}