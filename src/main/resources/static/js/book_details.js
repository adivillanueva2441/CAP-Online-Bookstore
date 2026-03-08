document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("book-details-container");
    const bookId = window.location.pathname.split("/").pop();

    fetch(`/api/books/${bookId}`)
        .then(res => {
            if (!res.ok) throw new Error("Book not found");
            return res.json();
        })
        .then(book => {
            container.innerHTML = `
                <div class="col-md-6">
                    <div class="card shadow p-4">
                        <h3>${book.title}</h3>
                        <p><strong>Category:</strong> ${book.categoryName}</p>
                        <p><strong>Author:</strong> ${book.authorName}</p>
                        <p>${book.description}</p>
                        <p><strong>Price:</strong> $${book.price.toFixed(2)}</p>
                        <div class="input-group mt-3">
                            <input type="number" min="1" value="1" class="form-control" id="quantityInput">
                            <button class="btn btn-success" id="addToCartBtn">Add to Cart</button>
                        </div>
                        <div id="cartMessage" class="mt-2"></div>
                    </div>
                </div>
            `;

            //Event listener for when 'add to cart' button is pressed
            document.getElementById("addToCartBtn").addEventListener("click", () => {
                const quantity = parseInt(document.getElementById("quantityInput").value);
                if (quantity < 1) {
                    document.getElementById("cartMessage").innerHTML = `<span class="text-danger">Quantity must be at least 1.</span>`;
                    return;
                }

                const payload = {
                    bookId: book.bookId,  // ensures payload matches DTO
                    quantity: quantity
                };

                //Call add to cart API
                fetch("/api/cart/add", {
                    method: "POST",
                    headers: {
                        "Content-Type": "application/json"
                    },
                    body: JSON.stringify(payload)
                })
                .then(res => {
                    if (!res.ok) throw new Error("Failed to add to cart");
                    return res.json();
                })
                .then(data => {
                    document.getElementById("cartMessage").innerHTML = `<span class="text-success">Added ${quantity} to cart!</span>`;
                })
                .catch(err => {
                    document.getElementById("cartMessage").innerHTML = `<span class="text-danger">${err.message}</span>`;
                });
            });
        })
        .catch(err => {
            container.innerHTML = `<div class="alert alert-danger">Error loading book details: ${err.message}</div>`;
        });
});