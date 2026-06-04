package com.fooddelivery.web;

import com.fooddelivery.model.User;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public abstract class JsonServlet extends HttpServlet {
    protected final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, (com.google.gson.JsonSerializer<LocalDateTime>) (src, type, context) -> context.serialize(src.toString()))
            .create();

    protected Map<?, ?> readJson(HttpServletRequest request) throws IOException {
        StringBuilder body = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                body.append(line);
            }
        }
        if (body.length() == 0) {
            return new HashMap<>();
        }
        return gson.fromJson(body.toString(), Map.class);
    }

    protected void json(HttpServletResponse response, Object payload) throws IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(gson.toJson(payload));
    }

    protected void error(HttpServletResponse response, int status, String message) throws IOException {
        response.setStatus(status);
        json(response, Map.of("error", message));
    }

    protected void handleError(HttpServletResponse response, Exception e) throws IOException {
        if (e instanceof IllegalArgumentException) {
            error(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());
        } else if (e instanceof SQLException) {
            error(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Database error");
        } else {
            error(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Server error");
        }
    }

    protected User currentUser(HttpServletRequest request) {
        return (User) request.getSession().getAttribute("user");
    }

    protected User requireUser(HttpServletRequest request, HttpServletResponse response) throws IOException {
        User user = currentUser(request);
        if (user == null) {
            error(response, HttpServletResponse.SC_UNAUTHORIZED, "Please sign in first");
        }
        return user;
    }

    protected String string(Map<?, ?> body, String key) {
        Object value = body.get(key);
        return value == null ? "" : String.valueOf(value).trim();
    }
}
