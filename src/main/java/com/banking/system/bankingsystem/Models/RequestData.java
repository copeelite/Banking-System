package com.banking.system.bankingsystem.Models;

import javafx.beans.property.*;
import java.time.LocalDateTime;

public class RequestData {
    private final IntegerProperty requestId;
    private final StringProperty customerEmail;
    private final StringProperty accountType;
    private final DoubleProperty initialDeposit;
    private final StringProperty employeeEmail;
    private final ObjectProperty<LocalDateTime> createdAt;

    public RequestData(int requestId, String customerEmail, String accountType, 
                      double initialDeposit, String employeeEmail, LocalDateTime createdAt) {
        this.requestId = new SimpleIntegerProperty(requestId);
        this.customerEmail = new SimpleStringProperty(customerEmail);
        this.accountType = new SimpleStringProperty(accountType);
        this.initialDeposit = new SimpleDoubleProperty(initialDeposit);
        this.employeeEmail = new SimpleStringProperty(employeeEmail);
        this.createdAt = new SimpleObjectProperty<>(createdAt);
    }

    // Getters
    public int getRequestId() { return requestId.get(); }
    public String getCustomerEmail() { return customerEmail.get(); }
    public String getAccountType() { return accountType.get(); }
    public double getInitialDeposit() { return initialDeposit.get(); }
    public String getEmployeeEmail() { return employeeEmail.get(); }
    public LocalDateTime getCreatedAt() { return createdAt.get(); }

    // Property getters
    public IntegerProperty requestIdProperty() { return requestId; }
    public StringProperty customerEmailProperty() { return customerEmail; }
    public StringProperty accountTypeProperty() { return accountType; }
    public DoubleProperty initialDepositProperty() { return initialDeposit; }
    public StringProperty employeeEmailProperty() { return employeeEmail; }
    public ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }
}