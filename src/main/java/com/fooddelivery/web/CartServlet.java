package com.fooddelivery.web;

import com.fooddelivery.dao.MenuDao;
import com.fooddelivery.model.CartItem;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/api/cart")
public class CartServlet extends JsonServlet {
    private final MenuDao menuDao = new MenuDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        json(response, summary(cart(request)));
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Map<?, ?> body = readJson(request);
            long itemId = ((Number) body.get("itemId")).longValue();
            int quantity = body.get("quantity") == null ? 1 : ((Number) body.get("quantity")).intValue();
            CartItem item = menuDao.findCartItem(itemId);
            if (item == null) {
                error(response, HttpServletResponse.SC_NOT_FOUND, "Menu item not found");
                return;
            }

            Map<Long, CartItem> cart = cart(request);
            if (!cart.isEmpty() && cart.values().iterator().next().getRestaurantId() != item.getRestaurantId()) {
                cart.clear();
            }
            CartItem existing = cart.get(itemId);
            if (existing == null) {
                item.setQuantity(Math.max(1, quantity));
                cart.put(itemId, item);
            } else {
                existing.setQuantity(existing.getQuantity() + Math.max(1, quantity));
            }
            json(response, summary(cart));
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            Map<?, ?> body = readJson(request);
            long itemId = ((Number) body.get("itemId")).longValue();
            int quantity = ((Number) body.get("quantity")).intValue();
            Map<Long, CartItem> cart = cart(request);
            if (quantity <= 0) {
                cart.remove(itemId);
            } else if (cart.containsKey(itemId)) {
                cart.get(itemId).setQuantity(quantity);
            }
            json(response, summary(cart));
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Map<Long, CartItem> cart = cart(request);
        if (request.getParameter("itemId") == null) {
            cart.clear();
        } else {
            cart.remove(Long.parseLong(request.getParameter("itemId")));
        }
        json(response, summary(cart));
    }

    @SuppressWarnings("unchecked")
    private Map<Long, CartItem> cart(HttpServletRequest request) {
        Object value = request.getSession().getAttribute("cart");
        if (value instanceof Map) {
            return (Map<Long, CartItem>) value;
        }
        Map<Long, CartItem> cart = new LinkedHashMap<>();
        request.getSession().setAttribute("cart", cart);
        return cart;
    }

    private Map<String, Object> summary(Map<Long, CartItem> cart) {
        List<CartItem> items = new ArrayList<>(cart.values());
        BigDecimal subtotal = items.stream().map(CartItem::getLineTotal).reduce(BigDecimal.ZERO, BigDecimal::add);
        BigDecimal deliveryFee = subtotal.compareTo(BigDecimal.ZERO) == 0 || subtotal.compareTo(BigDecimal.valueOf(499)) >= 0 ? BigDecimal.ZERO : BigDecimal.valueOf(35);
        BigDecimal tax = subtotal.multiply(BigDecimal.valueOf(0.05));
        return Map.of(
                "items", items,
                "subtotal", subtotal,
                "deliveryFee", deliveryFee,
                "tax", tax,
                "total", subtotal.add(deliveryFee).add(tax)
        );
    }
}
