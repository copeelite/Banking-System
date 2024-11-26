package com.banking.system.bankingsystem.Controllers.Employee;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.banking.system.bankingsystem.Models.DatabaseConnection;

public class CreateAccountController implements Initializable {
    @FXML private TextField customerEmail;
    @FXML private RadioButton savingsCheck;
    @FXML private RadioButton checkingCheck;
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

        try (Connection conn = DatabaseConnection.connect()) {
            conn.setAutoCommit(false);
            try {
                if (!userExists(conn, customerEmail.getText().trim())) {
                    messageLabel.setText("User does not exist, please confirm the email address!");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }

                String accountType = savingsCheck.isSelected() ? "SAVINGS" : "CHECKING";
                
                if (hasAccountType(conn, customerEmail.getText().trim(), accountType)) {
                    messageLabel.setText("User already has a " + accountType + " account!");
                    messageLabel.setStyle("-fx-text-fill: red;");
                    return;
                }

                createAccount(conn, accountType);

                if (!initialDeposit.getText().trim().isEmpty()) {
                    double amount = Double.parseDouble(initialDeposit.getText().trim());
                    if (amount > 0) {
                        processInitialDeposit(conn);
                    }
                }

                conn.commit();
                showSuccessAlert();
                clearForm();
                
            } catch (Exception e) {
                conn.rollback();
                messageLabel.setText("Failed to create account: " + e.getMessage());
                messageLabel.setStyle("-fx-text-fill: red;");
            }
        } catch (Exception e) {
            messageLabel.setText("Database connection failed");
            messageLabel.setStyle("-fx-text-fill: red;");
        }
    }

    @FXML
    private void handleClearForm() {
        clearForm();
    }

    private void clearForm() {
        customerEmail.clear();
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

    private boolean userExists(Connection conn, String email) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND role = 'CLIENT'";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        }
    }

    private void createAccount(Connection conn, String accountType) throws SQLException {
        String accountNumber = generateAccountNumber();
        String sql = "INSERT INTO accounts (customer_id, account_type, account_number, balance, created_at) " +
                    "SELECT user_id, ?, ?, 0.00, datetime('now') FROM users WHERE email = ?";
        
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, accountType);
        pstmt.setString(2, accountNumber);
        pstmt.setString(3, customerEmail.getText().trim());
        pstmt.executeUpdate();
    }

    private String generateAccountNumber() {
        return String.format("%016d", (long)(Math.random() * 10000000000000000L));
    }

    private void processInitialDeposit(Connection conn) throws SQLException {
        double amount = Double.parseDouble(initialDeposit.getText().trim());
        String accountType = checkingCheck.isSelected() ? "CHECKING" : "SAVINGS";
        
        
        String updateSql = "UPDATE accounts SET balance = balance + ? " +
                          "WHERE customer_id = (SELECT user_id FROM users WHERE email = ?) " +
                          "AND account_type = ?";
        PreparedStatement updateStmt = conn.prepareStatement(updateSql);
        updateStmt.setDouble(1, amount);
        updateStmt.setString(2, customerEmail.getText().trim());
        updateStmt.setString(3, accountType);
        updateStmt.executeUpdate();

        String transSql = "INSERT INTO transactions (account_id, transaction_type, amount, description) " +
                         "SELECT account_id, 'DEPOSIT', ?, 'Initial Deposit' FROM accounts " +
                         "WHERE customer_id = (SELECT user_id FROM users WHERE email = ?) " +
                         "AND account_type = ?";
        PreparedStatement transStmt = conn.prepareStatement(transSql);
        transStmt.setDouble(1, amount);
        transStmt.setString(2, customerEmail.getText().trim());
        transStmt.setString(3, accountType);
        transStmt.executeUpdate();
    }

    private boolean hasAccountType(Connection conn, String email, String accountType) throws SQLException {
        String sql = "SELECT COUNT(*) FROM accounts a " +
                    "JOIN users u ON a.customer_id = u.user_id " +
                    "WHERE u.email = ? AND a.account_type = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, accountType);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        }
    }

    private void showSuccessAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Account created successfully!");
        alert.showAndWait();
    }
}