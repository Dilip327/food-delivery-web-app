<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Restaurants | FoodieExpress</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<main class="container py-4">
    <div class="toolbar">
        <div>
            <h1 class="h3 mb-1">Restaurants</h1>
            <p class="text-muted mb-0">Browse open kitchens and order from one restaurant per cart.</p>
        </div>
        <form class="d-flex gap-2" id="restaurantSearch">
            <input class="form-control" id="searchInput" placeholder="Search cuisine or restaurant">
            <button class="btn btn-brand" type="submit">Search</button>
        </form>
    </div>
    <div id="restaurantsList" class="restaurant-grid mt-4"></div>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
<script>window.Foodie && Foodie.loadRestaurants();</script>
</body>
</html>
