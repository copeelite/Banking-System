package com.banking.system.bankingsystem.Controllers.Client;

import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import com.banking.system.bankingsystem.Models.DatabaseConnection;
import com.banking.system.bankingsystem.Models.Model;

public class AccountsController implements Initializable {
    public Label ch_acc_num;
    public Label transaction_limit;
    public Label ch_acc_date;
    public Label ch_acc_bal;
    public Label sv_acc_num;
    public Label withdrawal_limit;
    public Label sv_acc_date;
    public Label sv_acc_bal;
    public TextField amount_to_sv;
    public Button trans_to_sv_btn;
    public TextField amount_to_ch;
    public Button trans_to_ch_btn;

    public TextField ch_withdrawal_amount;  
    public TextField sv_withdrawal_amount;  


    public Button ch_withdrawal_btn;  
    public Button sv_withdrawal_btn; 

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAccountData();

        trans_to_sv_btn.setOnAction(event -> handleTransferToSavings());
        trans_to_ch_btn.setOnAction(event -> handleTransferToChecking());

        ch_withdrawal_btn.setOnAction(event -> handleWithdrawal("CHECKING"));
        sv_withdrawal_btn.setOnAction(event -> handleWithdrawal("SAVINGS"));
    }

    private void handleTransferToSavings() {
        try {
            double amount = Double.parseDouble(amount_to_sv.getText().trim());
            processInternalTransfer("CHECKING", "SAVINGS", amount);
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        }
    }

    private void handleTransferToChecking() {
        try {
            double amount = Double.parseDouble(amount_to_ch.getText().trim());
            processInternalTransfer("SAVINGS", "CHECKING", amount);
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        }
    }

    private void processInternalTransfer(String fromType, String toType, double amount) {
        try (Connection conn = DatabaseConnection.connect()) {
            conn.setAutoCommit(false);
            try {
                int userId = getUserId(conn, Model.getInstance().getCurrentUserEmail());
                int fromAccountId = getAccountId(conn, userId, fromType);
                int toAccountId = getAccountId(conn, userId, toType);

                double fromBalance = getAccountBalance(conn, fromAccountId);
                if (fromBalance < amount) {
                    showAlert("Error", "Insufficient balance");
                    return;
                }

                updateBalance(conn, fromAccountId, -amount);
                updateBalance(conn, toAccountId, amount);

                recordTransaction(conn, fromAccountId, "INTERNAL_TRANSFER", amount,
                        String.format("Transfer from %s to %s", fromType, toType));

                conn.commit();
                loadAccountData();
                showAlert("Success", "Transfer successful");

            } catch (Exception e) {
                conn.rollback();
                showAlert("Error", "Transfer failed: " + e.getMessage());
            }
        } catch (SQLException e) {
            showAlert("Error", "Database connection failed");
        }
    }

    private void handleWithdrawal(String accountType) {
        TextField amountField = accountType.equals("CHECKING") ? ch_withdrawal_amount : sv_withdrawal_amount;
        try {
            double amount = Double.parseDouble(amountField.getText().trim());
            processWithdrawal(accountType, amount);
        } catch (NumberFormatException e) {
            showAlert("Error", "Please enter a valid amount");
        }
    }

    private void processWithdrawal(String accountType, double amount) {
        try (Connection conn = DatabaseConnection.connect()) {
            conn.setAutoCommit(false);
            try {
                int userId = getUserId(conn, Model.getInstance().getCurrentUserEmail());
                int accountId = getAccountId(conn, userId, accountType);

                double balance = getAccountBalance(conn, accountId);
                if (balance < amount) {
                    showAlert("Error", "Insufficient balance");
                    return;
                }

                updateBalance(conn, accountId, -amount);

                recordTransaction(conn, accountId, "WITHDRAWAL", -amount,
                        String.format("Withdrawal from %s account", accountType));

                conn.commit();
                loadAccountData();
                showAlert("Success", "Withdrawal successful");

            } catch (Exception e) {
                conn.rollback();
                showAlert("Error", "Withdrawal failed: " + e.getMessage());
            }
        } catch (SQLException e) {
            showAlert("Error", "Database connection failed");
        }
    }

    private int getUserId(Connection conn, String email) throws SQLException {
        String sql = "SELECT user_id FROM users WHERE email = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, email);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("user_id");
            }
            throw new SQLException("User not found");
        }
    }

    private int getAccountId(Connection conn, int userId, String accountType) throws SQLException {
        String sql = "SELECT account_id FROM accounts WHERE customer_id = ? AND account_type = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, accountType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("account_id");
            }
            throw new SQLException("Account not found");
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void loadAccountData() {
        try (Connection conn = DatabaseConnection.connect()) {
            int userId = getUserId(conn, Model.getInstance().getCurrentUserEmail());

            loadAccountDetails(conn, userId, "CHECKING", ch_acc_num, ch_acc_bal, ch_acc_date);

            loadAccountDetails(conn, userId, "SAVINGS", sv_acc_num, sv_acc_bal, sv_acc_date);

        } catch (SQLException e) {
            showAlert("Error", "Failed to load account information");
        }
    }

    private void loadAccountDetails(Connection conn, int userId, String accountType,
            Label accNumLabel, Label balLabel, Label dateLabel) throws SQLException {
        String sql = "SELECT account_number, balance, created_at FROM accounts WHERE customer_id = ? AND account_type = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, userId);
            pstmt.setString(2, accountType);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                accNumLabel.setText(rs.getString("account_number"));
                balLabel.setText(String.format("$ %.2f", rs.getDouble("balance")));
                dateLabel.setText(rs.getDate("created_at").toString());
            } else {
                accNumLabel.setText("****");
                balLabel.setText("$ 0.00");
                dateLabel.setText("N/A");
            }
        }
    }

    private double getAccountBalance(Connection conn, int accountId) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE account_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getDouble("balance");
            }
            throw new SQLException("Account not found");
        }
    }

    private void updateBalance(Connection conn, int accountId, double amount) throws SQLException {
        String sql = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountId);
            pstmt.executeUpdate();
        }
    }

    private void recordTransaction(Connection conn, int accountId, String type, double amount, String description) throws SQLException {
        String sql = "INSERT INTO transactions (account_id, transaction_type, amount, description, transaction_date) VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, accountId);
            pstmt.setString(2, type);
            pstmt.setDouble(3, amount);
            pstmt.setString(4, description);
            pstmt.executeUpdate();
        }
    }

}
