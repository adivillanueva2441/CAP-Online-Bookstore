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
                    <div class="card shadow-sm h-100 d-flex flex-column p-3">
                        <img src="${book.coverImageUrl || 'https://img.freepik.com/free-vector/blue-text-book-library-icon_24877-83092.jpg?semt=ais_rp_50_assets&w=740&q=80'}"
                             class="card-img-top mb-3" style="height:200px; object-fill;">

                        <div class="card-body d-flex flex-column">
                            <h5 class="card-title text-primary fw-bold text-truncate" title="${book.title}"
                                style="font-size:1.25rem;">${book.title}</h5>

                            <p class="card-text text-muted mb-1"><small>Category: ${book.categoryName}</small></p>
                            <p class="card-text text-muted mb-2"><small>Author: ${book.authorName}</small></p>

                            <p class="card-text text-truncate-3" style="flex-grow:1;">${book.description}</p>

                            <div class="d-flex justify-content-between align-items-center mt-3">
                                <span class="fw-bold text-primary fs-5">$${book.price.toFixed(2)}</span>
                                <div class="input-group" style="width: 300px;">
                                    <button class="btn btn-primary btn-sm back-btn">Back to Home</button>
                                    <input type="number" min="1" value="1" class="form-control" id="quantityInput"
                                           onInput="this.value = Math.abs(this.value)">
                                    <button class="btn btn-success" id="addToCartBtn">Add</button>
                                </div>
                            </div>

                            <div id="cartMessage" class="mt-2"></div>
                        </div>
                    </div>
                </div>
            `;

            container.querySelector(".back-btn").addEventListener("click", () => {
                window.location.href = `/`;
            });

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
                    if (!res.ok) throw new Error("Unauthorized Action: User not logged in");
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