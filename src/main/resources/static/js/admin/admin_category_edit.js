document.addEventListener('DOMContentLoaded', () => {

    const categoryId = window.location.pathname.split('/').pop();
    const errorMsg = document.getElementById('error-msg');
    const successMsg = document.getElementById('success-msg');

    // Prefill form with existing category data
    fetch(`/api/admin/categories/${categoryId}`)
        .then(res => res.json())
        .then(category => {
            document.getElementById('categoryName').value = category.categoryName;
        })
        .catch(() => {
            errorMsg.textContent = 'Failed to load category data.';
            errorMsg.classList.remove('d-none');
        });

    // Submit
    document.getElementById('submit-btn').addEventListener('click', () => {
        const categoryName = document.getElementById('categoryName').value.trim();

        if (!categoryName) {
            errorMsg.textContent = 'Please enter a category name.';
            errorMsg.classList.remove('d-none');
            successMsg.classList.add('d-none');
            return;
        }

        errorMsg.classList.add('d-none');

        fetch(`/api/admin/categories/update/${categoryId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ categoryName })
        })
        .then(res => {
            if (res.ok) {
                successMsg.textContent = 'Category updated successfully!';
                successMsg.classList.remove('d-none');
                setTimeout(() => window.location.href = '/admin/categories', 1000);
            } else if (res.status === 500) {
                errorMsg.textContent = 'A category with this name already exists.';
                errorMsg.classList.remove('d-none');
            } else {
                errorMsg.textContent = 'Failed to update category.';
                errorMsg.classList.remove('d-none');
            }
        })
        .catch(() => {
            errorMsg.textContent = 'An error occurred. Please try again.';
            errorMsg.classList.remove('d-none');
        });
    });

});