package com.example.online_bookstore.dto.response;

import jakarta.validation.constraints.NotEmpty;

public class CartItemsDtoResponse {


    @NotEmpty(message = "cartItemId cannot be empty.")
    private Long cartItemId;

    @NotEmpty(message = "bookId cannot be empty.")
    private Long bookId;

    @NotEmpty(message = "title cannot be empty.")
    private String title;

    @NotEmpty(message = "quantity cannot be empty.")
    private int quantity;

    @NotEmpty(message = "price cannot be empty")
    private double price;

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
