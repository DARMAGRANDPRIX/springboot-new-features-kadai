document.addEventListener('DOMContentLoaded', function () {
    const deleteButtons = document.querySelectorAll('.delete-review-button');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function () {
            const reviewId = this.getAttribute('data-review-id');
            const houseId = this.getAttribute('data-house-id');
            const confirmDeleteButton = document.getElementById('confirmDeleteButton');

            // 既存のイベントリスナーを削除
            confirmDeleteButton.replaceWith(confirmDeleteButton.cloneNode(true));
            const newConfirmDeleteButton = document.getElementById('confirmDeleteButton');

            newConfirmDeleteButton.addEventListener('click', function () {
                deleteReview(reviewId, houseId);
            });

            var deleteReviewModal = new bootstrap.Modal(document.getElementById('deleteReviewModal'));
            deleteReviewModal.show();
        });
    });
});

function getCsrfToken() {
    return document.querySelector('meta[name="_csrf"]').getAttribute('content');
}

function getCsrfHeader() {
    return document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
}

function deleteReview(reviewId, houseId) {
    const csrfToken = getCsrfToken();
    const csrfHeader = getCsrfHeader();

    fetch(`/houses/${houseId}/reviews/${reviewId}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json', // 追加
            [csrfHeader]: csrfToken
        }
    })
    .then(response => {
        if (response.ok) {
            window.location.href = `/houses/${houseId}`;
        } else {
            return response.json().then(error => {
                alert(`削除に失敗しました: ${error.message}`);
            });
        }
    })
    .catch(error => console.error('Error:', error));
}