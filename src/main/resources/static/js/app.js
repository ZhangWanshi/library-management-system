const app = $("#app");

function loadPage(page) {

    app.load(`/${page}.html`, function () {

        if (page === "dashboard") {
            initDashboard();
        }
    });
}

function parseJwt(token) {

    try {

        const base64Payload = token.split('.')[1];

        const payload = atob(base64Payload);

        return JSON.parse(payload);

    } catch (e) {

        return null;
    }
}

function isTokenExpired(token) {

    const payload = parseJwt(token);

    if (!payload || !payload.exp) {
        return true;
    }

    const now = Math.floor(Date.now() / 1000);

    return payload.exp < now;
}

$(document).ready(function () {

    const token = localStorage.getItem("accessToken");

    if (!token) {
        loadPage("login");
        return;
    }

    if (isTokenExpired(token)) {

        console.log("Token expired, redirecting to login");

        localStorage.clear();

        loadPage("login");

        return;
    }

    loadPage("dashboard");
});