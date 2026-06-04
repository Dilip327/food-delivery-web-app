# Food Delivery Web App

A production-oriented Java JSP/Servlet food delivery application inspired by Swiggy and Zomato. It includes restaurant discovery, menus, cart management, checkout, order history, REST APIs, MySQL persistence, Bootstrap UI, and Google Maps integration.

## Stack

- Java 11
- JSP + Servlets
- MySQL 8
- Maven WAR packaging
- Apache Tomcat 9
- HTML, CSS, JavaScript, Bootstrap 5
- Google Maps JavaScript API

## Quick Start

1. Create the database:

```sql
SOURCE database/schema.sql;
```

2. Configure environment variables:

```powershell
$env:DB_URL="jdbc:mysql://localhost:3306/food_delivery?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC"
$env:DB_USER="root"
$env:DB_PASSWORD="your_password"
$env:GOOGLE_MAPS_API_KEY="your_google_maps_key"
```

3. Build the WAR:

```powershell
mvn clean package
```

4. Deploy `target/food-delivery.war` to Tomcat 9 and open:

```text
http://localhost:8080/food-delivery/
```

## Deploy Online

Use the Docker-based guide in `DEPLOYMENT.md`. The included `Dockerfile` builds the JSP/Servlet app and deploys it as Tomcat `ROOT.war`.

## First Login

Create a customer account from the Sign in page. Passwords are hashed with BCrypt before storage.

## API Overview

- `GET /api/restaurants`
- `GET /api/restaurants/{id}`
- `GET /api/restaurants/{id}/menu`
- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/auth/me`
- `POST /api/auth/logout`
- `GET /api/cart`
- `POST /api/cart`
- `PUT /api/cart`
- `DELETE /api/cart?itemId=1`
- `GET /api/orders`
- `POST /api/orders`

## Notes

- Passwords are stored with BCrypt.
- Cart state is session-backed.
- Order and order items are persisted transactionally.
- Google Maps loads only when a `GOOGLE_MAPS_API_KEY` value is supplied.
