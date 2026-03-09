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
                    <div class="card mb-3">
                        <div class="card-header">
                            Order Date: ${new Date(order.orderDate).toLocaleString()} | Total: $${order.totalPrice.toFixed(2)}
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
                                            <td class="title-col">${item.title}</td>
                                            <td>$${item.price.toFixed(2)}</td>
                                            <td>${item.quantity}</td>
                                            <td>$${item.subTotal.toFixed(2)}</td>
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