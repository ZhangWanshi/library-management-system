function showUserManagement() {

    $("#content").html(`
        <h4>User Management</h4>

        <button class="btn btn-library mb-3"
            onclick="openCreateUserModal()">
            Add New User
        </button>

        <table id="usersTable" class="display" style="width:100%">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Username</th>
                    <th>Email</th>
                    <th>Role</th>
                </tr>
            </thead>
        </table>
    `);

    loadUsers();
}

function loadUsers() {

    const token = localStorage.getItem("accessToken");

    $('#usersTable').DataTable({

        destroy: true,

        ajax: {
            url: "/api/users",
            type: "GET",

            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Bearer " + token);
            },

            dataSrc: "",

            error: function (xhr) {

                if (xhr.status === 401 || xhr.status === 403) {

                    alert("Session expired. Please login again.");

                    localStorage.clear();

                    loadPage("login");
                }
            }
        },

        columns: [
            { data: "id" },
            { data: "username" },
            { data: "email" },
            { data: "role" }
        ]
    });
}

function openCreateUserModal() {

    $("#createUserForm")[0].reset();

    $("#createUserAlert")
        .addClass("d-none")
        .text("");

    const modal = new bootstrap.Modal(
        document.getElementById('createUserModal')
    );

    modal.show();
}

$(document).on("click", "#createUserBtn", function () {

    const username = $("#newUsername").val().trim();
    const email = $("#newEmail").val().trim();
    const password = $("#newPassword").val();
    const role = $("#newRole").val();

    if (!username || !email || !password || !role) {
        showCreateUserError("All fields are required.");
        return;
    }

    const token = localStorage.getItem("accessToken");

    $.ajax({
        url: "/api/users",
        type: "POST",
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        },
        data: JSON.stringify({ username, email, password, role }),

        success: function () {

            const modalEl = document.getElementById('createUserModal');
            const modal = bootstrap.Modal.getInstance(modalEl);

            modal.hide();

            loadUsers();

            $("#content").prepend(`
        <div class="alert alert-success alert-dismissible fade show">
            User created successfully!
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        </div>
    `);
        },

        error: function (xhr) {

            let msg = "Failed to create user.";

            if (xhr.responseJSON) {

                const res = xhr.responseJSON;

                if (res.username ||
                    res.password ||
                    res.email ||
                    res.role) {

                    msg = Object.values(res).join("\n");
                }
                else if (res.error) {
                    msg = res.error;
                }
            }

            showCreateUserError(msg);
        }
    });
});

function showCreateUserError(message) {

    $("#createUserAlert")
        .removeClass("d-none")
        .text(message);
}