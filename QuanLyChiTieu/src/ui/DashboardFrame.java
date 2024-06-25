package ui;

import model.Transaction;
import model.User;
import service.TransactionService;
import util.DBConnection;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DashboardFrame extends JFrame {

    private static User user;
    private static JLabel balanceLabel;
    private static JLabel incomeLabel;
    private static JLabel expenseLabel;
    private static JPanel recentTransactionsPanel;
    private static JPanel recentIncomeTransactionsPanel;
    private static JPanel recentExpenseTransactionsPanel;
    private TransactionService transactionService;

    public DashboardFrame(User user) throws SQLException {
        this.user = user;
        this.transactionService = new TransactionService();
        initUI();
    }

    void initUI() throws SQLException {
        setTitle("Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // North Panel for user greeting
        JPanel northPanel = new JPanel();
        northPanel.setLayout(new BoxLayout(northPanel, BoxLayout.Y_AXIS));
        JLabel welcomeLabel = new JLabel("Chào mừng bạn trở lại, " + user.getUsername() + "!");
        welcomeLabel.setFont(new Font("Arial", Font.BOLD, 24));
        northPanel.add(welcomeLabel);

        panel.add(northPanel, BorderLayout.NORTH);

        // Center Panel for financial overview
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));

        balanceLabel = new JLabel("Số dư tài khoản: $" + getBalance() + "\n");
        incomeLabel = new JLabel("Tổng thu nhập: $" + getTotalIncome());
        expenseLabel = new JLabel("Tổng chi tiêu: $" + getTotalExpense());

        balanceLabel.setFont(new Font("Arial", Font.BOLD, 18));
        incomeLabel.setFont(new Font("Arial", Font.BOLD, 14));
        expenseLabel.setFont(new Font("Arial", Font.BOLD, 14));

        balanceLabel.setForeground(Color.GREEN);
        incomeLabel.setForeground(Color.GREEN);
        expenseLabel.setForeground(Color.RED);

        centerPanel.add(balanceLabel);
        centerPanel.add(incomeLabel);

        // Recent Income Transactions
        recentIncomeTransactionsPanel = new JPanel();
        recentIncomeTransactionsPanel.setLayout(new BoxLayout(recentIncomeTransactionsPanel, BoxLayout.Y_AXIS));
        recentIncomeTransactionsPanel.setBorder(BorderFactory.createTitledBorder("Recently"));
        loadRecentTransactions(recentIncomeTransactionsPanel, 2);
        centerPanel.add(recentIncomeTransactionsPanel);

        centerPanel.add(expenseLabel);

        // Recent Expense Transactions
        recentExpenseTransactionsPanel = new JPanel();
        recentExpenseTransactionsPanel.setLayout(new BoxLayout(recentExpenseTransactionsPanel, BoxLayout.Y_AXIS));
        recentExpenseTransactionsPanel.setBorder(BorderFactory.createTitledBorder("Recently"));
        loadRecentTransactions(recentExpenseTransactionsPanel, 1);
        centerPanel.add(recentExpenseTransactionsPanel);

        panel.add(centerPanel, BorderLayout.CENTER);

        // South Panel for navigation buttons
        JPanel southPanel = new JPanel();
        southPanel.setLayout(new FlowLayout());

        JButton addExpenseButton = new JButton("Thêm chi tiêu");
        addExpenseButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle Add Expense action
                showAddTransactionFrame("Chi tiêu");
            }
        });

        JButton addIncomeButton = new JButton("Thêm thu nhập");
        addIncomeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle Add Income action
                showAddTransactionFrame("Thu nhập");
            }
        });

        JButton viewReportsButton = new JButton("Xem báo cáo chi tiết");
        viewReportsButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Handle View Reports action
                showDetailReport();
            }
        });

        southPanel.add(addExpenseButton);
        southPanel.add(addIncomeButton);
        southPanel.add(viewReportsButton);

        panel.add(southPanel, BorderLayout.SOUTH);

        add(panel);
    }

    // Phương thức lấy số dư tài khoản
    private static double getBalance() throws SQLException {
        return getTotalIncome() - getTotalExpense();
    }

    // Phương thức lấy tổng thu nhập
    private static double getTotalIncome() throws SQLException {
        double totalIncome = 0.0;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBConnection.getConnection();
            String query = "SELECT SUM(amount) FROM transactions WHERE user_id = ? AND category_id = 2";
            statement = connection.prepareStatement(query);
            statement.setInt(1, user.getUserId());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalIncome = resultSet.getDouble(1);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return totalIncome;
    }

    // Phương thức lấy tổng chi tiêu
    private static double getTotalExpense() throws SQLException {
        double totalExpense = 0.0;
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBConnection.getConnection();
            String query = "SELECT SUM(amount) FROM transactions WHERE user_id = ? AND category_id = 1";
            statement = connection.prepareStatement(query);
            statement.setInt(1, user.getUserId());
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
                totalExpense = resultSet.getDouble(1);
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return totalExpense;
    }

    // Phương thức lấy các giao dịch chi tiêu gần đây
    private static List<Transaction> getRecentTransactions1() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBConnection.getConnection();
            String query = "SELECT amount, description,category_id FROM transactions WHERE user_id = ? AND category_id = 1 ORDER BY date DESC LIMIT 5";
            statement = connection.prepareStatement(query);
            statement.setInt(1, user.getUserId());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                double amount = resultSet.getDouble("amount");
                String description = resultSet.getString("description");
                transactions.add(new Transaction(amount, description));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return transactions;
    }
    
    // Phương thức lấy các giao dịch thu nhập gần đây
    private static List<Transaction> getRecentTransactions2() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        try {
            connection = DBConnection.getConnection();
            String query = "SELECT amount, description, category_id FROM transactions WHERE user_id = ? AND category_id = 2 ORDER BY date DESC LIMIT 5";
            statement = connection.prepareStatement(query);
            statement.setInt(1, user.getUserId());
            resultSet = statement.executeQuery();
            while (resultSet.next()) {
                double amount = resultSet.getDouble("amount");
                String description = resultSet.getString("description");
                transactions.add(new Transaction(amount, description));
            }
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (statement != null) {
                statement.close();
            }
            if (connection != null) {
                connection.close();
            }
        }
        return transactions;
    }
    
    private static void loadRecentTransactions(JPanel panel, int type) throws SQLException {
    	if (type == 1) {
    		List<Transaction> transactions = getRecentTransactions1();
    		panel.removeAll();
            for (Transaction transaction : transactions) {
                JLabel transactionLabel = new JLabel("$" + transaction.getAmount() + " - " + transaction.getDescription());
                transactionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                	panel.add(transactionLabel);
            }
    	} else {
    		List<Transaction> transactions = getRecentTransactions2();
    		panel.removeAll();
            for (Transaction transaction : transactions) {
                JLabel transactionLabel = new JLabel("$" + transaction.getAmount() + " - " + transaction.getDescription());
                transactionLabel.setFont(new Font("Arial", Font.ITALIC, 12));
                	panel.add(transactionLabel);
            }
    	}
    }

    private void showAddTransactionFrame(String title) {
        AddTransactionFrame addTransactionFrame = new AddTransactionFrame(user, title, this);
        addTransactionFrame.setVisible(true);
    }

    private void showDetailReport() {
        try {
            // Lấy thông tin giao dịch của người dùng
            List<Transaction> transactions = transactionService.getTransactionsByUserId(user.getUserId());

            // Khởi tạo biến để tính tổng số tiền thu và tổng số tiền chi
            double totalIncome = 0;
            double totalExpense = 0;

            // Tạo nội dung báo cáo chi tiết
            StringBuilder reportContent = new StringBuilder();
            reportContent.append("<html><body>");
            reportContent.append("<b>Tên user:</b> ").append(user.getUsername()).append("<br>");

            for (Transaction transaction : transactions) {
                if (transaction.getCategoryId() == 1) { // 1 là mã danh mục cho giao dịch chi
                    totalExpense += transaction.getAmount();
                    reportContent.append("<b>Số tiền chi:</b> ").append(transaction.getAmount()).append(", <b>Mô tả:</b> ").append(transaction.getDescription()).append(", <b>Thời gian:</b> ").append(transaction.getDate()).append("<br>");
                } else if (transaction.getCategoryId() == 2) { // 2 là mã danh mục cho giao dịch thu
                    totalIncome += transaction.getAmount();
                    reportContent.append("<b>Số tiền thu:</b> ").append(transaction.getAmount()).append(", <b>Mô tả:</b> ").append(transaction.getDescription()).append(", <b>Thời gian:</b> ").append(transaction.getDate()).append("<br>");
                }
            }

            // Tính tổng cộng
            double totalAmount = totalIncome - totalExpense;
            reportContent.append("<b>Tổng thu:</b> ").append(totalIncome).append("<br>");
            reportContent.append("<b>Tổng chi:</b> ").append(totalExpense).append("<br>");
            reportContent.append("<b>Tổng cộng:</b> ").append(totalAmount).append("<br>");
            reportContent.append("</body></html>");

            // Hiển thị cửa sổ báo cáo chi tiết
            showDetailReportWindow(reportContent.toString());
        } catch (SQLException ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "An error occurred while fetching transactions.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void showDetailReportWindow(String reportContent) {
        JFrame detailReportFrame = new JFrame("Detail Report");
        detailReportFrame.setSize(400, 300);
        detailReportFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        detailReportFrame.setLocationRelativeTo(this);

        // Tạo dòng tiêu đề in đậm
        String title = "<html><div style='text-align: center;'><b>BÁO CÁO CHI TIẾT THÁNG</b></div><br>";

        // Tạo label để hiển thị nội dung báo cáo
        JLabel titleLabel = new JLabel(title + reportContent);
        titleLabel.setVerticalAlignment(SwingConstants.TOP);
        titleLabel.setFont(new Font("Arial", Font.PLAIN, 12));
        JScrollPane scrollPane = new JScrollPane(titleLabel);

        // Thêm scrollPane vào cửa sổ
        detailReportFrame.add(scrollPane);

        // Tạo nút in và xử lý sự kiện khi nhấn
        JButton printButton = new JButton("In Báo Cáo");
        printButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    File reportFile = printReportToTxt(reportContent);
                    JOptionPane.showMessageDialog(detailReportFrame, "Báo cáo đã được in thành công.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    Desktop.getDesktop().open(reportFile); // Mở tập tin sau khi in thành công
                } catch (IOException ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(detailReportFrame, "Có lỗi xảy ra.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });
        detailReportFrame.add(printButton, BorderLayout.SOUTH);

        // Hiển thị cửa sổ báo cáo chi tiết
        detailReportFrame.setVisible(true);
    }

    private File printReportToTxt(String reportContent) throws IOException {
        File reportFile = new File("Report.html");
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(reportFile))) {
            writer.write(reportContent);
        }
        return reportFile;
    }



    // Phương thức cập nhật số liệu trên DashboardFrame
    public static void updateData() throws SQLException {
    	try {
    		double balance = getBalance();
    		double totalIncome = getTotalIncome();
    		double totalExpense = getTotalExpense();

    		balanceLabel.setText("Số dư tài khoản: $" + balance);
    		incomeLabel.setText("Tổng thu nhập: $" + totalIncome);
    		expenseLabel.setText("Tổng chi tiêu: $" + totalExpense);
    	} catch (SQLException e) {
    		e.printStackTrace();
    	}
//    	// Cập nhật lại giao dịch gần đây khi add
//    	int transactionType = AddTransactionFrame.transactionType;
//    	if (transactionType == 2) {
//    		loadRecentTransactions(recentIncomeTransactionsPanel, 2);
//    	} else {
//    		loadRecentTransactions(recentIncomeTransactionsPanel, 1);
//    	}
    }
    
    public void refresh() throws SQLException {
    	getContentPane().removeAll();
    	// Khởi tạo lại giao diện
        initUI();

        // Vẽ lại giao diện
        revalidate();
        repaint();
    }

    public static void main(String[] args) {
        User user = new User(1, "nhan2003", "@example.com", "password", null, null);
        try {
            DashboardFrame dashboardFrame = new DashboardFrame(user);
            dashboardFrame.setVisible(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
