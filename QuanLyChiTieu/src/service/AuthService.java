package service;

import java.sql.SQLException;

import dao.UserDao;
import model.User;
import util.PasswordUtils;

public class AuthService {
    private UserDao userDao;

    public AuthService() {
        this.userDao = new UserDao();
    }

    public User login(String username, String password) throws SQLException {
        User user = userDao.getUserByUsername(username);
        if (user != null) {
            try {
                if (PasswordUtils.verifyPassword(password, user.getPassword())) {
                    return user;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public boolean register(User user) {
        try {
            String hashedPassword = PasswordUtils.hashPassword(user.getPassword());
            user.setPassword(hashedPassword);
            userDao.addUser(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
