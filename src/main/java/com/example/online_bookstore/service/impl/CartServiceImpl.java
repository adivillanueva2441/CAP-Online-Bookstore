package com.example.online_bookstore.service.impl;

import com.example.online_bookstore.model.Cart;
import com.example.online_bookstore.repository.CartRepository;
import com.example.online_bookstore.service.ICartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartRepository cartRepository;


    @Override
    public void createCart(Cart cart) {
        cartRepository.save(cart);
    }

}
