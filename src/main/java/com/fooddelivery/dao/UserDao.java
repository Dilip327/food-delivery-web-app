package com.fooddelivery.dao;

import com.fooddelivery.model.User;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    public User register(String name, String email, String phone, String password) throws SQLException {
        String sql = "INSERT INTO users (name, email, phone, password_hash) VALUES (?, ?, ?, ?)";
        try (Connection connection = Database.connection();
             PreparedStatement statement = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, name);
            statement.setString(2, email.toLowerCase());
            statement.setString(3, phone);
            statement.setString(4, BCrypt.hashpw(password, BCrypt.gensalt()));
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                keys.next();
                return findById(keys.getLong(1));
            }
        }
    }

    public User authenticate(String email, String password) throws SQLException {
        String sql = "SELECT * FROM users WHERE email = ?";
        try (Connection connection = Database.connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email.toLowerCase());
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next() && BCrypt.checkpw(password, rs.getString("password_hash"))) {
                    return map(rs);
                }
            }
        }
        return null;
    }

    public User findById(long id) throws SQLException {
        try (Connection connection = Database.connection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    private User map(ResultSet rs) throws SQLException {
        User user = new User();
        user.setId(rs.getLong("id"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPhone(rs.getString("phone"));
        user.setRole(rs.getString("role"));
        return user;
    }
}
