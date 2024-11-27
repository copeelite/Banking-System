package com.banking.system.bankingsystem.Models;

import javafx.beans.property.*;

public class UserData {
    private final IntegerProperty id;
    private final StringProperty username;
    private final StringProperty email;
    private final StringProperty role;
    private boolean is_plus;
    public UserData(int id, String username, String email, String role, boolean is_plus) {
        this.id = new SimpleIntegerProperty(id);
        this.username = new SimpleStringProperty(username);
        this.email = new SimpleStringProperty(email);
        this.role = new SimpleStringProperty(role);
        this.is_plus = is_plus;
    }

    public int getId() { return id.get(); }
    public String getUsername() { return username.get(); }
    public String getEmail() { return email.get(); }
    public String getRole() { return role.get(); }
    public boolean getIsPlus() { return is_plus; }
    public IntegerProperty idProperty() { return id; }
    public StringProperty usernameProperty() { return username; }
    public StringProperty emailProperty() { return email; }
    public StringProperty roleProperty() { return role; }
    public BooleanProperty isPlusProperty() { return new SimpleBooleanProperty(is_plus); }
}