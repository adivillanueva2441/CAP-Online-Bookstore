package com.example.online_bookstore.controller.rest;

import com.example.online_bookstore.dto.response.OrderDtoResponse;
import com.example.online_bookstore.model.User;
import com.example.online_bookstore.service.ICartService;
import com.example.online_bookstore.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cart")
public class CartController {
    @Autowired
    private ICartService cartService;

    @Autowired
    private IUserService userService;

    @PostMapping("/checkout")
    public OrderDtoResponse checkout(Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        return cartService.orderCheckout(user.getUserId());
    }
}
