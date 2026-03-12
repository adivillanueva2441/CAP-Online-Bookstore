package com.example.online_bookstore.dto.response;

import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.model.CartItems;
import com.example.online_bookstore.model.OrderItems;

import java.util.List;

public class OrderItemsDtoResponse {

    private String title;
    private int quantity;
    private double price;
    private double subTotal;


    public OrderItemsDtoResponse(OrderItems orderItems) {
        this.title = orderItems.getBook().getTitle();
        this.quantity = orderItems.getQuantity();
        this.price = orderItems.getPrice();
        this.subTotal = orderItems.getPrice() * quantity;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(double subTotal) {
        this.subTotal = subTotal;
    }
}
