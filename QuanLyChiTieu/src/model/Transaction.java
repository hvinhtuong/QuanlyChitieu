package model;

import java.util.Date;

public class Transaction {

    private int transactionId;
    private double amount;
    private Date date;
    private String description;
    private int categoryId;
    private int userId;

    // Constructor
    public Transaction(double amount, String description) {
        this.amount = amount;
        this.description = description;
    }


	// Getters
    public int getTransactionId() {
        return transactionId;
    }

    public double getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public String getDescription() {
        return description;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public int getUserId() {
        return userId;
    }

    // Setters
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
