package model;

import java.util.Date;

public class Notification {

    private int notificationId;
    private int userId;
    private String message;
    private Date date;
    private String status;

    // Getters
    public int getNotificationId() {
        return notificationId;
    }

    public int getUserId() {
        return userId;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setDate(Date date) {
        // Consider making a copy of the date object to avoid unintended modification
        this.date = date;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
