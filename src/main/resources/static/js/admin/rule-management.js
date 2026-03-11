// US3 - Admin Configures Borrowing Rules
function openConfigRulesModal() {
    $("#configRulesForm")[0].reset();
    $("#configRulesAlert").addClass("d-none").text("");

    const modal = new bootstrap.Modal(document.getElementById('configRulesModal'));
    modal.show();
}

function submitRules() {
    const maxBooksAllowed = $("#maxBooks").val();
    const borrowDurationDays = $("#borrowDuration").val();

    if (!maxBooksAllowed || !borrowDurationDays) {
        $("#configRulesAlert").removeClass("d-none").text("All fields are required.");
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
        data: JSON.stringify({ maxBooksAllowed, borrowDurationDays }),
        success: function () {
            const modalEl = document.getElementById('configRulesModal');
            bootstrap.Modal.getInstance(modalEl).hide();
            alert("Borrowing rules updated successfully!");
        },
        error: function (xhr) {
            $("#configRulesAlert").removeClass("d-none").text(xhr.responseJSON?.error || "Failed to update rules.");
        }
    });
}