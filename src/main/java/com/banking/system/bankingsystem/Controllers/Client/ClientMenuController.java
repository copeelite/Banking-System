package com.banking.system.bankingsystem.Controllers.Client;

import com.banking.system.bankingsystem.Models.Model;
import com.banking.system.bankingsystem.Views.ClientMenuOptions;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import java.net.URL;
import java.util.ResourceBundle;

public class ClientMenuController implements Initializable {
    public Button dashboard_btn;
    public Button transaction_btn;
    public Button accounts_btn;
    public Button profile_btn;
    public Button logout_btn;
    public Button report_btn;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addListeners();
    }

    private void addListeners() {
        dashboard_btn.setOnAction(e -> {
            onDashboard();
        });
        transaction_btn.setOnAction(e -> {
            onTransactions();
        });
        accounts_btn.setOnAction(e -> {
            onAccounts();
        });
    }

    private void onDashboard() {
        System.out.println("Dashboard button clicked");
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.DASHBOARD);
    }

    private void onTransactions() {
        System.out.println("Transactions");
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.TRANSACTIONS);
    }

    private void onAccounts() {
        System.out.println("Accounts");
        Model.getInstance().getViewFactory().getClientSelectedMenuItem().set(ClientMenuOptions.ACCOUNTS);
    }

    @FXML
    private void handleLogout() {
        Stage stage = (Stage) logout_btn.getScene().getWindow();
        Model.getInstance().getViewFactory().closeStage(stage);
        Model.getInstance().getViewFactory().showLoginWindow();
    }
}
