package com.banking.system.bankingsystem.Controllers.Client;

import com.banking.system.bankingsystem.Models.DatabaseConnection;
import com.banking.system.bankingsystem.Models.Model;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ProfileController implements Initializable {
    @FXML private TextField usernameField;
    @FXML private TextField emailField;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        loadUserData();
    }

    private void loadUserData() {
        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "SELECT username, email FROM users WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, Model.getInstance().getCurrentUserEmail());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                usernameField.setText(rs.getString("username"));
                emailField.setText(rs.getString("email"));
            }
        } catch (SQLException e) {
            showMessage("Error loading user data", false);
        }
    }

    @FXML
    private void handleUpdateDetails() {
        String newUsername = usernameField.getText().trim();
        
        if (newUsername.isEmpty()) {
            showMessage("Username cannot be empty", false);
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {
            String sql = "UPDATE users SET username = ? WHERE email = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newUsername);
            pstmt.setString(2, Model.getInstance().getCurrentUserEmail());
            pstmt.executeUpdate();
            
            showMessage("Profile updated successfully", true);
        } catch (SQLException e) {
            showMessage("Error updating profile", false);
        }
    }

    @FXML
    private void handleChangePassword() {
        String currentPassword = currentPasswordField.getText();
        String newPassword = newPasswordField.getText();
        String confirmPassword = confirmPasswordField.getText();

        if (currentPassword.isEmpty() || newPassword.isEmpty() || confirmPassword.isEmpty()) {
            showMessage("All password fields are required", false);
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            showMessage("New passwords do not match", false);
            return;
        }

        try (Connection conn = DatabaseConnection.connect()) {
            if (validateCurrentPassword(conn, currentPassword)) {
                updatePassword(conn, newPassword);
                showMessage("Password changed successfully", true);
                clearPasswordFields();
            } else {
                showMessage("Current password is incorrect", false);
            }
        } catch (SQLException e) {
            showMessage("Error changing password", false);
        }
    }

    private boolean validateCurrentPassword(Connection conn, String currentPassword) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ? AND password = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, Model.getInstance().getCurrentUserEmail());
        pstmt.setString(2, currentPassword);
        ResultSet rs = pstmt.executeQuery();
        return rs.next() && rs.getInt(1) > 0;
    }

    private void updatePassword(Connection conn, String newPassword) throws SQLException {
        String sql = "UPDATE users SET password = ? WHERE email = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, newPassword);
        pstmt.setString(2, Model.getInstance().getCurrentUserEmail());
        pstmt.executeUpdate();
    }

    private void clearPasswordFields() {
        currentPasswordField.clear();
        newPasswordField.clear();
        confirmPasswordField.clear();
    }

    private void showMessage(String message, boolean isSuccess) {
        messageLabel.setText(message);
        messageLabel.getStyleClass().removeAll("success-message", "error-message");
        messageLabel.getStyleClass().add(isSuccess ? "success-message" : "error-message");
    }
}