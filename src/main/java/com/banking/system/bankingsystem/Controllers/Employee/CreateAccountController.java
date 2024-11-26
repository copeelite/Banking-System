package com.banking.system.bankingsystem.Controllers.Employee;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.util.ResourceBundle;

public class CreateAccountController implements Initializable {
    @FXML private TextField customerEmail;
    @FXML private TextField customerName;
    @FXML private PasswordField customerPassword;
    @FXML private CheckBox savingsCheck;
    @FXML private CheckBox checkingCheck;
    @FXML private TextField initialDeposit;
    @FXML private Label messageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clearForm();
    }

    @FXML
    private void handleCreateAccount() {
        if (!validateInputs()) {
            return;
        }

        try {
            messageLabel.setText("Account created successfully!");
            messageLabel.setStyle("-fx-text-fill: green;");
            clearForm();
        } catch (Exception e) {
            messageLabel.setText("Error creating account: " + e.getMessage());
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }

    private void clearForm() {
        customerEmail.clear();
        customerName.clear();
        customerPassword.clear();
        savingsCheck.setSelected(false);
        checkingCheck.setSelected(false);
        initialDeposit.clear();
        messageLabel.setText("");
    }

    private boolean validateInputs() {
        if (customerEmail.getText().trim().isEmpty()) {
            messageLabel.setText("Email is required");
            messageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }
        if (customerPassword.getText().trim().isEmpty()) {
            messageLabel.setText("Password is required");
            messageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }
        if (!savingsCheck.isSelected() && !checkingCheck.isSelected()) {
            messageLabel.setText("Please select at least one account type");
            messageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }
        try {
            if (!initialDeposit.getText().trim().isEmpty()) {
                double amount = Double.parseDouble(initialDeposit.getText().trim());
                if (amount < 0) {
                    throw new NumberFormatException();
                }
            }
        } catch (NumberFormatException e) {
            messageLabel.setText("Please enter a valid deposit amount");
            messageLabel.setStyle("-fx-text-fill: red;");
            return false;
        }
        return true;
    }
}