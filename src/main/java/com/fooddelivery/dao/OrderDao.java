package com.fooddelivery.dao;

import com.fooddelivery.model.CartItem;
import com.fooddelivery.model.Order;
import com.fooddelivery.model.OrderItem;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderDao {
    public long create(long userId, String name, String phone, String address, String paymentMethod, List<CartItem> cartItems) throws SQLException {
        if (cartItems.isEmpty()) {
            throw new IllegalArgumentException("Cart is empty");
        }

        long restaurantId = cartItems.get(0).getRestaurantId();
        BigDecimal subtotal = cartItems.stream().map(CartItem::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal deliveryFee = subtotal.compareTo(BigDecimal.valueOf(499)) >= 0 ? BigDecimal.ZERO : BigDecimal.valueOf(35);
        BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.05)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal total = subtotal.add(deliveryFee).add(tax);

        String orderSql = "INSERT INTO orders (user_id, restaurant_id, customer_name, phone, delivery_address, payment_method, subtotal, delivery_fee, tax, total) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        String itemSql = "INSERT INTO order_items (order_id, menu_item_id, item_name, quantity, unit_price) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = Database.connection()) {
            connection.setAutoCommit(false);
            try (PreparedStatement orderStatement = connection.prepareStatement(orderSql, Statement.RETURN_GENERATED_KEYS)) {
                orderStatement.setLong(1, userId);
                orderStatement.setLong(2, restaurantId);
                orderStatement.setString(3, name);
                orderStatement.setString(4, phone);
                orderStatement.setString(5, address);
                orderStatement.setString(6, paymentMethod);
                orderStatement.setBigDecimal(7, subtotal);
                orderStatement.setBigDecimal(8, deliveryFee);
                orderStatement.setBigDecimal(9, tax);
                orderStatement.setBigDecimal(10, total);
                orderStatement.executeUpdate();
                try (ResultSet keys = orderStatement.getGeneratedKeys()) {
                    keys.next();
                    long orderId = keys.getLong(1);
                    try (PreparedStatement itemStatement = connection.prepareStatement(itemSql)) {
                        for (CartItem item : cartItems) {
                            itemStatement.setLong(1, orderId);
                            itemStatement.setLong(2, item.getMenuItemId());
                            itemStatement.setString(3, item.getName());
                            itemStatement.setInt(4, item.getQuantity());
                            itemStatement.setBigDecimal(5, item.getPrice());
                            itemStatement.addBatch();
                        }
                        itemStatement.executeBatch();
                    }
                    connection.commit();
                    return orderId;
                }
            } catch (SQLException | RuntimeException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public List<Order> findByUser(long userId) throws SQLException {
        String sql = "SELECT o.*, r.name AS restaurant_name FROM orders o JOIN restaurants r ON r.id = o.restaurant_id WHERE o.user_id = ? ORDER BY o.created_at DESC";
        try (Connection connection = Database.connection();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setLong(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                List<Order> orders = new ArrayList<>();
                while (rs.next()) {
                    Order order = mapOrder(rs);
                    order.setItems(findItems(connection, order.getId()));
                    orders.add(order);
                }
                return orders;
            }
        }
    }

    private List<OrderItem> findItems(Connection connection, long orderId) throws SQLException {
        try (PreparedStatement statement = connection.prepareStatement("SELECT * FROM order_items WHERE order_id = ?")) {
            statement.setLong(1, orderId);
            try (ResultSet rs = statement.executeQuery()) {
                List<OrderItem> items = new ArrayList<>();
                while (rs.next()) {
                    OrderItem item = new OrderItem();
                    item.setMenuItemId(rs.getLong("menu_item_id"));
                    item.setItemName(rs.getString("item_name"));
                    item.setQuantity(rs.getInt("quantity"));
                    item.setUnitPrice(rs.getBigDecimal("unit_price"));
                    items.add(item);
                }
                return items;
            }
        }
    }

    private Order mapOrder(ResultSet rs) throws SQLException {
        Order order = new Order();
        order.setId(rs.getLong("id"));
        order.setUserId(rs.getLong("user_id"));
        order.setRestaurantId(rs.getLong("restaurant_id"));
        order.setRestaurantName(rs.getString("restaurant_name"));
        order.setStatus(rs.getString("status"));
        order.setCustomerName(rs.getString("customer_name"));
        order.setPhone(rs.getString("phone"));
        order.setDeliveryAddress(rs.getString("delivery_address"));
        order.setPaymentMethod(rs.getString("payment_method"));
        order.setSubtotal(rs.getBigDecimal("subtotal"));
        order.setDeliveryFee(rs.getBigDecimal("delivery_fee"));
        order.setTax(rs.getBigDecimal("tax"));
        order.setTotal(rs.getBigDecimal("total"));
        order.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
        return order;
    }
}
