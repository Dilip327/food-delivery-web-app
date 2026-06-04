package com.fooddelivery.dao;

import com.fooddelivery.model.CartItem;
import com.fooddelivery.model.MenuItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuDao {
    public List<MenuItem> findByRestaurant(long restaurantId) throws SQLException {
        String sql = "SELECT * FROM menu_items WHERE restaurant_id = ? AND is_available = TRUE ORDER BY category, name";
        try (Connection connection = Database.connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, restaurantId);
            try (ResultSet rs = statement.executeQuery()) {
                List<MenuItem> items = new ArrayList<>();
                while (rs.next()) {
                    items.add(map(rs));
                }
                return items;
            }
        }
    }

    public CartItem findCartItem(long menuItemId) throws SQLException {
        String sql = "SELECT m.id, m.restaurant_id, m.name, m.price, r.name AS restaurant_name FROM menu_items m JOIN restaurants r ON r.id = m.restaurant_id WHERE m.id = ? AND m.is_available = TRUE";
        try (Connection connection = Database.connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, menuItemId);
            try (ResultSet rs = statement.executeQuery()) {
                if (!rs.next()) {
                    return null;
                }
                CartItem item = new CartItem();
                item.setMenuItemId(rs.getLong("id"));
                item.setRestaurantId(rs.getLong("restaurant_id"));
                item.setRestaurantName(rs.getString("restaurant_name"));
                item.setName(rs.getString("name"));
                item.setPrice(rs.getBigDecimal("price"));
                item.setQuantity(1);
                return item;
            }
        }
    }

    private MenuItem map(ResultSet rs) throws SQLException {
        MenuItem item = new MenuItem();
        item.setId(rs.getLong("id"));
        item.setRestaurantId(rs.getLong("restaurant_id"));
        item.setName(rs.getString("name"));
        item.setDescription(rs.getString("description"));
        item.setCategory(rs.getString("category"));
        item.setPrice(rs.getBigDecimal("price"));
        item.setImageUrl(rs.getString("image_url"));
        item.setVeg(rs.getBoolean("is_veg"));
        item.setAvailable(rs.getBoolean("is_available"));
        return item;
    }
}
