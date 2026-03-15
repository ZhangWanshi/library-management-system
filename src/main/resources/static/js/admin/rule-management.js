function showRuleManagement() {

    $("#content").html(`

        <h4>Borrowing Rules</h4>

        <p id="currentRuleInfo" class="text-muted mb-3"></p>

        <div class="card p-4" style="max-width:500px">

            <div id="configRulesAlert" class="alert alert-danger d-none"></div>

            <div class="mb-3">
                <label class="form-label">Max Books Allowed</label>
                <input id="maxBooks" type="number" class="form-control">
            </div>

            <div class="mb-3">
                <label class="form-label">Borrow Duration (Days)</label>
                <input id="borrowDuration" type="number" class="form-control">
            </div>

            <button class="btn btn-library mb-3" onclick="submitRules()">
                Save Rules
            </button>

            <div id="rulesSuccess" class="alert alert-success d-none"></div>

        </div>
    `);

    loadCurrentRules();
}


function loadCurrentRules() {

    const token = localStorage.getItem("accessToken");

    $.ajax({
        url: "/api/rules",
        type: "GET",
        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        },

        success: function (data) {

            $("#maxBooks").val(data.maxBooksAllowed);
            $("#borrowDuration").val(data.borrowDurationDays);

            $("#currentRuleInfo").text(
                "Current rule: Max Books Allowed : "
                + data.maxBooksAllowed +
                ", Borrow Duration : "
                + data.borrowDurationDays +
                " days."
            );
            $("#maxBooks").val("");
            $("#borrowDuration").val("");
        }
    });
}


function submitRules() {

    const maxBooksAllowed = $("#maxBooks").val();
    const borrowDurationDays = $("#borrowDuration").val();

    if (!maxBooksAllowed || !borrowDurationDays) {

        $("#configRulesAlert")
            .removeClass("d-none")
            .text("All fields are required.");

        return;
    }

    const token = localStorage.getItem("accessToken");

    $.ajax({

        url: "/api/rules",
        type: "PUT",
        contentType: "application/json",

        beforeSend: function (xhr) {
            xhr.setRequestHeader("Authorization", "Bearer " + token);
        },

        data: JSON.stringify({
            maxBooksAllowed,
            borrowDurationDays
        }),

        success: function () {

            $("#rulesSuccess")
                .removeClass("d-none")
                .text("Borrowing rules updated successfully!");

            loadCurrentRules();

            setTimeout(function () {

                $("#rulesSuccess").addClass("d-none");

            }, 3000);
        },

        error: function (xhr) {

            $("#configRulesAlert")
                .removeClass("d-none")
                .text(xhr.responseJSON?.error || "Failed to update rules.");
        }
    });
}