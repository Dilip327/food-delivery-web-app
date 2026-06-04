<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>FoodieExpress | Food Delivery</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<main>
    <section class="hero-section">
        <div class="container hero-content">
            <div class="row align-items-center min-vh-75">
                <div class="col-lg-7">
                    <p class="eyebrow">Fast local delivery</p>
                    <h1 class="display-4 fw-bold">Order meals from top restaurants near you</h1>
                    <p class="lead text-white-75">Search by cuisine, compare ratings, build one-restaurant carts, and track every order from checkout to delivery.</p>
                    <form class="search-shell" action="<%= request.getContextPath() %>/restaurants.jsp">
                        <input class="form-control form-control-lg" name="q" placeholder="Search biryani, pizza, dosa, bowls">
                        <button class="btn btn-brand btn-lg" type="submit">Find food</button>
                    </form>
                </div>
            </div>
        </div>
    </section>
    <section class="container py-5">
        <div class="section-title">
            <h2>Popular right now</h2>
            <a href="<%= request.getContextPath() %>/restaurants.jsp">View all</a>
        </div>
        <div id="homeRestaurants" class="restaurant-grid"></div>
    </section>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
<script>window.Foodie && Foodie.loadHome();</script>
</body>
</html>
