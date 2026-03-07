package com.example.online_bookstore.repository;

import com.example.online_bookstore.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItems extends JpaRepository<Order, Long> {
}
