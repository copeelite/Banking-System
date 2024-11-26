package com.banking.system.bankingsystem;

import com.banking.system.bankingsystem.Models.DatabaseInitializer;
import com.banking.system.bankingsystem.Models.Model;
import com.banking.system.bankingsystem.Views.ViewFactory;
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
    public void init() {
        try {
            DatabaseInitializer.initialize();
            System.out.println("Database initialization completed");
        } catch (Exception e) {
            System.err.println("Failed to initialize database");
            e.printStackTrace();
            System.exit(1);
        }
    }

    @Override
    public void start(Stage stage) {
        Model.getInstance().getViewFactory().showSinginWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}