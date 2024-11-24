package com.banking.system.bankingsystem;

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
    public void start(Stage stage) {
        //FXMLLoader fxmlLoader = new FXMLLoader(BankApplication.class.getResource("hello-view.fxml"));
        //AppConfig.init();

//        Parent fxmlLoader = FXMLLoader.load(getClass().getResource("/fxml/signin.fxml"));
//
//        Scene scene = new Scene(fxmlLoader, AppConfig.stageWidth, AppConfig.stageHeight);
//
//        stage.setTitle(AppConfig.title);
//        stage.setResizable(AppConfig.stageResizable);
//        stage.getIcons().add(new Image(BankApplication.class.getResourceAsStream(AppConfig.icon)));
//        stage.setScene(scene);
//        stage.show();

//        ViewFactory viewFactory = new ViewFactory();
//        viewFactory.showSinginWindow();
        Model.getInstance().getViewFactory().showSinginWindow();
    }

    public static void main(String[] args) {
        launch();
    }
}