(function () {
    const ctx = window.APP_CONTEXT || "";
    let activeRestaurant = null;

    async function api(path, options = {}) {
        const response = await fetch(ctx + path, {
            headers: { "Content-Type": "application/json" },
            ...options
        });
        const data = await response.json();
        if (!response.ok) {
            throw new Error(data.error || "Request failed");
        }
        return data;
    }

    function money(value) {
        return "₹" + Number(value || 0).toFixed(2);
    }

    function params() {
        return new URLSearchParams(window.location.search);
    }

    function restaurantCard(restaurant) {
        return `
            <a class="restaurant-card text-decoration-none text-reset" href="${ctx}/restaurant.jsp?id=${restaurant.id}">
                <img src="${restaurant.imageUrl}" alt="${restaurant.name}">
                <div class="body">
                    <div class="d-flex justify-content-between gap-2">
                        <h3 class="h5 mb-1">${restaurant.name}</h3>
                        <span class="pill">★ ${restaurant.rating}</span>
                    </div>
                    <p class="text-muted mb-2">${restaurant.cuisine}</p>
                    <div class="meta-row">
                        <span>${restaurant.deliveryTimeMinutes} min</span>
                        <span>${money(restaurant.deliveryFee)} delivery</span>
                        <span>Min ${money(restaurant.minOrderAmount)}</span>
                    </div>
                </div>
            </a>`;
    }

    function menuCard(item) {
        return `
            <article class="menu-card">
                <div>
                    <span class="veg-dot ${item.veg ? "" : "nonveg"}"></span>
                    <span class="small text-muted">${item.category}</span>
                    <h3 class="h5 mb-1">${item.name}</h3>
                    <p class="text-muted mb-2">${item.description || ""}</p>
                    <div class="d-flex justify-content-between align-items-center">
                        <span class="price">${money(item.price)}</span>
                        <button class="btn btn-sm btn-brand" data-add="${item.id}">Add</button>
                    </div>
                </div>
                <img src="${item.imageUrl}" alt="${item.name}">
            </article>`;
    }

    async function updateCartCount() {
        try {
            const cart = await api("/api/cart");
            const count = cart.items.reduce((sum, item) => sum + item.quantity, 0);
            const badge = document.getElementById("cartCount");
            if (badge) badge.textContent = count;
        } catch (e) {
            // Cart count should never block page rendering.
        }
    }

    async function loadHome() {
        const node = document.getElementById("homeRestaurants");
        const restaurants = await api("/api/restaurants");
        node.innerHTML = restaurants.slice(0, 4).map(restaurantCard).join("");
        updateCartCount();
    }

    async function loadRestaurants() {
        const form = document.getElementById("restaurantSearch");
        const input = document.getElementById("searchInput");
        input.value = params().get("q") || "";

        async function render() {
            const q = input.value.trim();
            const restaurants = await api("/api/restaurants" + (q ? "?q=" + encodeURIComponent(q) : ""));
            document.getElementById("restaurantsList").innerHTML = restaurants.length
                ? restaurants.map(restaurantCard).join("")
                : `<p class="text-muted">No restaurants matched your search.</p>`;
        }

        form.addEventListener("submit", function (event) {
            event.preventDefault();
            render();
        });
        await render();
        updateCartCount();
    }

    async function loadRestaurantDetail() {
        const id = params().get("id");
        if (!id) return;
        const detail = await api(`/api/restaurants/${id}`);
        activeRestaurant = detail.restaurant;
        const hero = document.getElementById("restaurantHero");
        hero.style.background = `linear-gradient(180deg, rgba(17,24,39,.15), rgba(17,24,39,.95)), url("${activeRestaurant.imageUrl}") center/cover`;
        hero.innerHTML = `
            <div class="inner">
                <div class="container">
                    <p class="eyebrow">${activeRestaurant.cuisine}</p>
                    <h1 class="display-5 fw-bold">${activeRestaurant.name}</h1>
                    <div class="meta-row text-white-75">
                        <span>★ ${activeRestaurant.rating}</span>
                        <span>${activeRestaurant.deliveryTimeMinutes} min</span>
                        <span>${money(activeRestaurant.deliveryFee)} delivery</span>
                    </div>
                </div>
            </div>`;
        document.getElementById("restaurantAddress").textContent = activeRestaurant.address;
        if (window.google) {
            initMap();
        }

        const menu = await api(`/api/restaurants/${id}/menu`);
        document.getElementById("menuList").innerHTML = menu.map(menuCard).join("");
        document.getElementById("menuList").addEventListener("click", async function (event) {
            const button = event.target.closest("[data-add]");
            if (!button) return;
            await api("/api/cart", {
                method: "POST",
                body: JSON.stringify({ itemId: Number(button.dataset.add), quantity: 1 })
            });
            button.textContent = "Added";
            setTimeout(() => button.textContent = "Add", 900);
            updateCartCount();
        });
        if (!window.GOOGLE_MAPS_API_KEY) {
            document.getElementById("map").textContent = "Set GOOGLE_MAPS_API_KEY to enable live maps";
        }
        updateCartCount();
    }

    function initMap() {
        if (!activeRestaurant || !window.google) return;
        const location = {
            lat: Number(activeRestaurant.latitude || 19.076),
            lng: Number(activeRestaurant.longitude || 72.8777)
        };
        const map = new google.maps.Map(document.getElementById("map"), {
            center: location,
            zoom: 14,
            disableDefaultUI: true
        });
        new google.maps.Marker({ position: location, map, title: activeRestaurant.name });
    }

    function renderCart(cart) {
        const itemsNode = document.getElementById("cartItems");
        const totalsNode = document.getElementById("cartTotals");
        if (!cart.items.length) {
            itemsNode.innerHTML = `<div class="checkout-panel"><p class="mb-2">Your cart is empty.</p><a href="${ctx}/restaurants.jsp" class="btn btn-brand">Browse restaurants</a></div>`;
        } else {
            itemsNode.innerHTML = cart.items.map(item => `
                <article class="menu-card">
                    <div>
                        <h3 class="h5 mb-1">${item.name}</h3>
                        <p class="text-muted mb-2">${item.restaurantName}</p>
                        <span class="price">${money(item.price)}</span>
                    </div>
                    <div class="d-flex align-items-center justify-content-end gap-2">
                        <button class="btn btn-outline-dark btn-sm" data-dec="${item.menuItemId}">−</button>
                        <strong>${item.quantity}</strong>
                        <button class="btn btn-outline-dark btn-sm" data-inc="${item.menuItemId}">+</button>
                    </div>
                </article>`).join("");
        }
        totalsNode.innerHTML = `
            <div><span>Subtotal</span><strong>${money(cart.subtotal)}</strong></div>
            <div><span>Delivery</span><strong>${money(cart.deliveryFee)}</strong></div>
            <div><span>Tax</span><strong>${money(cart.tax)}</strong></div>
            <div class="fs-5"><span>Total</span><strong>${money(cart.total)}</strong></div>`;
    }

    async function loadCartPage() {
        let cart = await api("/api/cart");
        renderCart(cart);
        document.getElementById("cartItems").addEventListener("click", async function (event) {
            const inc = event.target.closest("[data-inc]");
            const dec = event.target.closest("[data-dec]");
            if (!inc && !dec) return;
            const itemId = Number((inc || dec).dataset[inc ? "inc" : "dec"]);
            const item = cart.items.find(entry => entry.menuItemId === itemId);
            const quantity = item.quantity + (inc ? 1 : -1);
            cart = await api("/api/cart", { method: "PUT", body: JSON.stringify({ itemId, quantity }) });
            renderCart(cart);
            updateCartCount();
        });
        document.getElementById("checkoutForm").addEventListener("submit", async function (event) {
            event.preventDefault();
            const body = Object.fromEntries(new FormData(event.target).entries());
            const message = document.getElementById("checkoutMessage");
            try {
                const result = await api("/api/orders", { method: "POST", body: JSON.stringify(body) });
                message.className = "small text-success mt-2 mb-0";
                message.textContent = `Order #${result.orderId} placed successfully.`;
                setTimeout(() => window.location.href = ctx + "/orders.jsp", 900);
            } catch (error) {
                message.className = "small text-danger mt-2 mb-0";
                message.textContent = error.message;
            }
        });
        updateCartCount();
    }

    function bindAuth() {
        async function submit(form, path) {
            const message = document.getElementById("authMessage");
            try {
                await api(path, { method: "POST", body: JSON.stringify(Object.fromEntries(new FormData(form).entries())) });
                window.location.href = ctx + "/restaurants.jsp";
            } catch (error) {
                message.className = "small text-danger mt-3 mb-0";
                message.textContent = error.message;
            }
        }
        document.getElementById("loginForm").addEventListener("submit", event => {
            event.preventDefault();
            submit(event.target, "/api/auth/login");
        });
        document.getElementById("registerForm").addEventListener("submit", event => {
            event.preventDefault();
            submit(event.target, "/api/auth/register");
        });
        updateCartCount();
    }

    async function loadOrders() {
        const node = document.getElementById("ordersList");
        try {
            const orders = await api("/api/orders");
            node.innerHTML = orders.length ? orders.map(order => `
                <article class="order-card">
                    <div class="d-flex justify-content-between gap-3">
                        <div>
                            <h2 class="h5 mb-1">Order #${order.id}</h2>
                            <p class="text-muted mb-2">${order.restaurantName} · ${order.createdAt}</p>
                        </div>
                        <span class="pill">${order.status.replaceAll("_", " ")}</span>
                    </div>
                    <p class="mb-2">${order.items.map(item => `${item.quantity} x ${item.itemName}`).join(", ")}</p>
                    <strong>${money(order.total)}</strong>
                </article>`).join("") : `<p class="text-muted">You have not placed an order yet.</p>`;
        } catch (error) {
            node.innerHTML = `<div class="checkout-panel"><p class="mb-2">${error.message}</p><a class="btn btn-brand" href="${ctx}/login.jsp">Sign in</a></div>`;
        }
        updateCartCount();
    }

    document.addEventListener("click", async function (event) {
        if (!event.target.matches("#logoutBtn")) return;
        await api("/api/auth/logout", { method: "POST" });
        window.location.href = ctx + "/index.jsp";
    });

    window.Foodie = {
        loadHome,
        loadRestaurants,
        loadRestaurantDetail,
        loadCartPage,
        bindAuth,
        loadOrders,
        initMap
    };
    document.addEventListener("DOMContentLoaded", updateCartCount);
})();
