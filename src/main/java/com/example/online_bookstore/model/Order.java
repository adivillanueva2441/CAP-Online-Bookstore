package com.example.online_bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {
    // Sets Primary/Foreign Keys and relationship with other tables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderId;

    //Columns
    @Min(value = 1, message = "Total price must be a positive number (greater than 0).")
    @Column(nullable = false, name = "total_price")
    private double totalPrice;


    @Column(nullable = false, name = "order_date")
    private LocalDateTime orderDate;


    @OneToMany(mappedBy = "order")
    private List<OrderItems> orderItems = new ArrayList<>();

    @ManyToOne()
    @JoinColumn(name = "user_id")
    private User user;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public List<OrderItems> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItems> orderItems) {
        this.orderItems = orderItems;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }
}
