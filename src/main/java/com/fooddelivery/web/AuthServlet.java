package com.fooddelivery.web;

import com.fooddelivery.dao.UserDao;
import com.fooddelivery.model.User;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet("/api/auth/*")
public class AuthServlet extends JsonServlet {
    private final UserDao userDao = new UserDao();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        if (request.getPathInfo() != null && request.getPathInfo().equals("/me")) {
            Map<String, Object> payload = new HashMap<>();
            payload.put("user", currentUser(request));
            json(response, payload);
            return;
        }
        error(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String path = request.getPathInfo() == null ? "" : request.getPathInfo();
            if (path.equals("/logout")) {
                request.getSession().invalidate();
                json(response, Map.of("ok", true));
                return;
            }

            Map<?, ?> body = readJson(request);
            User user;
            if (path.equals("/register")) {
                String password = string(body, "password");
                if (password.length() < 8) {
                    throw new IllegalArgumentException("Password must be at least 8 characters");
                }
                user = userDao.register(string(body, "name"), string(body, "email"), string(body, "phone"), password);
            } else if (path.equals("/login")) {
                user = userDao.authenticate(string(body, "email"), string(body, "password"));
                if (user == null) {
                    error(response, HttpServletResponse.SC_UNAUTHORIZED, "Invalid email or password");
                    return;
                }
            } else {
                error(response, HttpServletResponse.SC_NOT_FOUND, "Endpoint not found");
                return;
            }
            request.getSession().setAttribute("user", user);
            json(response, Map.of("user", user));
        } catch (Exception e) {
            handleError(response, e);
        }
    }
}
