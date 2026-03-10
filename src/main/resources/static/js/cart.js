document.addEventListener("DOMContentLoaded", loadCart);

function loadCart() {

    fetch("/api/cart")
        .then(res => res.json())
        .then(books => {

            const table = document.getElementById("cart-table-body");
            const totalDisplay = document.getElementById("cart-total");

            table.innerHTML = "";

            let cartTotal = 0;

            books.forEach(book => {

                const bookTotalPrice = book.price * book.quantity;
                cartTotal += bookTotalPrice;

                const row = document.createElement("tr");

                row.innerHTML = `
                    <td class="title-col">${book.title}</td>
                    <td>$${book.price}</td>

                    <td>
                        <input type="number" min="1" value="${book.quantity}"
                               class="form-control quantity-input"
                               data-bookid="${book.bookId}" onInput="this.value = Math.abs(this.value)">
                    </td>

                    <td>$${bookTotalPrice.toFixed(2)}</td>

                    <td>
                        <button class="btn btn-danger remove-btn"
                                data-bookid="${book.bookId}">
                                Remove from cart
                        </button>
                    </td>
                `;

                table.appendChild(row);
            });

            totalDisplay.innerText = "Cart Total: $" + cartTotal.toFixed(2);

            setupQuantityUpdate();
            setupRemoveButtons();

            checkoutBtn.addEventListener("click", (e) => {
                if (books.length === 0) {
                    e.preventDefault(); // stop redirect if cart is empty
                    alert("Your cart is empty. Please add items before checking out.");
                }
            });
        });
}

function setupQuantityUpdate() {

    document.querySelectorAll(".quantity-input").forEach(input => {

        input.addEventListener("change", () => {

            const bookId = input.dataset.bookid;
            const quantity = parseInt(input.value);

            fetch("/api/cart/update", {

                method: "PUT",

                headers: {
                    "Content-Type": "application/json"
                },

                body: JSON.stringify({
                    bookId: bookId,
                    quantity: quantity
                })

            }).then(() => loadCart());

        });

    });
}


function setupRemoveButtons() {
    document.querySelectorAll(".remove-btn").forEach(button => {

        button.addEventListener("click", () => {

            const bookId = button.dataset.bookid;

            fetch(`/api/cart/remove/${bookId}`, {
                method: "DELETE",
            })
            .then(() => loadCart());
        });
    });
}
