package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.CartItemsDto;
import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.model.Cart;
import com.example.online_bookstore.model.CartItems;
import com.example.online_bookstore.model.User;
import com.example.online_bookstore.repository.BookRepository;
import com.example.online_bookstore.repository.CartItemsRepository;
import com.example.online_bookstore.repository.CartRepository;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

@Service
public class CartItemsService {


    private final CartRepository cartRepository;
    private final CartItemsRepository cartItemsRepository;
    private final BookRepository bookRepository;

    public CartItemsService(CartRepository cartRepository,
                            CartItemsRepository cartItemsRepository,
                            BookRepository bookRepository) {
        this.cartRepository = cartRepository;
        this.cartItemsRepository = cartItemsRepository;
        this.bookRepository = bookRepository;

    }

    public CartItems addBookToCart(User user, CartItemsDto cartItemsDto) {
        Cart cart = cartRepository.findByUser(user);
        Book book = bookRepository.findById(cartItemsDto.getBookId())
                .orElseThrow(() -> new RuntimeException("Book not found"));

        CartItems item = getCartItems(cartItemsDto, cart, book);

        return cartItemsRepository.save(item);

    }

    @NonNull
    private CartItems getCartItems(CartItemsDto cartItemsDto, Cart cart, Book book) {
        CartItems item = new CartItems();
        item.setCart(cart);
        item.setBook(book);
        item.setQuantity(cartItemsDto.getQuantity());
        return item;
    }
}
