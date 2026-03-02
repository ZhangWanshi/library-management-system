const app = $("#app");

function loadPage(page) {
    app.load(`/${page}.html`, function () {

        if (page === "dashboard") {
            initDashboard();
        }
    });
}

$(document).ready(function () {

    const token = localStorage.getItem("accessToken");

    if (token) {
        loadPage("dashboard");
    } else {
        loadPage("login");
    }
});