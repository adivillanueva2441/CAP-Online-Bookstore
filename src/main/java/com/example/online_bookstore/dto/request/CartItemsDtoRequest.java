package com.example.online_bookstore.dto.request;

import jakarta.validation.constraints.NotEmpty;

public class CartItemsDtoRequest {

    @NotEmpty(message = "bookId cannot be empty.")
    private Long bookId;

    @NotEmpty(message = "quantity cannot be empty.")
    private int quantity;

    public CartItemsDtoRequest(Long bookId, int quantity) {
        this.bookId = bookId;
        this.quantity = quantity;
    }

    public Long getBookId() {return bookId;}

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }


}
