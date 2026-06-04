# Internet Deployment Guide

This project is ready to deploy as a Dockerized Tomcat web application. The app needs:

- A Java/Tomcat web service
- A MySQL database
- These environment variables:

```text
DB_URL=jdbc:mysql://YOUR_DB_HOST:3306/food_delivery?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
DB_USER=YOUR_DB_USER
DB_PASSWORD=YOUR_DB_PASSWORD
GOOGLE_MAPS_API_KEY=YOUR_GOOGLE_MAPS_KEY
```

`GOOGLE_MAPS_API_KEY` is optional.

## Option 1: Deploy With Docker On A VPS

1. Install Docker and Docker Compose on your server.

2. Point your domain DNS `A` record to your server public IP.

3. Upload this project folder to the server.

4. Create your production environment file:

```bash
cp .env.example .env
nano .env
```

Set:

```text
DOMAIN=yourdomain.com
MYSQL_ROOT_PASSWORD=strong_root_password
DB_USER=food_app
DB_PASSWORD=strong_app_password
GOOGLE_MAPS_API_KEY=
```

5. Start the production containers:

```bash
docker compose -f docker-compose.prod.yml up -d --build
```

6. Open:

```text
https://yourdomain.com
```

Caddy automatically requests and renews HTTPS certificates.

If you do not have a domain yet, use the basic local-style compose file:

```bash
docker compose up -d --build
```

Then open:

```text
http://YOUR_SERVER_IP:8080
```

## Option 2: Deploy To A Cloud App Platform

Use a platform that supports Docker web services and MySQL, such as Render, Railway, Fly.io, Azure App Service, AWS Elastic Beanstalk, or Google Cloud Run.

General steps:

1. Push this project to GitHub.

2. Create a MySQL database on the platform.

3. Import:

```text
database/schema.sql
```

4. Create a Docker web service from this repository.

5. Set the environment variables listed above.

6. Deploy. The Docker image exposes port `8080`.

## Local Docker Test

Before deploying online, test locally:

```bash
docker compose up --build
```

Then open:

```text
http://localhost:8080
```

## Production Notes

- Use a strong database password.
- Do not expose MySQL directly to the public internet.
- Use HTTPS on your domain.
- Restrict your Google Maps API key by domain.
- Keep `DB_PASSWORD` and API keys in platform secrets, not source code.
