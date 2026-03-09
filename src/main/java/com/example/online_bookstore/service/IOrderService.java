package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.response.OrderDtoResponse;
import com.example.online_bookstore.model.User;

import java.util.List;

public interface IOrderService {
    List<OrderDtoResponse> getOrdersByUser(User user);
}
