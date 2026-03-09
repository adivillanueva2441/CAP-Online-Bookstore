package com.example.online_bookstore.service.impl;

import com.example.online_bookstore.dto.response.OrderDtoResponse;
import com.example.online_bookstore.model.*;
import com.example.online_bookstore.repository.CartItemsRepository;
import com.example.online_bookstore.repository.CartRepository;
import com.example.online_bookstore.repository.OrderItemsRepository;
import com.example.online_bookstore.repository.OrderRepository;
import com.example.online_bookstore.service.IOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderServiceImpl implements IOrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public List<OrderDtoResponse> getOrdersByUser(User user){
        return orderRepository.findByUserOrderByOrderDateDesc(user)
                .stream()
                .map(OrderDtoResponse::new)
                .collect(Collectors.toList());
    }




















}
