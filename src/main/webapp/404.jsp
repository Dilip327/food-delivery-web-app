<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Not found | FoodieExpress</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="<%= request.getContextPath() %>/assets/css/styles.css">
</head>
<body>
<jsp:include page="/WEB-INF/jsp/header.jsp"/>
<main class="container py-5">
    <h1>Page not found</h1>
    <p class="text-muted">The page you requested is not available.</p>
    <a class="btn btn-brand" href="<%= request.getContextPath() %>/index.jsp">Back home</a>
</main>
<jsp:include page="/WEB-INF/jsp/footer.jsp"/>
</body>
</html>
