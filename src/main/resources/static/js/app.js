const app = document.getElementById("app");

function loadPage(page) {
    fetch(`/${page}.html`)
        .then(res => {
            if (!res.ok) {
                throw new Error("Page not found: " + page);
            }
            return res.text();
        })
        .then(html => app.innerHTML = html)
        .catch(err => {
            console.error(err);
            app.innerHTML = "<p>Error loading page</p>";
        });
}

function parseJwt(token) {
    const base64Payload = token.split('.')[1];
    const decodedPayload = atob(base64Payload);
    return JSON.parse(decodedPayload);
}

function login() {

    const username = document.getElementById("username").value;
    const password = document.getElementById("password").value;

    fetch("/auth/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ username, password })
    })
        .then(res => res.json())
        .then(data => {

            if (!data.accessToken) {
                alert("Login failed");
                return;
            }

            localStorage.setItem("accessToken", data.accessToken);
            localStorage.setItem("refreshToken", data.refreshToken);
            localStorage.setItem("role", data.role);

            redirectByRole(data.role);
        })
        .catch(err => {
            console.error(err);
            alert("Login error");
        });
}

function redirectByRole(role) {

    if (role === "ADMIN") {
        loadPage("admin");
    }
    else if (role === "LIBRARIAN") {
        loadPage("librarian");
    }
    else if (role === "MEMBER") {
        loadPage("member");
    }
    else {
        console.error("Unknown role:", role);
        loadPage("login");
    }
}

function authFetch(url, options = {}) {

    const token = localStorage.getItem("accessToken");

    options.headers = {
        ...options.headers,
        "Authorization": "Bearer " + token
    };

    return fetch(url, options);
}

function logout() {
    localStorage.clear();
    loadPage("login");
}

window.onload = () => {

    const token = localStorage.getItem("accessToken");
    const savedRole = localStorage.getItem("role");

    if (token) {
        try {

            const payload = parseJwt(token);

            const now = Math.floor(Date.now() / 1000);

            // token expired
            if (payload.exp < now) {
                localStorage.clear();
                loadPage("login");
                return;
            }

            redirectByRole(savedRole);

        } catch (error) {
            console.error("Invalid token:", error.message);

            localStorage.clear();
            loadPage("login");

            throw error;
        }
    } else {
        loadPage("login");
    }
};