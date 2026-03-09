function login() {

    const username = $("#username").val();
    const password = $("#password").val();

    $.ajax({
        url: "/api/auth/login",
        type: "POST",
        contentType: "application/json",
        data: JSON.stringify({ username, password }),

        success: function (data) {

            if (!data.accessToken) {
                alert("Login failed");
                return;
            }

            localStorage.setItem("accessToken", data.accessToken);
            localStorage.setItem("role", data.role);
            localStorage.setItem("username", username);

            loadPage("dashboard");
        },

        error: function () {
            alert("Invalid credentials");
        }
    });
}

function logout() {
    localStorage.clear();
    loadPage("login");
}