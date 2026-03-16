function showBookManagement() {
    $("#content").html(`
        <h4>Book Management</h4>
        <button class="btn btn-library mb-3" onclick="openAddBookModal()">Add New Book</button>
        <table id="booksTable" class="display" style="width:100%">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Cover</th> 
                    <th>Title</th>
                    <th>Author</th>
                    <th>ISBN</th>
                    <th>Category</th>
                    <th>Status</th>
                </tr>
            </thead>
        </table>
    `);

    loadBooksTable();
}

function loadBooksTable() {
    const token = localStorage.getItem("accessToken");

    $('#booksTable').DataTable({
        destroy: true,
        ajax: {
            url: "/api/books",
            type: "GET",
            beforeSend: function (xhr) {
                xhr.setRequestHeader("Authorization", "Bearer " + token);
            },
            dataSrc: ""
        },
        columns: [
            { data: "id" },
            {
                data: "coverImageUrl",
                render: function(data = '/images/default-book.png') {
                    return `<img src="${data}" alt="Cover" style="width:50px;height:70px;object-fit:cover;">`;
                },
                orderable: false,
                searchable: false
            },
            { data: "title" },
            { data: "author" },
            { data: "isbn" },
            { data: "category" },
            {
                data: "status",
                render: function(data) {
                    return data === 'AVAILABLE' ? `<span class="badge bg-success">Available</span>` : `<span class="badge bg-warning text-dark">Borrowed</span>`;
                }
            }
        ]
    });
}

function openAddBookModal() {
    $("#addBookForm")[0].reset();
    $("#addBookAlert").addClass("d-none").text("");

    const token = localStorage.getItem("accessToken");

    $.ajax({
        url: "/api/books/categories",
        type: "GET",
        beforeSend: function(xhr) {
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        },
        success: function(data) {
            const select = $("#bookCategory");
            select.empty(); // 清空
            select.append(`<option value="">Select Category</option>`);
            data.forEach(category => {
                select.append(`<option value="${category}">${category}</option>`);
            });
        },
        error: function() {
            showAlert("Failed to load categories", "danger");
        }
    });
    const modal = new bootstrap.Modal(document.getElementById('addBookModal'));
    modal.show();
}

function submitNewBook() {
    const title = $("#bookTitle").val().trim();
    const author = $("#bookAuthor").val().trim();
    const isbn = $("#bookIsbn").val().trim();
    const category = $("#bookCategory").val();
    const coverImageUrl = $("#bookCoverUrl").val().trim();

    if (!title || !author || !isbn || !category) {
        $("#addBookAlert").removeClass("d-none").text("Title, Author, ISBN, and Category are required.");
        return;
    }

    const token = localStorage.getItem("accessToken");

    $.ajax({
        url: "/api/books",
        type: "POST",
        contentType: "application/json",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        },
        data: JSON.stringify({ title, author, isbn,category, coverImageUrl }),
        success: function () {
            const modalEl = document.getElementById('addBookModal');
            bootstrap.Modal.getInstance(modalEl).hide();
            showAlert("Book created successfully!", "success");
            loadBooksTable();
        },
        error: function (xhr) {
            showAlert(xhr.responseJSON?.error || "Failed to add book.", "danger");
        }
    });
}