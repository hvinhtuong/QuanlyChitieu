package ui;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

//Tạo user thử nghiệm
public class Main {
    public static void main(String[] args) {
        // Thông tin kết nối tới cơ sở dữ liệu
        String url = "jdbc:mysql://localhost:3306/ExpenseManager";
        String user = "root";
        String password = "240397";

        // Thông tin người dùng mới
        String username = "nhan2003";
        String email = "nhan@yahoo.com";
        String hashedPassword = "$2y$10$$2y$04$J166MlKdTyP9GAlKDxXTGuv9gA0SDU90qWpJv4zjdxDtZ6Mbx7st2"; // Mật khẩu đã được mã hóa trước khi lưu vào cơ sở dữ liệu

        // Câu lệnh SQL để chèn người dùng mới vào bảng Users
        String insertQuery = "INSERT INTO Users (username, email, password_hash, created_at, last_login) VALUES (?, ?, ?, NOW(), NOW())";

        try (Connection connection = DriverManager.getConnection(url, user, password);
             PreparedStatement statement = connection.prepareStatement(insertQuery)) {
            // Thiết lập các tham số cho câu lệnh SQL
            statement.setString(1, username);
            statement.setString(2, email);
            statement.setString(3, hashedPassword);

            // Thực thi câu lệnh SQL
            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                System.out.println("A new user was inserted successfully!");
            } else {
                System.out.println("Failed to insert a new user!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
