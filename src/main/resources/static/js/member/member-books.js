// US5, 6, 7 - Member Views Books (Card Grid), Filters, and Borrows

let allBooks = [];

function showMemberBooks() {
    $("#content").html(`
        <div id="memberMessage" class="alert alert-success d-none"></div>
        <div class="d-flex justify-content-between align-items-center mb-4">
            <h4>Library Books</h4>
            <div>
                <select id="categoryFilter" class="form-select w-auto" onchange="filterBooksByCategory()">
                    <option value="ALL">All Categories</option>
                </select>
            </div>
        </div>
        <p class="text-muted">Browse available books and click "More Info" to view details or borrow.</p>
        
        <div id="booksGrid" class="row g-4">
            </div>
    `);

    loadBooksData();
}

function loadBooksData() {
    const token = localStorage.getItem("accessToken");

    $.ajax({
        url: "/api/books",
        type: "GET",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        },
        success: function (books) {
            allBooks = books;
            populateCategoryDropdown(books);
            renderBookCards(books);
        },
        error: function () {
            $("#booksGrid").html(`<div class="col-12 text-danger">Failed to load books.</div>`);
        }
    });
}

function populateCategoryDropdown(books) {
    const categories = [...new Set(books.map(b => b.category))];
    const dropdown = $("#categoryFilter");

    dropdown.find('option').not(':first').remove();

    categories.forEach(category => {
        if(category) {
            dropdown.append(`<option value="${category}">${category}</option>`);
        }
    });
}


function filterBooksByCategory() {
    const selectedCategory = $("#categoryFilter").val();
    if (selectedCategory === "ALL") {
        renderBookCards(allBooks);
    } else {
        const filtered = allBooks.filter(b => b.category === selectedCategory);
        renderBookCards(filtered);
    }
}


function renderBookCards(books) {
    const grid = $("#booksGrid");
    grid.empty();

    if (books.length === 0) {
        grid.html(`<div class="col-12"><p>No books found for this category.</p></div>`);
        return;
    }

    books.forEach(book => {
        const imgUrl = book.coverImageUrl ? book.coverImageUrl : '/images/default-book.png';
        const statusBadge = book.status === 'AVAILABLE'
            ? `<span class="badge bg-success position-absolute top-0 end-0 m-2">Available</span>`
            : `<span class="badge bg-secondary position-absolute top-0 end-0 m-2">Borrowed</span>`;

        const cardHtml = `
            <div class="col-12 col-sm-6 col-md-4 col-lg-3">
                <div class="card h-100 shadow-sm position-relative">
                    ${statusBadge}
                    <img src="${imgUrl}" class="card-img-top p-3 rounded" alt="Cover" style="height: 250px; object-fit: contain; background-color: #f8f9fa;">
                    <div class="card-body d-flex flex-column">
                        <h6 class="card-title text-truncate" title="${book.title}">${book.title}</h6>
                        <p class="card-text text-muted small mb-3 text-truncate" title="${book.author}">${book.author}</p>
                        <button class="btn btn-outline-dark mt-auto w-100" onclick="openBookModal(${book.id})">More Info</button>
                    </div>
                </div>
            </div>
        `;
        grid.append(cardHtml);
    });
}


function openBookModal(bookId) {

    const book = allBooks.find(b => b.id === bookId);
    if (!book) return;

    const imgUrl = book.coverImageUrl ? book.coverImageUrl : '/images/default-book.png';
    $("#detailCoverImage").attr("src", imgUrl);
    $("#detailTitle").text(book.title);
    $("#detailAuthor").text(book.author);
    $("#detailIsbn").text(book.isbn);
    $("#detailCategory").text(book.category);

    const statusHtml = book.status === 'AVAILABLE'
        ? `<span class="badge bg-success">Available</span>`
        : `<span class="badge bg-warning text-dark">Borrowed</span>`;
    $("#detailStatus").html(statusHtml);

    const actionArea = $("#detailActionArea");
    actionArea.empty();

    if (book.status === 'AVAILABLE') {
        actionArea.html(`<button class="btn btn-library w-100" onclick="borrowBook(${book.id})">Borrow This Book</button>`);
    }
    const modal = new bootstrap.Modal(document.getElementById('bookDetailModal'));
    modal.show();
}

// US6 - Member Borrows a Book
function borrowBook(bookId) {
    const token = localStorage.getItem("accessToken");

    $.ajax({
        url: `/api/borrowing/${bookId}`,
        type: "POST",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        },
        success: function () {
            $("#memberMessage")
                .removeClass("d-none")
                .text("Book borrowed successfully!");
            bootstrap.Modal.getInstance(document.getElementById('bookDetailModal')).hide();
            const currentCategory = $("#categoryFilter").val();
            loadBooksDataAndPreserveFilter(currentCategory);
        },
        error: function (xhr) {
            alert(xhr.responseJSON?.error || "Failed to borrow book. You may have reached your limit.");
        }
    });
}

function loadBooksDataAndPreserveFilter(currentCategory) {
    const token = localStorage.getItem("accessToken");
    $.ajax({
        url: "/api/books",
        type: "GET",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        },
        success: function (books) {
            allBooks = books;
            populateCategoryDropdown(books);
            $("#categoryFilter").val(currentCategory);
            filterBooksByCategory();
        }
    });
}