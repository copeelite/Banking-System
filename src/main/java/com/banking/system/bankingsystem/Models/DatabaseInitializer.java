package com.banking.system.bankingsystem.Models;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        try (Connection conn = DatabaseConnection.connect()) {
            try {
                executeScript(conn, "schema.sql");
                //executeScript(conn, "initial_data.sql");
                System.out.println("Database check completed.");
            } catch (Exception e) {
                System.out.println("Database already initialized. Deleted to create.");
            }
        } catch (Exception e) {
            System.err.println("Database connection failed.");
        }
    }

    private static void executeScript(Connection conn, String filename) throws IOException, SQLException {
        URL resource = DatabaseInitializer.class.getResource("/db/" + filename);
        if (resource == null) {
            System.err.println("File not found: /db/" + filename);
            throw new IOException("SQL script file not found, please ensure file exists at: /db/" + filename);
        }

        System.out.println("File path found: " + resource.getPath());
        
        try (InputStream is = resource.openStream()) {
            String script = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            Statement stmt = conn.createStatement();
            
            for (String sql : script.split(";")) {
                if (!sql.trim().isEmpty()) {
                    stmt.execute(sql.trim());
                }
            }
        }
    }
}