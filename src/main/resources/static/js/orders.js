document.addEventListener("DOMContentLoaded", loadOrders);

function loadOrders() {
    fetch("/api/orders")
        .then(res => res.json())
        .then(orders => {
            const container = document.getElementById("orders-container");
            container.innerHTML = "";

            if (orders.length === 0) {
                container.innerHTML = "<p>You have no orders yet.</p>";
                return;
            }

            orders.forEach(order => {
                const orderHtml = `
                    <div class="card mb-3 mt-4">
                        <div class="card-header card text-white bg-success fw-bold">
                            Order Date: ${new Date(order.orderDate).toLocaleString()} | Total Cost: $${order.totalPrice.toFixed(2)}
                        </div>
                        <div class="card-body p-0">
                            <table class="table mb-0">
                                <thead>
                                    <tr>
                                        <th>Book</th>
                                        <th>Price</th>
                                        <th>Quantity</th>
                                        <th>Subtotal</th>
                                    </tr>
                                </thead>
                                <tbody>
                                    ${order.orderItems.map(item => `
                                        <tr>
                                            <td class="text-truncate w-50" style="max-width: 50%;" title="${item.title}">${item.title}</td>
                                            <td class="w-15 text center">$${item.price.toFixed(2)}</td>
                                            <td class="w-15 text center">${item.quantity}</td>
                                            <td class="w-20 text center">$${item.subTotal.toFixed(2)}</td>
                                        </tr>
                                    `).join('')}
                                </tbody>
                            </table>
                        </div>
                    </div>
                `;
                container.innerHTML += orderHtml;
            });
        })
        .catch(err => {
            console.error(err);
            document.getElementById("orders-container").innerHTML = "<p>Failed to load orders.</p>";
        });
}