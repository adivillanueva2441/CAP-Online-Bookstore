document.addEventListener("DOMContentLoaded", () => {
    // Fetch all books from backend API
    fetch("/api/books")
        .then(res => res.json())
        .then(books => {
            const container = document.getElementById("book-list");

            books.forEach(book => {
                const div = document.createElement("div");
                div.classList.add("col-md-4");

                // Book card HTML
                div.innerHTML = `
                    <div class="card shadow p-3 h-100 d-flex flex-column">
                        <h5 class="card-title">${book.title}</h5>
                        <p class="card-text"><strong>Category:</strong> ${book.categoryName}</p>
                        <p class="card-text"><strong>Author:</strong> ${book.authorName}</p>
                        <p class="card-text">${book.description}</p>
                        <p class="card-text"><strong>Price:</strong> $${book.price.toFixed(2)}</p>
                        <div class="mt-auto">
                            <button class="btn btn-primary w-100 view-btn">View Book Details</button>
                        </div>
                    </div>
                `;

                //redirect to dynamic book details page
                div.querySelector(".view-btn").addEventListener("click", () => {
                    // Navigate to /books/{bookId}
                    window.location.href = `/books/${book.bookId}`;
                });

                container.appendChild(div);
            });
        })
        .catch(err => console.error("Error fetching books:", err));
});