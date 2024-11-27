package com.banking.system.bankingsystem.Controllers.Admin;

import com.banking.system.bankingsystem.Models.Model;
import com.banking.system.bankingsystem.Views.AdminMenuOptions;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AdminMenuController implements Initializable {
    
    @FXML
    private Button create_client_btn;
    @FXML
    private Button clients_btn;
    @FXML
    private Button deposit_btn;
    @FXML
    private Button manage_users_btn;
    @FXML
    private Button manage_accounts_btn;
    @FXML
    private Button generate_reports_btn;
    @FXML
    private Button approve_requests_btn;
    @FXML
    private Button profile_btn;
    @FXML
    private Button logout_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addListeners();
    }

    private void addListeners() {
        create_client_btn.setOnAction(event -> onCreateClient());
        clients_btn.setOnAction(event -> onClients());
        deposit_btn.setOnAction(event -> onDeposit());
        manage_users_btn.setOnAction(event -> onManageUsers());
        manage_accounts_btn.setOnAction(event -> onManageAccounts());
        generate_reports_btn.setOnAction(event -> onGenerateReports());
        approve_requests_btn.setOnAction(event -> onApproveRequests());
    }

    private void onManageUsers() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.MANAGE_USERS);
    }

    private void onManageAccounts() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.MANAGE_ACCOUNTS);
    }

    private void onGenerateReports() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.GENERATE_REPORTS);
    }

    private void onApproveRequests() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.APPROVE_REQUESTS);
    }

    private void onClients() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CLIENTS);
    }

    private void onDeposit() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.DEPOSIT);
    }

    private void onProfile() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.PROFILE);
    }

    @FXML
    private void handleLogout() {
        Stage stage = (Stage) logout_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }

    private void onCreateClient() {
        Model.getInstance().getViewFactory().getAdminSelectedMenuItem().set(AdminMenuOptions.CREATE_CLIENT);
    }
}