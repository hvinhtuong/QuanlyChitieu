package model;

import java.util.Date;

public class Budget {

    private int budgetId;
    private int categoryId;
    private double amount;
    private Date startDate;
    private Date endDate;
    private int userId;

    // Getters
    public int getBudgetId() {
        return budgetId;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public double getAmount() {
        return amount;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public int getUserId() {
        return userId;
    }

    // Setters
    public void setBudgetId(int budgetId) {
        this.budgetId = budgetId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}

