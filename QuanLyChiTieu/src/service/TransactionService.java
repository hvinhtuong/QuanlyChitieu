package service;

import dao.TransactionDao;
import model.Transaction;
import java.sql.SQLException;
import java.util.List;

public class TransactionService {
    private TransactionDao transactionDao = new TransactionDao();

    public void addTransaction(Transaction transaction) throws SQLException {
        transactionDao.addTransaction(transaction);
    }

    public List<Transaction> getTransactionsByUserId(int userId) throws SQLException {
        return transactionDao.getTransactionsByUserId(userId);
    }
}
