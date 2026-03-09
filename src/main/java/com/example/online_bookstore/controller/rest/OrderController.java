package com.example.online_bookstore.controller.rest;

import com.example.online_bookstore.dto.response.OrderDtoResponse;
import com.example.online_bookstore.model.User;
import com.example.online_bookstore.service.IOrderService;
import com.example.online_bookstore.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private IOrderService orderService;

    @Autowired
    private IUserService userService;

    @GetMapping
    public List<OrderDtoResponse> getOrders(Authentication authentication) {

        User user = userService.findByUsername(authentication.getName());

        return orderService.getOrdersByUser(user);
    }

}
