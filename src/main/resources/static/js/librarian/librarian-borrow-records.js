function showBorrowManagement() {

    $("#content").html(`

        <h4>Borrowing Management</h4>

        <table id="borrowManagementTable" class="display" style="width:100%">

            <thead>
                <tr>
                    <th>ID</th>
                    <th>Member</th>
                    <th>Email</th>
                    <th>Book</th>
                    <th>ISBN</th>
                    <th>Borrow Date</th>
                    <th>Due Date</th>
                    <th>Return Date</th>
                    <th>Status</th>
                </tr>
            </thead>

        </table>
    `);

    loadBorrowManagement();
}

function loadBorrowManagement() {

    const token = localStorage.getItem("accessToken");

    $('#borrowManagementTable').DataTable({

        destroy: true,

        ajax: {

            url: "/api/borrowing/all-records",

            type: "GET",

            beforeSend: function (xhr) {

                xhr.setRequestHeader(
                    "Authorization",
                    "Bearer " + token
                );
            },

            dataSrc: ""
        },

        columns: [

            { data: "id" },

            { data: "memberUsername" },

            { data: "memberEmail" },

            { data: "bookTitle" },

            { data: "isbn" },

            { data: "borrowDate" },

            { data: "dueDate" },

            { data: "returnDate" },

            {
                data: "status",

                render: function (data) {

                    if (data === "BORROWED") {
                        return `<span class="badge bg-warning">Borrowed</span>`;
                    }

                    return `<span class="badge bg-success">Returned</span>`;
                }
            }

        ]

    });

}