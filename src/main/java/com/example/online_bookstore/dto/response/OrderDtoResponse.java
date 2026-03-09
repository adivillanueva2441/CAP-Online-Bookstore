package com.example.online_bookstore.dto.response;

import com.example.online_bookstore.model.Order;

import java.time.LocalDateTime;
import java.util.List;

public class OrderDtoResponse {
    private LocalDateTime orderDate;
    private Double totalPrice;
    private List<OrderItemsDtoResponse> orderItems;


    public OrderDtoResponse(Order order) {
        this.orderDate = order.getOrderDate();
        this.totalPrice = order.getTotalPrice();
        this.orderItems = order.getOrderItems().stream().map(OrderItemsDtoResponse::new).toList();
    }


    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public Double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(Double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public List<OrderItemsDtoResponse> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<OrderItemsDtoResponse> orderItems) {
        this.orderItems = orderItems;
    }
}
