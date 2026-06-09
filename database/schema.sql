DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS menu_items;
DROP TABLE IF EXISTS restaurants;
DROP TABLE IF EXISTS users;

CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(160) NOT NULL UNIQUE,
    phone VARCHAR(20) NOT NULL,
    password_hash VARCHAR(100) NOT NULL,
    role ENUM('CUSTOMER', 'ADMIN') NOT NULL DEFAULT 'CUSTOMER',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE restaurants (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(140) NOT NULL,
    cuisine VARCHAR(160) NOT NULL,
    description TEXT,
    image_url VARCHAR(500),
    rating DECIMAL(2,1) NOT NULL DEFAULT 4.0,
    delivery_time_minutes INT NOT NULL DEFAULT 30,
    delivery_fee DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    min_order_amount DECIMAL(8,2) NOT NULL DEFAULT 0.00,
    address VARCHAR(255) NOT NULL,
    latitude DECIMAL(10,7),
    longitude DECIMAL(10,7),
    is_open BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE menu_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    restaurant_id BIGINT NOT NULL,
    name VARCHAR(140) NOT NULL,
    description VARCHAR(500),
    category VARCHAR(80) NOT NULL,
    price DECIMAL(8,2) NOT NULL,
    image_url VARCHAR(500),
    is_veg BOOLEAN NOT NULL DEFAULT TRUE,
    is_available BOOLEAN NOT NULL DEFAULT TRUE,
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

CREATE TABLE orders (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL,
    restaurant_id BIGINT NOT NULL,
    status ENUM('PLACED', 'CONFIRMED', 'PREPARING', 'OUT_FOR_DELIVERY', 'DELIVERED', 'CANCELLED') NOT NULL DEFAULT 'PLACED',
    customer_name VARCHAR(100) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    delivery_address VARCHAR(255) NOT NULL,
    payment_method ENUM('COD', 'CARD', 'UPI') NOT NULL DEFAULT 'COD',
    subtotal DECIMAL(10,2) NOT NULL,
    delivery_fee DECIMAL(8,2) NOT NULL,
    tax DECIMAL(8,2) NOT NULL,
    total DECIMAL(10,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id),
    FOREIGN KEY (restaurant_id) REFERENCES restaurants(id)
);

CREATE TABLE order_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    order_id BIGINT NOT NULL,
    menu_item_id BIGINT NOT NULL,
    item_name VARCHAR(140) NOT NULL,
    quantity INT NOT NULL,
    unit_price DECIMAL(8,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (menu_item_id) REFERENCES menu_items(id)
);

INSERT INTO restaurants (name, cuisine, description, image_url, rating, delivery_time_minutes, delivery_fee, min_order_amount, address, latitude, longitude) VALUES
('Tandoori Junction', 'North Indian, Mughlai', 'Rich curries, smoky kebabs, and warm breads made for comfort dinners.', 'https://images.unsplash.com/photo-1585937421612-70a008356fbe?auto=format&fit=crop&w=900&q=80', 4.5, 32, 35.00, 149.00, 'Bandra West, Mumbai', 19.0607, 72.8362),
('Urban Dosa Co.', 'South Indian, Filter Coffee', 'Crisp dosas, fluffy idlis, and fresh chutneys from a fast casual kitchen.', 'https://images.unsplash.com/photo-1668236543090-82eba5ee5976?auto=format&fit=crop&w=900&q=80', 4.3, 24, 25.00, 99.00, 'Indiranagar, Bengaluru', 12.9719, 77.6412),
('Pizza Metro', 'Pizza, Italian', 'Stone-baked pizzas, pastas, and sides with quick city delivery.', 'https://images.unsplash.com/photo-1513104890138-7c749659a591?auto=format&fit=crop&w=900&q=80', 4.2, 28, 40.00, 199.00, 'Koregaon Park, Pune', 18.5362, 73.8937),
('Green Bowl Kitchen', 'Healthy, Salads, Bowls', 'Balanced bowls, cold-pressed juices, and high-protein lunch plates.', 'https://images.unsplash.com/photo-1512621776951-a57141f2eefd?auto=format&fit=crop&w=900&q=80', 4.6, 22, 20.00, 129.00, 'Jubilee Hills, Hyderabad', 17.4326, 78.4071);

INSERT INTO menu_items (restaurant_id, name, description, category, price, image_url, is_veg) VALUES
(1, 'Butter Chicken', 'Creamy tomato gravy with tender chicken tikka.', 'Recommended', 329.00, 'https://images.unsplash.com/photo-1603894584373-5ac82b2ae398?auto=format&fit=crop&w=700&q=80', FALSE),
(1, 'Paneer Lababdar', 'Paneer cubes in spiced onion tomato gravy.', 'Main Course', 279.00, 'https://images.unsplash.com/photo-1631452180519-c014fe946bc7?auto=format&fit=crop&w=700&q=80', TRUE),
(1, 'Garlic Naan', 'Tandoor naan brushed with garlic butter.', 'Breads', 69.00, 'https://images.unsplash.com/photo-1617692855027-33b14f061079?auto=format&fit=crop&w=700&q=80', TRUE),
(2, 'Masala Dosa', 'Crisp dosa filled with spiced potato masala.', 'Recommended', 139.00, 'https://images.unsplash.com/photo-1694849789325-914b71ab4075?auto=format&fit=crop&w=700&q=80', TRUE),
(2, 'Ghee Podi Idli', 'Mini idlis tossed with ghee and podi.', 'Breakfast', 119.00, 'https://images.unsplash.com/photo-1584195232185-55b5f615b381?auto=format&fit=crop&w=700&q=80', TRUE),
(2, 'Filter Coffee', 'Traditional hot filter coffee.', 'Beverages', 59.00, 'https://images.unsplash.com/photo-1497935586351-b67a49e012bf?auto=format&fit=crop&w=700&q=80', TRUE),
(3, 'Margherita Pizza', 'San Marzano tomato, mozzarella, and basil.', 'Pizza', 249.00, 'https://images.unsplash.com/photo-1604068549290-dea0e4a305ca?auto=format&fit=crop&w=700&q=80', TRUE),
(3, 'Pepperoni Pizza', 'Mozzarella, pepperoni, and house seasoning.', 'Pizza', 349.00, 'https://images.unsplash.com/photo-1628840042765-356cda07504e?auto=format&fit=crop&w=700&q=80', FALSE),
(3, 'Arrabbiata Pasta', 'Penne in spicy tomato garlic sauce.', 'Pasta', 229.00, 'https://images.unsplash.com/photo-1621996346565-e3dbc646d9a9?auto=format&fit=crop&w=700&q=80', TRUE),
(4, 'Protein Power Bowl', 'Brown rice, grilled paneer, greens, and tahini.', 'Bowls', 259.00, 'https://images.unsplash.com/photo-1543339308-43e59d6b73a6?auto=format&fit=crop&w=700&q=80', TRUE),
(4, 'Chicken Quinoa Bowl', 'Grilled chicken, quinoa, vegetables, and herb dressing.', 'Bowls', 299.00, 'https://images.unsplash.com/photo-1546069901-ba9599a7e63c?auto=format&fit=crop&w=700&q=80', FALSE),
(4, 'Detox Lemonade', 'Lemon, mint, cucumber, and sparkling water.', 'Beverages', 99.00, 'https://images.unsplash.com/photo-1513558161293-cdaf765ed2fd?auto=format&fit=crop&w=700&q=80', TRUE);
