document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("book-list");
    const searchInput = document.getElementById("search-title");
    const searchBtn = document.getElementById("search-btn");
    const categorySelect = document.getElementById("category-filter"); // NEW

    let debounceTimeout;

    // Renders the books
    function renderBooks(books) {
        container.innerHTML = "";
        if (books.length === 0) {
            container.innerHTML = "<p class='text-center'>No books found.</p>";
            return;
        }

        books.forEach(book => {
            const div = document.createElement("div");
            div.classList.add("col-md-3");

            div.innerHTML = `
                <div class="card shadow-sm h-100 d-flex flex-column">
                    <img src="${book.coverImageUrl ? book.coverImageUrl : 'https://img.freepik.com/free-vector/blue-text-book-library-icon_24877-83092.jpg?semt=ais_rp_50_assets&w=740&q=80'}"
                         class="card-img-top"
                         alt="${book.title}"
                         style="height: 200px; object-fill: cover; border-radius: 0.5rem 0.5rem 0 0;">

                    <div class="card-body d-flex flex-column">
                        <h5 class="card-title fw-bold mb-2 text-truncate" style="border-bottom: 2px solid #6c63ff; padding-bottom: 0.3rem;">
                            ${book.title}
                        </h5>
                        <p class="card-text text-muted mb-1"><small>Category: ${book.categoryName}</small></p>
                        <p class="card-text text-muted mb-2"><small>Author: ${book.authorName}</small></p>
                        <p class="card-text text-truncate-3" style="flex-grow: 1;">${book.description}</p>

                        <div class="d-flex justify-content-between align-items-center mt-3">
                            <span class="fw-bold text-primary">$${book.price.toFixed(2)}</span>
                            <button class="btn btn-outline-primary btn-sm view-btn">View</button>
                        </div>
                    </div>
                </div>
            `;

            div.querySelector(".view-btn").addEventListener("click", () => {
                window.location.href = `/books/${book.bookId}`;
            });

            container.appendChild(div);
        });
    }

    // fetch books
    fetch("/api/books")
        .then(res => res.json())
        .then(renderBooks)
        .catch(err => console.error("Error fetching books:", err));

    //Search bar event listener
    searchInput.addEventListener("input", () => {
        const title = searchInput.value.trim();

        clearTimeout(debounceTimeout);
        debounceTimeout = setTimeout(() => {
            if (title === "") {
                fetch("/api/books")
                    .then(res => res.json())
                    .then(renderBooks)
                    .catch(err => console.error(err));
                return;
            }

            fetch(`/api/books/search?title=${encodeURIComponent(title)}`)
                .then(res => res.json())
                .then(renderBooks)
                .catch(err => console.error(err));
        }, 300);
    });

    //Search button event listener
    searchBtn.addEventListener("click", () => {
        const title = searchInput.value.trim();
        if (!title) return;

        fetch(`/api/books/search?title=${encodeURIComponent(title)}`)
            .then(res => res.json())
            .then(renderBooks)
            .catch(err => console.error(err));
    });

    //Populates Category selection from the database
    fetch("/api/category")
        .then(res => res.json())
        .then(categories => {
            categories.forEach(category => {
                const option = document.createElement("option");
                option.value = category.categoryId;
                option.textContent = category.categoryName;
                categorySelect.appendChild(option);
            });
        })
        .catch(err => console.error("Error fetching categories:", err));

    // Category filter event listener
    categorySelect.addEventListener("change", () => {
        const categoryId = categorySelect.value;

        if (!categoryId) {
            // All categories selected
            fetch("/api/books")
                .then(res => res.json())
                .then(renderBooks)
                .catch(err => console.error(err));
            return;
        }

        // Fetch books filtered by category
        fetch(`/api/books/category/${categoryId}`)
            .then(res => res.json())
            .then(renderBooks)
            .catch(err => console.error(err));
    });
});