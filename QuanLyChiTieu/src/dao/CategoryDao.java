package dao;

import model.Category;
import util.DBConnection;
import java.sql.*;

public class CategoryDao {
    public void addCategory(Category category) throws SQLException {
        String query = "INSERT INTO Categories (name, type, user_id) VALUES (?, ?, ?)";
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, category.getName());
            statement.setString(2, category.getType());
            statement.setInt(3, category.getUserId());
            statement.executeUpdate();
        }
    }
    
    // Các phương thức khác như getCategoryById, updateCategory, deleteCategory
}
