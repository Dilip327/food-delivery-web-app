package com.fooddelivery.web;

import com.fooddelivery.dao.OrderDao;
import com.fooddelivery.model.CartItem;
import com.fooddelivery.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

@WebServlet("/api/orders")
public class OrderServlet extends JsonServlet {
    private final OrderDao orderDao = new OrderDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            User user = requireUser(request, response);
            if (user == null) {
                return;
            }
            json(response, orderDao.findByUser(user.getId()));
        } catch (Exception e) {
            handleError(response, e);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            User user = requireUser(request, response);
            if (user == null) {
                return;
            }
            Map<?, ?> body = readJson(request);
            Map<Long, CartItem> cart = cart(request);
            long orderId = orderDao.create(
                    user.getId(),
                    string(body, "name"),
                    string(body, "phone"),
                    string(body, "address"),
                    string(body, "paymentMethod").isEmpty() ? "COD" : string(body, "paymentMethod"),
                    new ArrayList<>(cart.values())
            );
            cart.clear();
            json(response, Map.of("orderId", orderId));
        } catch (Exception e) {
            handleError(response, e);
        }
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
}
