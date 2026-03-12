document.addEventListener('DOMContentLoaded', () => {
    const pageSize = 10;

    const pagination = createPagination({
        containerId: 'categories-pagination',
        onPageChange: (page) => fetchCategories(page)
    });

    function renderCategories(categories) {
        const tbody = document.getElementById('categories-table-body');

        if (categories.length === 0) {
            tbody.innerHTML = `<tr><td colspan="3" class="text-center text-muted py-3">No categories found.</td></tr>`;
            return;
        }

        tbody.innerHTML = categories.map(category => `
            <tr>
                <td>${category.categoryName}</td>
                <td>
                    <a href="/admin/categories/update/${category.categoryId}" class="btn btn-sm btn-outline-primary me-1">Edit</a>
                    <button class="btn btn-sm btn-outline-danger delete-btn" data-id="${category.categoryId}">Delete</button>
                </td>
            </tr>
        `).join('');

        tbody.querySelectorAll('.delete-btn').forEach(btn => {
            btn.addEventListener('click', () => deleteCategory(btn.dataset.id));
        });
    }

    function fetchCategories(page) {
        fetch(`/api/admin/categories?page=${page}&size=${pageSize}`)
            .then(res => res.json())
            .then(data => {
                renderCategories(data.content);
                pagination.render(data.number, data.totalPages);
            })
            .catch(() => {
                document.getElementById('categories-table-body').innerHTML =
                    `<tr><td colspan="3" class="text-center text-danger py-3">Failed to load categories.</td></tr>`;
            });
    }

    function deleteCategory(categoryId) {
        if (!confirm('Are you sure you want to delete this category?')) return;

        fetch(`/api/admin/categories/delete/${categoryId}`, { method: 'DELETE' })
            .then(res => {
                if (res.ok) {
                    fetchCategories(0);
                } else if (res.status === 500) {
                    alert('Cannot delete category with existing books.');
                } else {
                    alert('Failed to delete category.');
                }
            })
            .catch(() => alert('An error occurred while deleting.'));
    }

    fetchCategories(0);
});