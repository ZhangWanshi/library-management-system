function loadAdminDashboard() {

    $("#content").html(`

        <h4 class="mb-4">Analytics Dashboard</h4>

        <div class="row mb-4">

            <div class="col-md-4">
                <div class="card text-center shadow-sm">
                    <div class="card-body">
                        <h5>Total Books</h5>
                        <h3 id="totalBooks"></h3>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card text-center shadow-sm">
                    <div class="card-body">
                        <h5>Total Borrow Records</h5>
                        <h3 id="totalBorrows"></h3>
                    </div>
                </div>
            </div>

            <div class="col-md-4">
                <div class="card text-center shadow-sm">
                    <div class="card-body">
                        <h5>Total Members</h5>
                        <h3 id="totalMembers"></h3>
                    </div>
                </div>
            </div>

        </div>

        <div class="row mb-5">

            <div class="col-md-6">

                <h5>Books by Category</h5>

                <div class="chart-container">
                    <canvas id="categoryChart"></canvas>
                </div>

            </div>

            <div class="col-md-6">

                <h5>Most Borrowed Books</h5>

                <div class="chart-container">
                    <canvas id="borrowChart"></canvas>
                </div>

            </div>

        </div>
    `);

    loadSummary();
    loadCategoryChart();
    loadBorrowChart();
}


function loadSummary() {

    const token = localStorage.getItem("accessToken");

    $.ajax({

        url: "/api/books/summary",

        beforeSend: function (xhr) {
            xhr.setRequestHeader(
                "Authorization",
                "Bearer " + token
            );
        },

        success: function (data) {

            $("#totalBooks").text(data.totalBooks);
            $("#totalBorrows").text(data.totalBorrowRecords);
            $("#totalMembers").text(data.totalMembers);
        }
    });
}


function loadCategoryChart() {

    const token = localStorage.getItem("accessToken");

    $.ajax({

        url: "/api/books/by-category",

        beforeSend: function (xhr) {
            xhr.setRequestHeader(
                "Authorization",
                "Bearer " + token
            );
        },

        success: function (data) {

            const labels = data.map(c => c.category);
            const values = data.map(c => c.bookCount);

            new Chart(
                document.getElementById("categoryChart"),
                {
                    type: "doughnut",

                    data: {

                        labels: labels,

                        datasets: [{

                            data: values,

                            backgroundColor: [
                                "#9ab5ad",
                                "#d6c7a1",
                                "#bfa77a",
                                "#a58d5e",
                                "#6f8f87",
                                "#c9b37c"
                            ]
                        }]
                    },

                    options: {

                        maintainAspectRatio: false,

                        plugins: {

                            legend: {
                                position: "bottom"
                            },

                            tooltip: {

                                callbacks: {

                                    label: function(context) {

                                        const label = context.label;
                                        const value = context.raw;

                                        return label + ": " + value;
                                    }
                                }
                            }
                        }
                    }
                }
            );
        }
    });
}


function loadBorrowChart() {

    const token = localStorage.getItem("accessToken");

    $.ajax({

        url: "/api/borrowing/most-borrowed",

        beforeSend: function (xhr) {

            xhr.setRequestHeader(
                "Authorization",
                "Bearer " + token
            );
        },

        success: function (data) {

            const labels = data.map(
                b => b.bookTitle + " (" + b.category + ")"
            );

            const values = data.map(
                b => b.borrowCount
            );

            new Chart(
                document.getElementById("borrowChart"),
                {
                    type: "bar",

                    data: {

                        labels: labels,

                        datasets: [{

                            data: values,

                            backgroundColor: "#9ab5ad"
                        }]
                    },

                    options: {

                        maintainAspectRatio: false,

                        indexAxis: 'y',

                        plugins: {

                            legend: {
                                display: false
                            },

                            tooltip: {

                                callbacks: {

                                    label: function(context) {

                                        return "Borrowed: " + context.raw;
                                    }
                                }
                            }
                        }
                    }
                }
            );
        }
    });
}