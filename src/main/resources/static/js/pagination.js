function createPagination({ containerId, onPageChange, maxVisible = 10 }) {
    const container = document.getElementById(containerId);

    if (!container) {
        console.error(`Pagination: No element found with id "${containerId}"`);
        return null;
    }

    function render(currentPageNum, totalPages) {
        container.innerHTML = "";
        if (totalPages <= 1) return;

        let startPage = Math.max(0, currentPageNum - Math.floor(maxVisible / 2));
        let endPage = Math.min(totalPages - 1, startPage + maxVisible - 1);

        if (endPage - startPage < maxVisible - 1) {
            startPage = Math.max(0, endPage - maxVisible + 1);
        }

        // Helper: create a page item
        function createPageItem({ label, page, disabled = false, active = false }) {
            const li = document.createElement("li");
            li.classList.add("page-item");
            if (disabled) li.classList.add("disabled");
            if (active) li.classList.add("active");

            const a = document.createElement("a");
            a.classList.add("page-link");
            a.href = "#";
            a.innerHTML = label;

            if (!disabled && page !== null) {
                a.addEventListener("click", (e) => {
                    e.preventDefault();
                    onPageChange(page);
                });
            } else {
                a.addEventListener("click", (e) => e.preventDefault());
            }

            li.appendChild(a);
            return li;
        }

        // Previous
        container.appendChild(createPageItem({
            label: "&laquo;",
            page: currentPageNum - 1,
            disabled: currentPageNum === 0,
        }));

        // First page + ellipsis
        if (startPage > 0) {
            container.appendChild(createPageItem({ label: "1", page: 0 }));
            if (startPage > 1) {
                container.appendChild(createPageItem({ label: "...", page: null, disabled: true }));
            }
        }

        // Page window
        for (let i = startPage; i <= endPage; i++) {
            container.appendChild(createPageItem({
                label: i + 1,
                page: i,
                active: i === currentPageNum,
            }));
        }

        // Ellipsis + last page
        if (endPage < totalPages - 1) {
            if (endPage < totalPages - 2) {
                container.appendChild(createPageItem({ label: "...", page: null, disabled: true }));
            }
            container.appendChild(createPageItem({ label: totalPages, page: totalPages - 1 }));
        }

        // Next
        container.appendChild(createPageItem({
            label: "&raquo;",
            page: currentPageNum + 1,
            disabled: currentPageNum === totalPages - 1,
        }));
    }

    return { render };
}