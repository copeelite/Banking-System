package com.banking.system.bankingsystem.cof;

import com.banking.system.bankingsystem.BankApplication;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;


/**
 * @create 2024-10
 * @author FanghaoMeng
 * @description: configuration file
 */
public class AppConfig {

    /**
     * App title
     */
    public static String title = "Bank Application";
    /**
     * App icon
     */
    public static String icon = "/icon/icon.png";
    /**
     * Window width
     */
    public static int stageWidth = 1800;
    /**
     * Window Height
     */
    public static int stageHeight = 1800;
    /**
     * Allow adjustment of window size
     */
    public static boolean stageResizable = true;

    public static void init() {
        try {
            Properties properties = new Properties();
            InputStream in = BankApplication.class.getResourceAsStream("/app.properties");
            // 检查 InputStream 是否为 null
            if (in == null) {
                throw new IOException("Properties file not found: /app.properties");
            }

            properties.load(in);

            title = properties.getProperty("title");
            icon = properties.getProperty("icon");
            stageWidth = Integer.parseInt(properties.getProperty("stage.width"));
            stageHeight = Integer.parseInt(properties.getProperty("stage.height"));
            stageResizable = Boolean.parseBoolean(properties.getProperty("stage.resizable"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}


