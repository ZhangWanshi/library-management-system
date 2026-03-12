function initDashboard() {

    const role = localStorage.getItem("role");
    const username = localStorage.getItem("username");

    $("#nav-username").text(username);

    $("#homeBtn").on("click", goHome);
    $("#logoutBtn").on("click", logout);

    buildSidebar(role);
}

function buildSidebar(role) {

    let sidebarHtml = "";

    if (role === "ADMIN") {
        sidebarHtml += `
            <button class="btn btn-dark w-100 mb-2"
                onclick="showUserManagement()">
                User Management
            </button>
            <button class="btn btn-dark w-100 mb-2"
                onclick="showRuleManagement()">
                Configure Borrow Rules
            </button>
        `;
    }

    if (role === "LIBRARIAN") {
        sidebarHtml += `
            <button class="btn btn-dark w-100 mb-2"
                onclick="showBookManagement()">
                Book Management
            </button>
        `;
    }

    if (role === "MEMBER") {
        sidebarHtml += `
            <button class="btn btn-dark w-100 mb-2" onclick="showMemberBooks()"> Browse & Borrow Books
            </button>
        `;
    }

    $("#sidebar").html(sidebarHtml);
}

function goHome() {
    $("#content").html(`
        <h4>Dashboard</h4>
        <p>Select an option from the sidebar.</p>
    `);
}