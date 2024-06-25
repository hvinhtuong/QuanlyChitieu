package dao;

import model.Notification;
import util.DBConnection;
import java.sql.*;

public class NotificationDao {
    public void addNotification(Notification notification) throws SQLException {
        String query = "INSERT INTO Notifications (user_id, message, date, status) VALUES (?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, notification.getUserId());
            statement.setString(2, notification.getMessage());
            statement.setTimestamp(3, new Timestamp(notification.getDate().getTime()));
            statement.setString(4, notification.getStatus());
            statement.executeUpdate();
        }
    }
}
