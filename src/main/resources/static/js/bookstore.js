
document.addEventListener("DOMContentLoaded", () => {
    // Retrieves book list
    fetch("/api/books")
        .then(res => res.json())
        .then(books => {
            const container = document.getElementById("book-list");

            books.forEach(book => {
                const div = document.createElement("div");
                div.classList.add("col-md-4");
                div.innerHTML = `
                    <div class="card shadow p-3 h-100">
                        <h5 class="card-title">${book.title}</h5>
                        <p class="card-text"><strong>Category:</strong> ${book.categoryName}</p>
                        <p class="card-text"><strong>Author:</strong> ${book.authorName}</p>
                        <p class="card-text">${book.description}</p>
                        <p class="card-text"><strong>Price:</strong> $${book.price}</p>
                    </div>
                `;
                container.appendChild(div);
            });
        })
        .catch(err => console.error("Error fetching books:", err));


});