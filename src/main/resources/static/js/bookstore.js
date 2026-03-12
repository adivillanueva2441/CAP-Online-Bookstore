// books.js — uses the shared pagination.js module

document.addEventListener("DOMContentLoaded", () => {
    const container = document.getElementById("book-list");
    const searchInput = document.getElementById("search-title");
    const searchBtn = document.getElementById("search-btn");
    const categorySelect = document.getElementById("category-filter");

    let debounceTimeout;
    let currentPage = 0;
    const pageSize = 12;

    let currentMode = "all";
    let currentTitle = "";
    let currentCategoryId = null;

    const pagination = createPagination({
        containerId: "pagination",
        onPageChange: (page) => fetchPage(page),
    });

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
                    <img src="${book.coverImageUrl || 'https://img.freepik.com/free-vector/blue-text-book-library-icon_24877-83092.jpg'}"
                         class="card-img-top" alt="${book.title}"
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

    function buildUrl(page) {
        if (currentMode === "search") {
            return `/api/books/search?title=${encodeURIComponent(currentTitle)}&page=${page}&size=${pageSize}`;
        } else if (currentMode === "category") {
            return `/api/books/category/${currentCategoryId}?page=${page}&size=${pageSize}`;
        } else {
            return `/api/books?page=${page}&size=${pageSize}`;
        }
    }

    function fetchPage(page) {
        currentPage = page;
        fetch(buildUrl(page))
            .then(res => res.json())
            .then(data => {
                renderBooks(data.content);
                pagination.render(data.number, data.totalPages);
            })
            .catch(err => console.error("Error fetching books:", err));
    }

    fetchPage(0);

    searchInput.addEventListener("input", () => {
        clearTimeout(debounceTimeout);
        debounceTimeout = setTimeout(() => {
            const title = searchInput.value.trim();
            currentMode = title ? "search" : "all";
            currentTitle = title;
            fetchPage(0);
        }, 300);
    });

    searchBtn.addEventListener("click", () => {
        const title = searchInput.value.trim();
        if (!title) return;
        currentMode = "search";
        currentTitle = title;
        fetchPage(0);
    });

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

    categorySelect.addEventListener("change", () => {
        const categoryId = categorySelect.value;
        currentMode = categoryId ? "category" : "all";
        currentCategoryId = categoryId || null;
        fetchPage(0);
    });
});