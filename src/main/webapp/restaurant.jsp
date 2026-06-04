<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.fooddelivery.config.AppConfig" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Menu | FoodieExpress</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<main>
    <section id="restaurantHero" class="restaurant-hero"></section>
    <section class="container py-4">
        <div class="row g-4">
            <div class="col-lg-8">
                <div class="section-title">
                    <h2>Menu</h2>
                    <span class="text-muted">Freshly prepared favorites</span>
                </div>
                <div id="menuList" class="menu-list"></div>
            </div>
            <aside class="col-lg-4">
                <div class="checkout-panel sticky-lg-top">
                    <h3 class="h5">Delivery location</h3>
                    <div id="map" class="map-box"></div>
                    <p id="restaurantAddress" class="small text-muted mt-2"></p>
                    <a class="btn btn-brand w-100 mt-2" href="<%= request.getContextPath() %>/cart.jsp">Go to cart</a>
                </div>
            </aside>
        </div>
    </section>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
<script>window.GOOGLE_MAPS_API_KEY = "<%= AppConfig.googleMapsApiKey() %>";</script>
<script>window.Foodie && Foodie.loadRestaurantDetail();</script>
<% if (!AppConfig.googleMapsApiKey().isEmpty()) { %>
<script async defer src="https://maps.googleapis.com/maps/api/js?key=<%= AppConfig.googleMapsApiKey() %>&callback=Foodie.initMap"></script>
<% } %>
</body>
</html>
