package com.banking.system.bankingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class DashboardController {
    @FXML private Label contentLabel;

    @FXML
    protected void onLogoutButtonClick(javafx.event.ActionEvent event) {
        try {
            Parent loginView = FXMLLoader.load(getClass().getResource("/com/banking/system/bankingsystem/login-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(loginView);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    protected void onAccountSummaryClick() {
        contentLabel.setText("Account Summary: Balance $10,000\nRecent Transactions: \n- Deposit: $500\n- Withdraw: $200");
    }

    @FXML
    protected void onTransferClick(javafx.event.ActionEvent event) {
        navigateToTransaction("Transfer", event);
    }

    @FXML
    protected void onDepositClick(javafx.event.ActionEvent event) {
        navigateToTransaction("Deposit", event);
    }

    @FXML
    protected void onWithdrawClick(javafx.event.ActionEvent event) {
        navigateToTransaction("Withdraw", event);
    }

    private void navigateToTransaction(String transactionType, javafx.event.ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/banking/system/bankingsystem/transaction-view.fxml"));
            Parent transactionView = loader.load();

            TransactionController controller = loader.getController();
            controller.setTransactionType(transactionType);

            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(transactionView);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
