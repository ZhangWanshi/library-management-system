function initDashboard() {

    const role = localStorage.getItem("role");
    const username = localStorage.getItem("username");

    $("#nav-username").text(username);

    $("#homeBtn").on("click", goHome);
    $("#logoutBtn").on("click", logout);

    buildSidebar(role);

    if (role === "ADMIN") {
        loadAdminDashboard();
    }
}

function buildSidebar(role) {

    let sidebarHtml = "";

    if (role === "ADMIN") {
        sidebarHtml += `
            <button id="menuAnalyticDashboard" class="btn btn-dark w-100 mb-2"
                onclick="loadAdminDashboard()">
                Analytic Dashboard
            </button>
            <button id="menuUserManagement" class="btn btn-dark w-100 mb-2"
                onclick="showUserManagement()">
                User Management
            </button>
            <button id="menuBorrowingRules" class="btn btn-dark w-100 mb-2"
                onclick="showRuleManagement()">
                Borrowing Rules
            </button>
        `;
    }

    if (role === "LIBRARIAN") {
        sidebarHtml += `
            <button id="menuBookManagement" class="btn btn-dark w-100 mb-2"
                onclick="showBookManagement()">
                Book Management
            </button>
            <button id="menuBorrowManagement" class="btn btn-dark w-100 mb-2"
                onclick="showBorrowManagement()">
                Borrow Management
            </button>
        `;
    }

    if (role === "MEMBER") {
        sidebarHtml += `
            <button id="menuBookList" class="btn btn-dark w-100 mb-2" 
                onclick="showMemberBooks()"> 
                Books List
            </button>
            <button id="menuMyBorrowing" class="btn btn-dark w-100 mb-2"
                onclick="showBorrowRecords()">
                My Borrowing
            </button>
        `;
    }

    $("#sidebar").html(sidebarHtml);
}

function goHome() {

    const role = localStorage.getItem("role");

    if (role === "ADMIN") {
        loadAdminDashboard();
        return;
    }

    $("#content").html(`
        <h4>Dashboard</h4>
        <p>Select an option from the sidebar.</p>
    `);
}