package dao;

import model.Budget;
import util.DBConnection;
import java.sql.*;

public class BudgetDao {
    public void addBudget(Budget budget) throws SQLException {
        String query = "INSERT INTO Budgets (category_id, amount, start_date, end_date, user_id) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, budget.getCategoryId());
            statement.setDouble(2, budget.getAmount());
            statement.setDate(3, new java.sql.Date(budget.getStartDate().getTime()));
            statement.setDate(4, new java.sql.Date(budget.getEndDate().getTime()));
            statement.setInt(5, budget.getUserId());
            statement.executeUpdate();
        }
    }
}
