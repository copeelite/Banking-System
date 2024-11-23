package com.banking.system.bankingsystem;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class BankController {
    public VBox rootPane;
    @FXML
    private Label welcomeText;

    @FXML
    protected void onBankButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }
}