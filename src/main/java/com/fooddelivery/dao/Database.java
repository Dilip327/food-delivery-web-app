package com.fooddelivery.dao;

import com.fooddelivery.config.AppConfig;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Database {
    private Database() {
    }

    static {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new IllegalStateException("MySQL driver not found", e);
        }
    }

    public static Connection connection() throws SQLException {
        return DriverManager.getConnection(AppConfig.dbUrl(), AppConfig.dbUser(), AppConfig.dbPassword());
    }
}
