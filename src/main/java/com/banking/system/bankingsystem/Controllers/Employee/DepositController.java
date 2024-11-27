package com.banking.system.bankingsystem.Controllers.Employee;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import java.net.URL;
import java.sql.*;
import java.util.ResourceBundle;

import com.banking.system.bankingsystem.Models.DatabaseConnection;

public class DepositController implements Initializable {
    @FXML private TextField clientEmail;
    @FXML private RadioButton savingsRadio;
    @FXML private RadioButton checkingRadio;
    @FXML private TextField depositAmount;
    @FXML private TextField description;
    @FXML private Label messageLabel;
    
    private ToggleGroup accountTypeGroup;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        accountTypeGroup = new ToggleGroup();
        savingsRadio.setToggleGroup(accountTypeGroup);
        checkingRadio.setToggleGroup(accountTypeGroup);
        clearForm();
    }

    @FXML
    private void handleDeposit() {
        if (!validateInputs()) {
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {
            conn.setAutoCommit(false);
            try {
                if (!userExists(conn, clientEmail.getText().trim())) {
                    showError("Client does not exist!");
                    return;
                }

                String accountType = savingsRadio.isSelected() ? "SAVINGS" : "CHECKING";
                int accountId = getAccountId(conn, clientEmail.getText().trim(), accountType);
                
                if (accountId == -1) {
                    showError("Client does not have a " + accountType + " account!");
                    return;
                }

                double amount = Double.parseDouble(depositAmount.getText().trim());
                processDeposit(conn, accountId, amount);

                conn.commit();
                showSuccess("Deposit processed successfully!");
                clearForm();
                
            } catch (Exception e) {
                conn.rollback();
                showError("Failed to process deposit: " + e.getMessage());
            }
        } catch (SQLException e) {
            showError("Database connection failed");
        }
    }

    private boolean validateInputs() {
        if (clientEmail.getText().trim().isEmpty()) {
            showError("Client email is required");
            return false;
        }
        if (!savingsRadio.isSelected() && !checkingRadio.isSelected()) {
            showError("Please select an account type");
            return false;
        }
        try {
            double amount = Double.parseDouble(depositAmount.getText().trim());
            if (amount <= 0) {
                throw new NumberFormatException();
            }
        } catch (NumberFormatException e) {
            showError("Please enter a valid deposit amount");
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

    private int getAccountId(Connection conn, String email, String accountType) throws SQLException {
        String sql = "SELECT a.account_id FROM accounts a " +
                    "JOIN users u ON a.customer_id = u.user_id " +
                    "WHERE u.email = ? AND a.account_type = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            pstmt.setString(2, accountType);
            ResultSet rs = pstmt.executeQuery();
            return rs.next() ? rs.getInt("account_id") : -1;
        }
    }

    private void processDeposit(Connection conn, int accountId, double amount) throws SQLException {
        // Update account balance
        String updateSql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateSql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountId);
            pstmt.executeUpdate();
        }

        // Record transaction
        String transSql = "INSERT INTO transactions (account_id, transaction_type, amount, description) " +
                         "VALUES (?, 'DEPOSIT', ?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(transSql)) {
            pstmt.setInt(1, accountId);
            pstmt.setDouble(2, amount);
            pstmt.setString(3, description.getText().trim().isEmpty() ? 
                          "Cash Deposit" : description.getText().trim());
            pstmt.executeUpdate();
        }
    }

    @FXML
    private void handleClear() {
        clearForm();
    }

    private void clearForm() {
        clientEmail.clear();
        savingsRadio.setSelected(false);
        checkingRadio.setSelected(false);
        depositAmount.clear();
        description.clear();
        messageLabel.setText("");
    }

    private void showError(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: red;");
    }

    private void showSuccess(String message) {
        messageLabel.setText(message);
        messageLabel.setStyle("-fx-text-fill: green;");
    }
}