package com.fooddelivery.model;

import java.math.BigDecimal;

public class MenuItem {
    private long id;
    private long restaurantId;
    private String name;
    private String description;
    private String category;
    private BigDecimal price;
    private String imageUrl;
    private boolean veg;
    private boolean available;

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }
    public long getRestaurantId() { return restaurantId; }
    public void setRestaurantId(long restaurantId) { this.restaurantId = restaurantId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public boolean isVeg() { return veg; }
    public void setVeg(boolean veg) { this.veg = veg; }
    public boolean isAvailable() { return available; }
    public void setAvailable(boolean available) { this.available = available; }
}
