package com.banking.system.bankingsystem.Models;

import javafx.beans.property.*;
import java.sql.Date;

public class ClientData {
    private final StringProperty username;
    private final StringProperty email;
    private final StringProperty checkingAccount;
    private final DoubleProperty checkingBalance;
    private final StringProperty savingsAccount;
    private final DoubleProperty savingsBalance;
    private final ObjectProperty<Date> createdAt;

    public ClientData(String username, String email, String checkingAccount, 
                     double checkingBalance, String savingsAccount, 
                     double savingsBalance, Date createdAt) {
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.checkingAccount = new SimpleStringProperty(checkingAccount);
        this.checkingBalance = new SimpleDoubleProperty(checkingBalance);
        this.savingsAccount = new SimpleStringProperty(savingsAccount);
        this.savingsBalance = new SimpleDoubleProperty(savingsBalance);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
    }

    // Getters
    public String getUsername() { return username.get(); }
    public String getEmail() { return email.get(); }
    public String getCheckingAccount() { return checkingAccount.get(); }
    public double getCheckingBalance() { return checkingBalance.get(); }
    public String getSavingsAccount() { return savingsAccount.get(); }
    public double getSavingsBalance() { return savingsBalance.get(); }
    public Date getCreatedAt() { return createdAt.get(); }


    

    // Property getters
    public StringProperty usernameProperty() { return username; }
    public StringProperty emailProperty() { return email; }
    public StringProperty checkingAccountProperty() { return checkingAccount; }
    public DoubleProperty checkingBalanceProperty() { return checkingBalance; }
    public StringProperty savingsAccountProperty() { return savingsAccount; }
    public DoubleProperty savingsBalanceProperty() { return savingsBalance; }
    public ObjectProperty<Date> createdAtProperty() { return createdAt; }
}