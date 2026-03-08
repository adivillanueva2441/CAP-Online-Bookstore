package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.request.CartItemsDtoRequest;
import com.example.online_bookstore.dto.response.CartItemsDtoResponse;
import com.example.online_bookstore.model.CartItems;

import java.util.List;

public interface ICartItemsService {
    List<CartItemsDtoResponse> getCartItems(Long userId);
    CartItems addBookToCart(Long userId, CartItemsDtoRequest cartItemsDtoRequest);
    void updateCartItems(Long userId, CartItemsDtoRequest cartItemsDtoRequest);
    void removeBookFromCart(Long userId, Long bookId);
}
