package com.example.proyekadsi.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnect {
    // Sesuaikan nama database jika perlu
    private static final String URL = "jdbc:postgresql://localhost:5433/proyekADSI";
    private static final String USER = "postgres";
    private static final String PASS = "postgres";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASS);
    }
}