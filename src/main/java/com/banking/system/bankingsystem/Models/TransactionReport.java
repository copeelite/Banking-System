package com.banking.system.bankingsystem.Models;

public class TransactionReport {
    private String date;
    private String type;
    private double amount;
    private String description;

    public TransactionReport(String date, String type, double amount, String description) {
        this.date = date;
        this.type = type;
        this.amount = amount;
        this.description = description;
    }

    public String getDate() { return date; }
    public String getType() { return type; }
    public double getAmount() { return amount; }
    public String getDescription() { return description; }
}