package com.banking.system.bankingsystem.Controllers;

import com.banking.system.bankingsystem.Models.Model;
import com.banking.system.bankingsystem.Views.AccountType;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;
public class SigninController implements Initializable {

    public ChoiceBox<AccountType> account_selector;
    public Label email_lbl;
    public TextField email_address_fld;
    public TextField password_fld;
    public Button signin_btn;
    public Label error_lbl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        account_selector.setItems(FXCollections.observableArrayList(AccountType.CLIENT, AccountType.ADMIN, AccountType.EMPLOYEE));
        account_selector.setValue(Model.getInstance().getViewFactory().getSigninAccountType());
        account_selector.valueProperty().addListener((observable -> Model.getInstance().getViewFactory().setSigninAccountType(account_selector.getValue())));
        signin_btn.setOnAction(e -> {
            onSigninButtonClick();
        });

    }

    private void onSigninButtonClick() {
        System.out.println("Signin button clicked");
        Stage stage = (Stage) error_lbl.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
//        Model.getInstance().getViewFactory().showClientWindow();
        if(Model.getInstance().getViewFactory().getSigninAccountType() == AccountType.CLIENT) {
            Model.getInstance().getViewFactory().showClientWindow();
        }
        else if(Model.getInstance().getViewFactory().getSigninAccountType() == AccountType.EMPLOYEE){
            Model.getInstance().getViewFactory().showEmployeeWindow();
        }
        else{
            Model.getInstance().getViewFactory().showAdminWindow();
        }
    }

    public void handleSignUp() {
        loadFXML("/Fxml/Signup.fxml");
    }

    private void loadFXML(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) email_address_fld.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
