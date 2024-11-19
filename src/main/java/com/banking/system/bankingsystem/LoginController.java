package com.banking.system.bankingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.Node;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;

    private static final String VALID_USERNAME = "user";
    private static final String VALID_PASSWORD = "password";

    @FXML
    protected void onLoginButtonClick(javafx.event.ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (username.equals(VALID_USERNAME) && password.equals(VALID_PASSWORD)) {
            // Navigate to Dashboard
            try {
                Parent dashboard = FXMLLoader.load(getClass().getResource("/com/banking/system/bankingsystem/dashboard-view.fxml"));
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                Scene scene = new Scene(dashboard);
                stage.setScene(scene);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            errorLabel.setText("Invalid username or password.");
            errorLabel.setVisible(true);
        }
    }
}
