document.addEventListener("DOMContentLoaded", () =>{
    loadCheckout();
    orderCheckout();
});

function loadCheckout() {

    fetch("/api/cart")
        .then(res => res.json())
        .then(cart => {

            const table = document.getElementById("checkout-items");
            table.innerHTML = "";

            let totalPrice = 0;

            cart.forEach(book => {

                const subtotal = book.price * book.quantity;
                totalPrice += subtotal;

                const row = `
                    <tr>
                        <td class="title-col">${book.title}</td>
                        <td>$${book.price.toFixed(2)}</td>
                        <td>${book.quantity}</td>
                        <td>$${subtotal.toFixed(2)}</td>
                    </tr>
                `;

                table.innerHTML += row;
            });

            document.getElementById("checkout-total").innerText = totalPrice.toFixed(2);
        });

}


function orderCheckout(){
    document.getElementById("checkout-btn").addEventListener("click", () => {
        fetch("/api/cart/checkout", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"
            },
        })
        .then(res => {
            if (res.ok) {
                return res.json();
            } else {
                throw new Error("Checkout failed");
            }
        })
        .then(order => {
            alert("Checkout successful! Total: $" + order.totalPrice.toFixed(2));
            window.location.href = "/orders";
        })
        .catch(err => {
            console.error(err);
            alert("Checkout failed. Please try again.");
        });
    });

}
