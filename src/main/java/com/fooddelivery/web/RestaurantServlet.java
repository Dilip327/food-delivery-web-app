package com.fooddelivery.web;

import com.fooddelivery.dao.MenuDao;
import com.fooddelivery.dao.RestaurantDao;
import com.fooddelivery.model.Restaurant;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@WebServlet("/api/restaurants/*")
public class RestaurantServlet extends JsonServlet {
    private final RestaurantDao restaurantDao = new RestaurantDao();
    private final MenuDao menuDao = new MenuDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String path = request.getPathInfo();
            if (path == null || path.equals("/")) {
                json(response, restaurantDao.findAll(request.getParameter("q")));
                return;
            }

            String[] parts = path.substring(1).split("/");
            long id = Long.parseLong(parts[0]);
            if (parts.length == 2 && parts[1].equals("menu")) {
                json(response, menuDao.findByRestaurant(id));
                return;
            }

            Restaurant restaurant = restaurantDao.findById(id);
            if (restaurant == null) {
                error(response, HttpServletResponse.SC_NOT_FOUND, "Restaurant not found");
                return;
            }
            json(response, Map.of("restaurant", restaurant));
        } catch (Exception e) {
            handleError(response, e);
        }
    }
}
