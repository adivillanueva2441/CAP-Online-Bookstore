document.addEventListener('DOMContentLoaded', () => {

    // Get book ID from URL e.g. /admin/books/edit/5
    const bookId = window.location.pathname.split('/').pop();

    const errorMsg = document.getElementById('error-msg');
    const successMsg = document.getElementById('success-msg');

    // Load authors and categories first, then prefill book data
    Promise.all([
        fetch('/api/admin/authors').then(res => res.json()),
        fetch('/api/category').then(res => res.json()),
        fetch(`/api/admin/books/${bookId}`).then(res => res.json())
    ])
    .then(([authors, categories, book]) => {

        // Populate authors dropdown
        const authorSelect = document.getElementById('author');
        authors.forEach(author => {
            const option = document.createElement('option');
            option.value = author.authorId;
            option.textContent = author.authorName;
            authorSelect.appendChild(option);
        });

        // Populate categories dropdown
        const categorySelect = document.getElementById('category');
        categories.forEach(category => {
            const option = document.createElement('option');
            option.value = category.categoryId;
            option.textContent = category.categoryName;
            categorySelect.appendChild(option);
        });

        // Prefill form with existing book data
        document.getElementById('title').value = book.title;
        document.getElementById('description').value = book.description;
        document.getElementById('price').value = book.price;

        // Select the correct author and category options
        // We match by name since response DTO only has authorName/categoryName
        [...authorSelect.options].forEach(opt => {
            if (opt.textContent === book.authorName) opt.selected = true;
        });
        [...categorySelect.options].forEach(opt => {
            if (opt.textContent === book.categoryName) opt.selected = true;
        });

    })
    .catch(() => {
        errorMsg.textContent = 'Failed to load book data.';
        errorMsg.classList.remove('d-none');
    });

    // Submit
    document.getElementById('submit-btn').addEventListener('click', () => {
        const title = document.getElementById('title').value.trim();
        const description = document.getElementById('description').value.trim();
        const price = parseFloat(document.getElementById('price').value);
        const authorId = document.getElementById('author').value;
        const categoryId = document.getElementById('category').value;

        if (!title || !description || isNaN(price) || !authorId || !categoryId) {
            errorMsg.textContent = 'Please fill in all fields.';
            errorMsg.classList.remove('d-none');
            successMsg.classList.add('d-none');
            return;
        }

        errorMsg.classList.add('d-none');

        fetch(`/api/admin/books/update/${bookId}`, {
            method: 'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                title,
                description,
                price,
                authorId: parseInt(authorId),
                categoryId: parseInt(categoryId)
            })
        })
        .then(res => {
            if (res.ok) {
                successMsg.textContent = 'Book updated successfully!';
                successMsg.classList.remove('d-none');
                setTimeout(() => window.location.href = '/admin/books', 1000);
            } else if (res.status === 500) {
                errorMsg.textContent = 'A book with this title already exists.';
                errorMsg.classList.remove('d-none');
            } else {
                errorMsg.textContent = 'Failed to update book.';
                errorMsg.classList.remove('d-none');
            }
        })
        .catch(() => {
            errorMsg.textContent = 'An error occurred. Please try again.';
            errorMsg.classList.remove('d-none');
        });
    });

});