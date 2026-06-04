<%@ page import="com.fooddelivery.model.User" %>
<%
    String ctx = request.getContextPath();
    User currentUser = (User) session.getAttribute("user");
%>
<nav class="navbar navbar-expand-lg sticky-top bg-white border-bottom">
    <div class="container">
        <a class="navbar-brand fw-bold text-brand" href="<%= ctx %>/index.jsp">FoodieExpress</a>
        <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#mainNav">
            <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="mainNav">
            <ul class="navbar-nav ms-auto align-items-lg-center gap-lg-2">
                <li class="nav-item"><a class="nav-link" href="<%= ctx %>/restaurants.jsp">Restaurants</a></li>
                <li class="nav-item"><a class="nav-link" href="<%= ctx %>/orders.jsp">Orders</a></li>
                <li class="nav-item">
                    <a class="btn btn-outline-dark position-relative" href="<%= ctx %>/cart.jsp">
                        Cart <span id="cartCount" class="badge rounded-pill bg-brand">0</span>
                    </a>
                </li>
                <% if (currentUser == null) { %>
                <li class="nav-item"><a class="btn btn-brand" href="<%= ctx %>/login.jsp">Sign in</a></li>
                <% } else { %>
                <li class="nav-item"><span class="nav-link small text-muted">Hi, <%= currentUser.getName() %></span></li>
                <li class="nav-item"><button class="btn btn-link nav-link" id="logoutBtn" type="button">Logout</button></li>
                <% } %>
            </ul>
        </div>
    </div>
</nav>
<script>window.APP_CONTEXT = "<%= ctx %>";</script>
