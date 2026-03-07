package com.example.online_bookstore.controller.rest;

import com.example.online_bookstore.dto.CartItemsDto;
import com.example.online_bookstore.model.CartItems;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/cart")
public class CartItemsController {

    @PostMapping("/add")
    public CartItems addBookToCart(@RequestBody CartItemsDto cartItemsDto) {}
}
