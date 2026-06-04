package com.fooddelivery.model;

import java.math.BigDecimal;

public class OrderItem {
    private long menuItemId;
    private String itemName;
    private int quantity;
    private BigDecimal unitPrice;

    public long getMenuItemId() { return menuItemId; }
    public void setMenuItemId(long menuItemId) { this.menuItemId = menuItemId; }
    public String getItemName() { return itemName; }
    public void setItemName(String itemName) { this.itemName = itemName; }
    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }
    public BigDecimal getUnitPrice() { return unitPrice; }
    public void setUnitPrice(BigDecimal unitPrice) { this.unitPrice = unitPrice; }
}
