package com.example.online_bookstore.repository;

import com.example.online_bookstore.model.Cart;
import com.example.online_bookstore.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {

    Cart findByUser(User user);

    Cart findByUser_UserId(Long userId);
}
