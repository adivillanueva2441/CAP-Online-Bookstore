package com.example.online_bookstore.repository;

import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.model.Cart;
import com.example.online_bookstore.model.CartItems;
import com.example.online_bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemsRepository extends JpaRepository<CartItems, Long> {

    // Check book if it exists in the cart
    CartItems findByCartAndBook(Cart cart, Book book);

    CartItems findByCartAndBook_BookId(Cart cart, Long bookId);
    //Retrieve books in cart
    List<CartItems> findByCart(Cart cart);
}
