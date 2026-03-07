package com.example.online_bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

@Entity
@Table(name = "order_items",
        uniqueConstraints = @UniqueConstraint(columnNames = {"order_id", "book_id"}))
public class OrderItems {
    // Sets Primary/Foreign Keys and relationship with other tables

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderItemsId;

    //Columns
    @Column(nullable = false, name = "quantity")
    @Min(value = 1, message = "Ordered book should be at least 1 unit.")
    private int quantity;

    @Column(nullable = false, name = "total_price")
    @Min(value = 1, message = "Total price should be a positive number.")
    private int totalPrice;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;


    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getOrderItemsId() {
        return orderItemsId;
    }

    public void setOrderItemsId(Long orderItemsId) {
        this.orderItemsId = orderItemsId;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
