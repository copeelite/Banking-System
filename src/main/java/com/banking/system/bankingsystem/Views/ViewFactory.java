package com.banking.system.bankingsystem.Views;
import com.banking.system.bankingsystem.BankApplication;
import com.banking.system.bankingsystem.Controllers.Client.ClientController;
import com.banking.system.bankingsystem.cof.AppConfig;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import com.banking.system.bankingsystem.cof.AppConfig;

public class ViewFactory {

    //Client
    private AnchorPane dashbaordView;
    public ViewFactory() {}
    public AnchorPane getDashboardView() {
        if( dashbaordView == null) {

            try{
                dashbaordView = new FXMLLoader(getClass().getResource("/fxml/Client/Dashboard.fxml")).load();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dashbaordView;
    }

    public void showSinginWindow() {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Signin.fxml"));
            createStage(loader);
    }

    public void showClientWindow(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Client/Client.fxml"));
        ClientController clientController = new ClientController();
        loader.setController(clientController);
        createStage(loader);
    }

    private void createStage(FXMLLoader loader){
        AppConfig.init();

        Scene scene = null;

        try {
            scene = new Scene(loader.load());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        Stage stage = new Stage();
        stage.setScene(scene);
        stage.setTitle(AppConfig.title);
        stage.setResizable(AppConfig.stageResizable);
        stage.getIcons().add(new Image(BankApplication.class.getResourceAsStream(AppConfig.icon)));
        stage.show();
    }

    public void closeStage(Stage stage) {
        stage.close();
    }
}
