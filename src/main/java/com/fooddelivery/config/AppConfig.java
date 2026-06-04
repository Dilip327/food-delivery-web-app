package com.fooddelivery.config;

public final class AppConfig {
    private AppConfig() {
    }

    public static String dbUrl() {
        return value("DB_URL", "jdbc:mysql://localhost:3306/food_delivery?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC");
    }

    public static String dbUser() {
        return value("DB_USER", "root");
    }

    public static String dbPassword() {
        return value("DB_PASSWORD", "");
    }

    public static String googleMapsApiKey() {
        return value("GOOGLE_MAPS_API_KEY", "");
    }

    private static String value(String key, String fallback) {
        String env = System.getenv(key);
        if (env == null || env.trim().isEmpty()) {
            return fallback;
        }
        return env.trim();
    }
}
