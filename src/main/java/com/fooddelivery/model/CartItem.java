package com.fooddelivery.model;

import java.math.BigDecimal;

public class CartItem {
    private long menuItemId;
    private long restaurantId;
    private String restaurantName;
    private String name;
    private BigDecimal price;
    private int quantity;

    public long getMenuItemId() { return menuItemId; }
    public void setMenuItemId(long menuItemId) { this.menuItemId = menuItemId; }
    public long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(long restaurantId) { this.restaurantId = restaurantId; }
    public String getRestaurantName() { return restaurantName; }
    public void setRestaurantName(String restaurantName) { this.restaurantName = restaurantName; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getLineTotal() { return price.multiply(BigDecimal.valueOf(quantity)); }
}
