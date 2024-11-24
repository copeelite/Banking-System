package com.banking.system.bankingsystem.Controllers;

import com.banking.system.bankingsystem.Models.Model;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.net.URL;
import java.util.ResourceBundle;
public class SigninController implements Initializable {

    public ChoiceBox account_selector;
    public Label email_lbl;
    public TextField email_address_fld;
    public TextField password_fld;
    public Button signin_btn;
    public Label error_lbl;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        signin_btn.setOnAction(e -> {
            System.out.println("Signin button clicked");
            Model.getInstance().getViewFactory().showClientWindow();
        });

    }
}
