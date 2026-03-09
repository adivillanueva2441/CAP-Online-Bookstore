package com.example.online_bookstore.dto.response;

import com.example.online_bookstore.model.CartItems;
import jakarta.validation.constraints.NotEmpty;

public class CartItemsDtoResponse {

    private Long cartItemId;
    private Long bookId;
    private String title;
    private int quantity;
    private double price;

    public CartItemsDtoResponse(CartItems cartItems) {
        this.cartItemId = cartItems.getCartItemsId();
        this.bookId = cartItems.getBook().getBookId();
        this.title = cartItems.getBook().getTitle();
        this.quantity = cartItems.getQuantity();
        this.price = cartItems.getBook().getPrice();
    }

    public Long getCartItemId() {
        return cartItemId;
    }

    public void setCartItemId(Long cartItemId) {
        this.cartItemId = cartItemId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
