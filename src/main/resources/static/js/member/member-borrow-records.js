function showBorrowRecords() {

    $("#content").html(`

        <h4>My Borrow Records</h4>

        <div id="returnSuccess" class="alert alert-success d-none"></div>

        <table id="borrowTable" class="display" style="width:100%">

            <thead>
                <tr>
                    <th>ID</th>
                    <th>Title</th>
                    <th>Author</th>
                    <th>ISBN</th>
                    <th>Borrow Date</th>
                    <th>Due Date</th>
                    <th>Return Date</th>
                    <th>Status</th>
                    <th>Action</th>
                </tr>
            </thead>

        </table>
    `);

    loadBorrowRecords();
}

function loadBorrowRecords() {

    const token = localStorage.getItem("accessToken");

    $('#borrowTable').DataTable({

        destroy: true,

        ajax: {

            url: "/api/borrowing/my-records",

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

            { data: "bookTitle" },

            { data: "bookAuthor" },

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
            },

            {
                data: "status",

                render: function (data, type, row) {

                    if (data === "BORROWED") {

                        return `<button class="btn btn-sm btn-library"
                            onclick="returnBookFromRecord(${row.id})">
                            Return
                        </button>`;
                    }

                    return "-";
                }
            }

        ]

    });

}

function returnBookFromRecord(recordId) {

    const token = localStorage.getItem("accessToken");

    $.ajax({

        url: "/api/borrowing/return/" + recordId,

        type: "POST",

        beforeSend: function (xhr) {

            xhr.setRequestHeader(
                "Authorization",
                "Bearer " + token
            );
        },

        success: function () {

            $("#returnSuccess")
                .removeClass("d-none")
                .text("Book returned successfully!");

            loadBorrowRecords();
        },

        error: function () {

            alert("Failed to return book");
        }

    });
}