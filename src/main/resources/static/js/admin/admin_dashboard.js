document.addEventListener('DOMContentLoaded', () => {

    function loadRecentUsers() {
        fetch('/api/admin/users?page=0&size=5')
            .then(res => res.json())
            .then(data => {
                document.getElementById('stat-users').textContent = data.totalElements;
                document.getElementById('admin-recent-users-body').innerHTML =
                    data.content.map(user => `
                        <tr>
                            <td>${user.username}</td>
                            <td>${user.role}</td>
                        </tr>
                    `).join('');
            })
            .catch(() => {
                document.getElementById('admin-recent-users-body').innerHTML =
                    `<tr><td colspan="3" class="text-center text-danger py-3">Failed to load users.</td></tr>`;
            });
    }
    function loadRecentBooks() {
        fetch('/api/admin/books?page=0&size=5')
            .then(res => res.json())
            .then(data => {
                document.getElementById('stat-books').textContent = data.totalElements;
                document.getElementById('admin-book-list-body').innerHTML =
                    data.content.map(book => `
                        <tr>
                            <td>${book.title}</td>
                            <td>${book.categoryName}</td>
                            <td>${book.price}</td>
                        </tr>
                    `).join('');
            })
            .catch(() => {
                document.getElementById('admin-book-list-body').innerHTML =
                    `<tr><td colspan="3" class="text-center text-danger py-3">Failed to load books.</td></tr>`;
            });
    }
    function loadCategories() {
        fetch('/api/admin/categories?page=0&size=5')
            .then(res => res.json())
            .then(data => {
                document.getElementById('stat-categories').textContent = data.totalElements;
                document.getElementById('admin-category-list-body').innerHTML =
                    data.content.map(category => `
                        <tr>
                            <td>${category.categoryName}</td>
                        </tr>
                    `).join('');
            })
            .catch(() => {
                document.getElementById('admin-category-list-body').innerHTML =
                    `<tr><td colspan="3" class="text-center text-danger py-3">Failed to load categories.</td></tr>`;
            });
    }
/*    function loadRecentOrders() {
        fetch('/api/admin/orders?page=0&size=5')
            .then(res => res.json())
            .then(data => {
                document.getElementById('stat-orders').textContent = data.totalElements;
                document.getElementById('admin-recent-orders-body').innerHTML =
                    data.content.map(order => `
                        <tr>
                            <td>${order.username}</td>
                            <td>${user.role}</td>
                        </tr>
                    `).join('');
            })
            .catch(() => {
                document.getElementById('admin-recent-orders-body').innerHTML =
                    `<tr><td colspan="3" class="text-center text-danger py-3">Failed to load orders.</td></tr>`;
            });
    }
*/
    loadRecentUsers();
    loadRecentBooks();
    console.log('about to load categories');
    loadCategories();
    console.log('loadCategories called');
//    loadRecentOrders();


});