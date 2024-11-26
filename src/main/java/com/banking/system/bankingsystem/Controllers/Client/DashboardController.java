package com.banking.system.bankingsystem.Controllers.Client;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.fxml.Initializable;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.banking.system.bankingsystem.Models.DatabaseConnection;
import com.banking.system.bankingsystem.Models.Model;

public class DashboardController implements Initializable {

    public Text user_name;
    public Label signin_date;
    public Label checking_bal;
    public Label checking_acc_num;
    public Label savings_bal;
    public Label savings_acc_num;
    public Text income_lbl;
    public Text expense_lbl;
    public ListView transaction_listview;
    public TextField etrans_fld;
    public TextField amount_fld;
    public TextField message_fld;
    public Button send_money_btn;

    private String currentUserEmail;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadAccountData();

        updateDateTime();

        loadUserData();

        loadTransactionSummary();
    }

    private void updateDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        signin_date.setText("Today, " + now.format(formatter));
    }

    private void loadUserData() {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT username FROM users WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, getCurrentUserEmail());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String username = rs.getString("username");
                if (username != null && !username.isEmpty()) {
                    user_name.setText("Hi, " + username);
                } else {
                    user_name.setText("Hello, Client");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            user_name.setText("Hello, Client");
        }
    }

    private void loadAccountData() {
        try (Connection conn = DatabaseConnection.connect()) {
            String currentEmail = getCurrentUserEmail();
            System.out.println("Current user email: " + currentEmail); 
    
            String userIdSql = "SELECT user_id FROM users WHERE email = ?";
            PreparedStatement userStmt = conn.prepareStatement(userIdSql);
            userStmt.setString(1, currentEmail);
            ResultSet userRs = userStmt.executeQuery();
            
            if (!userRs.next()) {
                System.out.println("User not found for email: " + currentEmail);
                return;
            }
            
            int userId = userRs.getInt("user_id");
            System.out.println("User ID: " + userId); 
    
            String sql = "SELECT account_type, account_number, balance FROM accounts WHERE customer_id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, userId);
            ResultSet rs = pstmt.executeQuery();
    
            checking_bal.setText("No Account");
            savings_bal.setText("No Account");
            checking_acc_num.setText("N/A");
            savings_acc_num.setText("N/A");
    
            while (rs.next()) {
                String accountType = rs.getString("account_type");
                String accountNumber = rs.getString("account_number");
                double balance = rs.getDouble("balance");
                
                System.out.println("Found account: " + accountType + ", number: " + accountNumber);
    
                if ("CHECKING".equals(accountType)) {
                    checking_acc_num.setText(accountNumber);
                    checking_bal.setText(String.format("$%.2f", balance));
                    System.out.println("Updated checking display"); 
                } else if ("SAVINGS".equals(accountType)) {
                    savings_acc_num.setText(accountNumber);
                    savings_bal.setText(String.format("$%.2f", balance));
                    System.out.println("Updated savings display"); 
                }
            }
        } catch (Exception e) {
            System.out.println("Error loading account data: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadTransactionSummary() {
        try (Connection conn = DatabaseConnection.connect()) {
            ResultSet tables = conn.getMetaData().getTables(null, null, "transactions", null);
            if (!tables.next()) {
                income_lbl.setText("+ $0.00");
                expense_lbl.setText("- $0.00");
                return;
            }

            income_lbl.setText("+ $0.00");
            expense_lbl.setText("- $0.00");

            String incomeSql = "SELECT SUM(amount) as total FROM transactions WHERE customer_id = ? AND type = 'CREDIT'";
            PreparedStatement incomeStmt = conn.prepareStatement(incomeSql);
            incomeStmt.setString(1, getCurrentUserEmail());
            ResultSet incomeRs = incomeStmt.executeQuery();

            if (incomeRs.next() && incomeRs.getDouble("total") > 0) {
                double income = incomeRs.getDouble("total");
                income_lbl.setText(String.format("+ $%.2f", income));
            }

            String expenseSql = "SELECT SUM(amount) as total FROM transactions WHERE customer_id = ? AND type = 'DEBIT'";
            PreparedStatement expenseStmt = conn.prepareStatement(expenseSql);
            expenseStmt.setString(1, getCurrentUserEmail());
            ResultSet expenseRs = expenseStmt.executeQuery();

            if (expenseRs.next() && expenseRs.getDouble("total") > 0) {
                double expense = expenseRs.getDouble("total");
                expense_lbl.setText(String.format("- $%.2f", expense));
            }
        } catch (Exception e) {
            System.out.println("Database not initialized yet");
        }
    }

    private String getCurrentUserEmail() {
        return Model.getInstance().getCurrentUserEmail();
    }

    public void setCurrentUserEmail(String email) {
        this.currentUserEmail = email;
    }
}
