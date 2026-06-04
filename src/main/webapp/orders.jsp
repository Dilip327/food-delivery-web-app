<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Orders | FoodieExpress</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<main class="container py-4">
    <h1 class="h3">Order history</h1>
    <div id="ordersList" class="orders-list mt-3"></div>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
<script>window.Foodie && Foodie.loadOrders();</script>
</body>
</html>
