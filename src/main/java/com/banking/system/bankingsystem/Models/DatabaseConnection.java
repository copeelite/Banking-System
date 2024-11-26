package com.banking.system.bankingsystem.Models;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    // the database file will be created in the project root directory
    private static final String DB_URL = "jdbc:sqlite:banking_system.db";
    
    public static Connection connect() {
        Connection conn = null;
        try {
            // ensure the driver is loaded
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Database connection established");
        } catch (SQLException | ClassNotFoundException e) {
            System.err.println("Database connection failed: " + e.getMessage());
        }
        return conn;
    }
}