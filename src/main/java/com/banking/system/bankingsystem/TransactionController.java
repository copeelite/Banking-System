package com.banking.system.bankingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class TransactionController {
    @FXML private Label transactionTitle;
    @FXML private TextField recipientField;
    @FXML private TextField amountField;
    @FXML private Label confirmationLabel;

    private String transactionType;

    public void setTransactionType(String type) {
        this.transactionType = type;
        switch (transactionType) {
            case "Transfer":
                transactionTitle.setText("Transfer Funds");
                recipientField.setVisible(true);
                break;
            case "Deposit":
                transactionTitle.setText("Deposit Funds");
                recipientField.setVisible(false);
                break;
            case "Withdraw":
                transactionTitle.setText("Withdraw Funds");
                recipientField.setVisible(false);
                break;
        }
    }

    @FXML
    protected void onSubmitClick() {
        String recipient = recipientField.getText();
        String amount = amountField.getText();

        if (transactionType.equals("Transfer") && recipient.isEmpty()) {
            confirmationLabel.setText("Recipient is required for transfers.");
            confirmationLabel.setVisible(true);
            return;
        }

        if (amount.isEmpty() || !amount.matches("\\d+")) {
            confirmationLabel.setText("Please enter a valid amount.");
            confirmationLabel.setVisible(true);
            return;
        }

        confirmationLabel.setText(transactionType + " successful! Amount: $" + amount + (transactionType.equals("Transfer") ? " to " + recipient : ""));
        confirmationLabel.setVisible(true);
    }

    @FXML
    protected void onBackToDashboardClick(javafx.event.ActionEvent event) {
        try {
            Parent dashboard = FXMLLoader.load(getClass().getResource("/com/banking/system/bankingsystem/dashboard-view.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(dashboard);
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
