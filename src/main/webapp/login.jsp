<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sign in | FoodieExpress</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<main class="auth-wrap">
    <section class="auth-panel">
        <ul class="nav nav-tabs" id="authTabs">
            <li class="nav-item"><button class="nav-link active" data-bs-toggle="tab" data-bs-target="#loginPane" type="button">Sign in</button></li>
            <li class="nav-item"><button class="nav-link" data-bs-toggle="tab" data-bs-target="#registerPane" type="button">Create account</button></li>
        </ul>
        <div class="tab-content pt-3">
            <form class="tab-pane fade show active" id="loginForm">
                <input class="form-control mb-2" name="email" type="email" placeholder="Email" required>
                <input class="form-control mb-2" name="password" type="password" placeholder="Password" required>
                <button class="btn btn-brand w-100" type="submit">Sign in</button>
            </form>
            <form class="tab-pane fade" id="registerForm">
                <input class="form-control mb-2" name="name" placeholder="Name" required>
                <input class="form-control mb-2" name="email" type="email" placeholder="Email" required>
                <input class="form-control mb-2" name="phone" placeholder="Phone" required>
                <input class="form-control mb-2" name="password" type="password" placeholder="Password" required>
                <button class="btn btn-brand w-100" type="submit">Create account</button>
            </form>
        </div>
        <p id="authMessage" class="small mt-3 mb-0"></p>
    </section>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
<script>window.Foodie && Foodie.bindAuth();</script>
</body>
</html>
