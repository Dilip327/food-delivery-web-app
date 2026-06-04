package com.fooddelivery.dao;

import com.fooddelivery.model.Restaurant;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class RestaurantDao {
    public List<Restaurant> findAll(String query) throws SQLException {
        String sql = "SELECT * FROM restaurants WHERE is_open = TRUE AND (? IS NULL OR name LIKE ? OR cuisine LIKE ?) ORDER BY rating DESC";
        try (Connection connection = Database.connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            String search = query == null || query.trim().isEmpty() ? null : "%" + query.trim() + "%";
            statement.setString(1, search);
            statement.setString(2, search);
            statement.setString(3, search);
            try (ResultSet rs = statement.executeQuery()) {
                List<Restaurant> restaurants = new ArrayList<>();
                while (rs.next()) {
                    restaurants.add(map(rs));
                }
                return restaurants;
            }
        }
    }

    public Restaurant findById(long id) throws SQLException {
        try (Connection connection = Database.connection();
             PreparedStatement statement = connection.prepareStatement("SELECT * FROM restaurants WHERE id = ?")) {
            statement.setLong(1, id);
            try (ResultSet rs = statement.executeQuery()) {
                return rs.next() ? map(rs) : null;
            }
        }
    }

    private Restaurant map(ResultSet rs) throws SQLException {
        Restaurant restaurant = new Restaurant();
        restaurant.setId(rs.getLong("id"));
        restaurant.setName(rs.getString("name"));
        restaurant.setCuisine(rs.getString("cuisine"));
        restaurant.setDescription(rs.getString("description"));
        restaurant.setImageUrl(rs.getString("image_url"));
        restaurant.setRating(rs.getBigDecimal("rating"));
        restaurant.setDeliveryTimeMinutes(rs.getInt("delivery_time_minutes"));
        restaurant.setDeliveryFee(rs.getBigDecimal("delivery_fee"));
        restaurant.setMinOrderAmount(rs.getBigDecimal("min_order_amount"));
        restaurant.setAddress(rs.getString("address"));
        BigDecimal latitude = rs.getBigDecimal("latitude");
        BigDecimal longitude = rs.getBigDecimal("longitude");
        restaurant.setLatitude(latitude == null ? null : latitude.doubleValue());
        restaurant.setLongitude(longitude == null ? null : longitude.doubleValue());
        restaurant.setOpen(rs.getBoolean("is_open"));
        return restaurant;
    }
}
