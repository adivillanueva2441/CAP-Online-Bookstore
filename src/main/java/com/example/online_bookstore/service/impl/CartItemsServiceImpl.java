package com.example.online_bookstore.service.impl;

import com.example.online_bookstore.dto.request.CartItemsDtoRequest;
import com.example.online_bookstore.dto.response.CartItemsDtoResponse;
import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.model.Cart;
import com.example.online_bookstore.model.CartItems;
import com.example.online_bookstore.repository.BookRepository;
import com.example.online_bookstore.repository.CartItemsRepository;
import com.example.online_bookstore.repository.CartRepository;
import com.example.online_bookstore.service.ICartItemsService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartItemsServiceImpl implements ICartItemsService {

    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemsRepository cartItemsRepository;
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<CartItemsDtoResponse> getCartItems(Long userId) {

        Cart cart = cartRepository.findByUser_UserId(userId);

        return getCartItemsDtoResponse(cart);
    }

    @Override
    public CartItems addBookToCart(Long userId, CartItemsDtoRequest cartItemsDtoRequest) {
        Cart cart = cartRepository.findByUser_UserId(userId);
        Book book = bookRepository.findById(cartItemsDtoRequest.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        //adds quantity to existing book if adding an existing book to the cart
        CartItems existingItem = cartItemsRepository.findByCartAndBook(cart, book);
        if (existingItem != null) {
            existingItem.setQuantity(existingItem.getQuantity() + cartItemsDtoRequest.getQuantity());
            return cartItemsRepository.save(existingItem);
        }

        //Saves if book does not yet exist in the cart
        CartItems newItem = createCartItems(cartItemsDtoRequest, cart, book);
        return cartItemsRepository.save(newItem);

    }

    @Override
    public void updateCartItems(Long userId, CartItemsDtoRequest cartItemsDtoRequest) {
        Cart cart = cartRepository.findByUser_UserId(userId);
        Book book = bookRepository.findById(cartItemsDtoRequest.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));
        CartItems cartItem = cartItemsRepository.findByCartAndBook(cart, book);

        cartItem.setQuantity(cartItemsDtoRequest.getQuantity());

        if (cartItemsDtoRequest.getQuantity() <= 0) {
            cartItemsRepository.delete(cartItem);
        } else {
            cartItem.setQuantity(cartItemsDtoRequest.getQuantity());
            cartItemsRepository.save(cartItem);
        }
    }

    @Override
    public void removeBookFromCart(Long userId, Long bookId) {
        Cart cart = cartRepository.findByUser_UserId(userId);
        CartItems cartItem = cartItemsRepository.findByCartAndBook_BookId(cart, bookId);

        if (cartItem != null) {
            cartItemsRepository.delete(cartItem);
        }
    }

    @NonNull
    private List<CartItemsDtoResponse> getCartItemsDtoResponse(Cart cart) {
        return cartItemsRepository.findByCart(cart)
                .stream()
                .map(cartItems -> {

                    CartItemsDtoResponse cartItemsDtoResponse = new CartItemsDtoResponse();

                    cartItemsDtoResponse.setBookId(cartItems.getBook().getBookId());
                    cartItemsDtoResponse.setTitle(cartItems.getBook().getTitle());
                    cartItemsDtoResponse.setPrice(cartItems.getBook().getPrice());
                    cartItemsDtoResponse.setQuantity(cartItems.getQuantity());

                    return cartItemsDtoResponse;

                }).toList();
    }

    @NonNull
    private CartItems createCartItems(CartItemsDtoRequest cartItemsDtoRequest, Cart cart, Book book) {
        CartItems newItem = new CartItems();
        newItem.setCart(cart);
        newItem.setBook(book);
        newItem.setQuantity(cartItemsDtoRequest.getQuantity());
        return newItem;
    }
}
