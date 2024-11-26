package com.banking.system.bankingsystem.Controllers.Client;

import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.banking.system.bankingsystem.Models.DatabaseConnection;
import com.banking.system.bankingsystem.Models.Model;

public class TransactionsController implements Initializable {
    public ListView transactions_listview;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadTransactions();
    }

    private void loadTransactions() {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = """
                SELECT t.transaction_date, t.transaction_type, t.amount, 
                       t.description, a.account_type, a.account_number
                FROM transactions t
                JOIN accounts a ON t.account_id = a.account_id
                WHERE a.customer_id = (SELECT user_id FROM users WHERE email = ?)
                ORDER BY t.transaction_date DESC
            """;

            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, Model.getInstance().getCurrentUserEmail());
            ResultSet rs = pstmt.executeQuery();

            transactions_listview.getItems().clear();

            while (rs.next()) {
                String date = rs.getTimestamp("transaction_date").toLocalDateTime()
                        .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
                String type = rs.getString("transaction_type");
                double amount = rs.getDouble("amount");
                String description = rs.getString("description");
                String accountType = rs.getString("account_type");
                String accountNumber = rs.getString("account_number");

                String transactionText = String.format("%s | %s | %s | $%.2f | %s | Account: %s",
                        date, type, description, Math.abs(amount),
                        accountType, accountNumber.substring(accountNumber.length() - 4));

                transactions_listview.getItems().add(transactionText);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
