package com.banking.system.bankingsystem.Controllers.Employee;

import com.banking.system.bankingsystem.Models.Model;
import com.banking.system.bankingsystem.Views.AdminMenuOptions;
import com.banking.system.bankingsystem.Views.EmployeeMenuOptions;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class EmployeeMenuController implements Initializable {
    @FXML
    private Button logout_btn;
    @FXML
    private Button profile_btn;
    @FXML
    private Button create_client_btn;
    @FXML private Button clients_btn;
    @FXML private Button deposit_btn;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addListeners();

        Model.getInstance().getViewFactory().getEmployeeSelectedMenuItem().set(EmployeeMenuOptions.MANAGE_ACCOUNTS);

    }
    private void addListeners() {
        profile_btn.setOnAction(event -> onProfile());
        create_client_btn.setOnAction(event -> onCreateAccount());
        clients_btn.setOnAction(event -> onClients()); 
        deposit_btn.setOnAction(event -> onDeposit());
    }
    private void onDeposit() {
        Model.getInstance().getViewFactory().getEmployeeSelectedMenuItem().set(EmployeeMenuOptions.DEPOSIT);
    }
    private void onClients() {
        Model.getInstance().getViewFactory().getEmployeeSelectedMenuItem().set(EmployeeMenuOptions.CLIENTS);
    }
    private void onProfile() {
        Model.getInstance().getViewFactory().getEmployeeSelectedMenuItem().set(EmployeeMenuOptions.PROFILE);
    }

    private void onCreateAccount() {
        Model.getInstance().getViewFactory().getEmployeeSelectedMenuItem().set(EmployeeMenuOptions.MANAGE_ACCOUNTS);
    }
    @FXML
    private void handleLogout() {
        Stage stage = (Stage) logout_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }
}
