package ui;

import model.Transaction;
import model.User;
import service.TransactionService;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.Date;

public class AddTransactionFrame extends JFrame {
    private User user;
    private String type;
    private JTextField amountField;
    private JTextField descriptionField;
    static int transactionType;
    private DashboardFrame dashboardFrame;
    private TransactionService transactionService = new TransactionService();

    public AddTransactionFrame(User user, String type, DashboardFrame dashboardFrame) {
        this.user = user;
        this.type = type;
        this.dashboardFrame = dashboardFrame;
        setTitle(type.equals("Chi tiêu") ? "Thêm chi tiêu" : "Thêm thu nhập");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

        panel.add(new JLabel("Số tiền:"));
        amountField = new JTextField();
        panel.add(amountField);

        panel.add(new JLabel("Mô tả:"));
        descriptionField = new JTextField();
        panel.add(descriptionField);

        JButton addButton = new JButton("Add");
        addButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    addTransaction();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });
        panel.add(addButton);

        add(panel);
    }

    private void addTransaction() throws SQLException, NumberFormatException {
        double amount = Double.parseDouble(amountField.getText());
        String description = descriptionField.getText();
     // Validate input
        if (amount == 0 || description.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ số tiền và mô tả!");
            return; // Stop further processing if fields are empty
        }
        try {
        	Transaction transaction = new Transaction(amount, description);
        	transaction.setAmount(amount);
        	transaction.setDescription(description);
        	transaction.setDate(new Date());
        	transaction.setUserId(user.getUserId());
        	// Set dựa vào type, 1 là chi tiêu, 2 là thu nhập
        	transaction.setCategoryId(type.equals("Chi tiêu") ? 1 : 2);
        	// Thêm vào cơ sở dữ liệu
        	transactionService.addTransaction(transaction);

        	// Cập nhật số liệu trên DashboardFrame
        	transactionType = transaction.getCategoryId();
        	DashboardFrame.updateData(); 
        	// Khởi động lại DashboardFrame
        	dashboardFrame.refresh();

        	JOptionPane.showMessageDialog(this, "Thêm thành công!");
        	dispose();
        } catch (NumberFormatException e) {
        	JOptionPane.showMessageDialog(this, "Số tiền phải là một giá trị số!");
        	return;
        }
    }
}
