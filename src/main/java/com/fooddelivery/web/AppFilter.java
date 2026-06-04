package com.fooddelivery.web;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter("/*")
public class AppFilter implements Filter {
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (response instanceof HttpServletResponse) {
            HttpServletResponse http = (HttpServletResponse) response;
            http.setHeader("X-Content-Type-Options", "nosniff");
            http.setHeader("X-Frame-Options", "SAMEORIGIN");
            http.setHeader("Referrer-Policy", "strict-origin-when-cross-origin");
        }

        if (request instanceof HttpServletRequest) {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpSession session = httpRequest.getSession(false);
            if (session != null) {
                session.setMaxInactiveInterval(30 * 60);
            }
        }

        chain.doFilter(request, response);
    }
}
