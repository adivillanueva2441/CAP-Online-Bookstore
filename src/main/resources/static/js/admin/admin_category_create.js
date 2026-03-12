document.addEventListener('DOMContentLoaded', () => {

    document.getElementById('submit-btn').addEventListener('click', () => {
        const categoryName = document.getElementById('categoryName').value.trim();
        const errorMsg = document.getElementById('error-msg');
        const successMsg = document.getElementById('success-msg');

        if (!categoryName) {
            errorMsg.textContent = 'Please enter a category name.';
            errorMsg.classList.remove('d-none');
            successMsg.classList.add('d-none');
            return;
        }

        errorMsg.classList.add('d-none');

        fetch('/api/admin/categories/add', {
            method: 'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ categoryName })
        })
        .then(res => {
            if (res.ok) {
                successMsg.textContent = 'Category added successfully!';
                successMsg.classList.remove('d-none');
                setTimeout(() => window.location.href = '/admin/categories', 1000);
            } else if (res.status === 500) {
                errorMsg.textContent = 'A category with this name already exists.';
                errorMsg.classList.remove('d-none');
            } else {
                errorMsg.textContent = 'Failed to add category.';
                errorMsg.classList.remove('d-none');
            }
        })
        .catch(() => {
            errorMsg.textContent = 'An error occurred. Please try again.';
            errorMsg.classList.remove('d-none');
        });
    });

});