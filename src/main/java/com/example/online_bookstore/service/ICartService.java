package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.response.OrderDtoResponse;
import com.example.online_bookstore.model.Cart;

public interface ICartService {

    void createCart(Cart cart);

    OrderDtoResponse orderCheckout(Long userId);

}
