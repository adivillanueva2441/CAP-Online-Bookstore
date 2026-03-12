document.addEventListener('DOMContentLoaded', () => {
    const pageSize = 10;

    const pagination = createPagination({
        containerId: 'books-pagination',
        onPageChange: (page) => fetchBooks(page)
    });

    function renderBooks(books) {
        const tbody = document.getElementById('books-table-body');

        if (books.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" class="text-center text-muted py-3">No books found.</td></tr>`;
            return;
        }

        tbody.innerHTML = books.map(book => `
            <tr>
                <td class="px-4">${book.title}</td>
                <td>${book.authorName}</td>
                <td>${book.categoryName}</td>
                <td>$${book.price.toFixed(2)}</td>
                <td>
                    <a href="/admin/books/update/${book.bookId}" class="btn btn-sm btn-outline-primary me-1">Edit</a>
                    <button class="btn btn-sm btn-outline-danger delete-btn" data-id="${book.bookId}">Delete</button>
                </td>
            </tr>
        `).join('');

        // attach delete listeners
        tbody.querySelectorAll('.delete-btn').forEach(btn => {
            btn.addEventListener('click', () => deleteBook(btn.dataset.id));
        });
    }

    function fetchBooks(page) {
        fetch(`/api/admin/books?page=${page}&size=${pageSize}`)
            .then(res => res.json())
            .then(data => {
                renderBooks(data.content);
                pagination.render(data.number, data.totalPages);
            })
            .catch(() => {
                document.getElementById('books-table-body').innerHTML =
                    `<tr><td colspan="5" class="text-center text-danger py-3">Failed to load books.</td></tr>`;
            });
    }

    function deleteBook(bookId) {
        if (!confirm('Are you sure you want to delete this book?')) return;

        fetch(`/api/admin/books/delete/${bookId}`, { method: 'DELETE' })
            .then(res => {
                if (res.ok) {
                    fetchBooks(0); // refresh list after delete
                } else {
                    alert('Failed to delete book.');
                }
            })
            .catch(() => alert('An error occurred while deleting.'));
    }

    fetchBooks(0);
});