package com.banking.system.bankingsystem.Models;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public static void initialize() {
        try (Connection conn = DatabaseConnection.connect()) {
            boolean tablesExist = checkIfTablesExist(conn);
            
            if (!tablesExist) {
                try {
                    executeScript(conn, "schema.sql");
                    System.out.println("数据库表创建成功");
                } catch (Exception e) {
                    System.err.println("创建数据库表失败: " + e.getMessage());
                    e.printStackTrace();
                }
            } else {
                System.out.println("数据库表已存在，无需重新创建");
            }
        } catch (Exception e) {
            System.err.println("数据库连接失败: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private static boolean checkIfTablesExist(Connection conn) throws SQLException {
        try (ResultSet rs = conn.getMetaData().getTables(null, null, "users", null)) {
            return rs.next();
        }
    }

    private static void executeScript(Connection conn, String filename) throws IOException, SQLException {
        try (InputStream is = DatabaseInitializer.class.getClassLoader()
                .getResourceAsStream("db/" + filename)) {
            if (is == null) {
                throw new IOException("找不到SQL脚本文件，请确保文件位置: /db/" + filename);
            }
            
            String script = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            System.out.println("成功读取SQL脚本内容");
            
            Statement stmt = conn.createStatement();
            for (String sql : script.split(";")) {
                if (!sql.trim().isEmpty()) {
                    try {
                        stmt.execute(sql.trim());
                        System.out.println("执行SQL成功: " + sql.trim());
                    } catch (SQLException e) {
                        System.err.println("执行SQL失败: " + sql.trim());
                        throw e;
                    }
                }
            }
        }
    }
}