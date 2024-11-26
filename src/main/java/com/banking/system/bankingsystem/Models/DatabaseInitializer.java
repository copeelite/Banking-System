package com.banking.system.bankingsystem.Models;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        try (Connection conn = DatabaseConnection.connect()) {
            try {
                executeScript(conn, "schema.sql");
                executeScript(conn, "initial_data.sql");
                System.out.println("Database check completed.");
            } catch (Exception e) {
                System.out.println("Database already initialized. Deleted to create.");
            }
        } catch (Exception e) {
            System.err.println("Database connection failed.");
        }
    }

    private static void executeScript(Connection conn, String filename) throws IOException, SQLException {
        String script = new String(Files.readAllBytes(
            Paths.get("src/main/resources/db/" + filename)));
            
        Statement stmt = conn.createStatement();
        for (String sql : script.split(";")) {
            if (!sql.trim().isEmpty()) {
                stmt.execute(sql.trim());
            }
        }
    }
}