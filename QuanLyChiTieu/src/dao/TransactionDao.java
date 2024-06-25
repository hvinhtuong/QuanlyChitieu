package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import model.Transaction;
import util.DBConnection;

public class TransactionDao {
    public void addTransaction(Transaction transaction) throws SQLException {
        String query = "INSERT INTO Transactions (amount, date, description, category_id, user_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDouble(1, transaction.getAmount());
            statement.setTimestamp(2, new Timestamp(transaction.getDate().getTime()));
            statement.setString(3, transaction.getDescription());
            statement.setInt(4, transaction.getCategoryId());
            statement.setInt(5, transaction.getUserId());
            statement.executeUpdate();
        }
    }
    
    public List<Transaction> getTransactionsByUserId(int userId) throws SQLException {
        String query = "SELECT * FROM Transactions WHERE user_id = ?";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, userId);
            ResultSet resultSet = statement.executeQuery();
            List<Transaction> transactions = new ArrayList<>();
            while (resultSet.next()) {
                Transaction transaction = new Transaction(userId, query);
                transaction.setTransactionId(resultSet.getInt("transaction_id"));
                transaction.setAmount(resultSet.getDouble("amount"));
                transaction.setDate(resultSet.getTimestamp("date"));
                transaction.setDescription(resultSet.getString("description"));
                transaction.setCategoryId(resultSet.getInt("category_id"));
                transaction.setUserId(resultSet.getInt("user_id"));
                transactions.add(transaction);
            }
            return transactions;
        }
    }
}
