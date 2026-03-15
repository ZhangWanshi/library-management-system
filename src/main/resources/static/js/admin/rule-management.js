function showRuleManagement() {

    $("#content").html(`

        <h4>Borrowing Rules</h4>

        <p id="currentRuleInfo" class="text-muted mb-3"></p>

        <div class="card p-4" style="max-width:500px">

            <div class="mb-3">
                <label class="form-label">Max Books Allowed</label>
                <input id="maxBooks" type="number" class="form-control">
                <div id="maxBooksAlert" class="text-danger mt-1 d-none"></div>
            </div>

            <div class="mb-3">
                <label class="form-label">Borrow Duration (Days)</label>
                <input id="borrowDuration" type="number" class="form-control">
                <div id="borrowDurationAlert" class="text-danger mt-1 d-none"></div>
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

    $("#maxBooksAlert").addClass("d-none").text("");
    $("#borrowDurationAlert").addClass("d-none").text("");

    let hasError = false;

    if (maxBooksAllowed === "") {
        $("#maxBooksAlert")
            .removeClass("d-none")
            .text("Max Books Allowed is required.");
        hasError = true;
        setTimeout(() => $("#maxBooksAlert").addClass("d-none"), 3000);
    }

    if (borrowDurationDays === "") {
        $("#borrowDurationAlert")
            .removeClass("d-none")
            .text("Borrow Duration is required.");
        hasError = true;
        setTimeout(() => $("#borrowDurationAlert").addClass("d-none"), 3000);
    }

    if (hasError) return;

    if (Number.parseInt(maxBooksAllowed) < 0) {
        $("#maxBooksAlert")
            .removeClass("d-none")
            .text("Max Books Allowed must be >= 0.");
        setTimeout(() => $("#maxBooksAlert").addClass("d-none"), 3000);
        return;
    }

    if (Number.parseInt(borrowDurationDays) < 0) {
        $("#borrowDurationAlert")
            .removeClass("d-none")
            .text("Borrow Duration must be >= 0.");
        setTimeout(() => $("#borrowDurationAlert").addClass("d-none"), 3000);
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
            const errMsg = xhr.responseJSON?.error || "Failed to update rules.";
            $("#maxBooksAlert").removeClass("d-none").text(errMsg);
            setTimeout(() => $("#maxBooksAlert").addClass("d-none"), 3000);
        }
    });
}