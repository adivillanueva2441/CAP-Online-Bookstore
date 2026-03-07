package com.example.online_bookstore.service;

import com.example.online_bookstore.model.Cart;
import com.example.online_bookstore.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartService {
    @Autowired
    private CartRepository cartRepository;


    public void createCart(Cart cart) {
        cartRepository.save(cart);
    }

}
