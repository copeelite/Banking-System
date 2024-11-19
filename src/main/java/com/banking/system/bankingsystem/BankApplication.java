package com.banking.system.bankingsystem;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import com.banking.system.bankingsystem.cof.AppConfig;

public class BankApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        // Initialize configuration
        AppConfig.init();

        // Load the login screen as the initial view
        Parent loginView = FXMLLoader.load(getClass().getResource("login-view.fxml"));

        // Create the scene
        Scene scene = new Scene(loginView, AppConfig.stageWidth, AppConfig.stageHeight);

        // Configure the stage
        stage.setTitle(AppConfig.title);
        stage.setResizable(AppConfig.stageResizable);
        stage.getIcons().add(new Image(BankApplication.class.getResourceAsStream(AppConfig.icon)));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
