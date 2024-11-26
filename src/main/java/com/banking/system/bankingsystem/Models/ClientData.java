package com.banking.system.bankingsystem.Models;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ClientData {
    private final StringProperty email;
    private final StringProperty username;

    public ClientData(String email, String username) {
        this.email = new SimpleStringProperty(email);
        this.username = new SimpleStringProperty(username);
    }

    public StringProperty emailProperty() { return email; }
    public StringProperty usernameProperty() { return username; }
    public String getEmail() { return email.get(); }
    public String getUsername() { return username.get(); }
}