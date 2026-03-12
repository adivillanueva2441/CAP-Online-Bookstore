document.addEventListener('DOMContentLoaded', () => {
    const pageSize = 10;

    const pagination = createPagination({
        containerId: 'users-pagination',
        onPageChange: (page) => fetchPage(page)
    });

    function renderPage(users, page, totalPages) {
        const tbody = document.getElementById('users-table-body');

        if (users.length === 0) {
            tbody.innerHTML = `<tr><td colspan="5" class="text-center text-muted py-3">No users found.</td></tr>`;
            return;
        }

        tbody.innerHTML = users.map(user => `
            <tr>
                <td>${user.firstName}</td>
                <td>${user.lastName}</td>
                <td>${user.username}</td>
                <td>${user.role}</td>
            </tr>
        `).join('');

        pagination.render(page, totalPages);
    }

    function fetchPage(page) {
        fetch(`/api/admin/users?page=${page}&size=${pageSize}`)
            .then(res => res.json())
            .then(data => {
                renderPage(data.content, data.number, data.totalPages);
            })
            .catch(err => {
                document.getElementById('users-table-body').innerHTML =
                    `<tr><td colspan="5" class="text-center text-danger py-3">Failed to load users.</td></tr>`;
                console.error(err);
            });
    }

    fetchPage(0);
});