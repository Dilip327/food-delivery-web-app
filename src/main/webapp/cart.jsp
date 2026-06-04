<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Cart | FoodieExpress</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<main class="container py-4">
    <h1 class="h3">Your cart</h1>
    <div class="row g-4 mt-1">
        <div class="col-lg-7">
            <div id="cartItems" class="menu-list"></div>
        </div>
        <div class="col-lg-5">
            <div class="checkout-panel">
                <h2 class="h5">Checkout</h2>
                <div id="cartTotals" class="totals"></div>
                <form id="checkoutForm" class="mt-3">
                    <div class="row g-2">
                        <div class="col-md-6"><input class="form-control" name="name" placeholder="Full name" required></div>
                        <div class="col-md-6"><input class="form-control" name="phone" placeholder="Phone" required></div>
                        <div class="col-12"><textarea class="form-control" name="address" rows="3" placeholder="Delivery address" required></textarea></div>
                        <div class="col-12">
                            <select class="form-select" name="paymentMethod">
                                <option value="COD">Cash on delivery</option>
                                <option value="UPI">UPI</option>
                                <option value="CARD">Card</option>
                            </select>
                        </div>
                    </div>
                    <button class="btn btn-brand w-100 mt-3" type="submit">Place order</button>
                    <p id="checkoutMessage" class="small mt-2 mb-0"></p>
                </form>
            </div>
        </div>
    </div>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
<script>window.Foodie && Foodie.loadCartPage();</script>
</body>
</html>
