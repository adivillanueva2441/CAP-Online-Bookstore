package com.example.online_bookstore.service.impl;

import com.example.online_bookstore.dto.response.OrderDtoResponse;
import com.example.online_bookstore.model.Cart;
import com.example.online_bookstore.model.CartItems;
import com.example.online_bookstore.model.Order;
import com.example.online_bookstore.model.OrderItems;
import com.example.online_bookstore.repository.CartItemsRepository;
import com.example.online_bookstore.repository.CartRepository;
import com.example.online_bookstore.repository.OrderItemsRepository;
import com.example.online_bookstore.repository.OrderRepository;
import com.example.online_bookstore.service.ICartService;
import com.example.online_bookstore.service.IOrderService;
import com.example.online_bookstore.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private CartItemsRepository cartItemsRepository;

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    //transactional ensures all database operations succeed or fail together
    @Transactional
    public OrderDtoResponse orderCheckout(Long userId){
        Cart cart = cartRepository.findByUser_UserId(userId);
        List<CartItems> cartItems = cartItemsRepository.findByCart(cart);

        if(cartItems.isEmpty()){
            throw new IllegalStateException("Cart is empty");
        }

        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDateTime.now());

        double totalPrice = 0;

        for (CartItems cartItem : cartItems) {
            OrderItems orderItem = new OrderItems();
            orderItem.setOrder(order);
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            order.getOrderItems().add(orderItem);


            orderItemsRepository.save(orderItem);
            totalPrice += cartItem.getBook().getPrice() *  cartItem.getQuantity();
        }

        order.setTotalPrice(totalPrice);
        orderRepository.save(order);

        cartItemsRepository.deleteAll(cartItems);

        return new OrderDtoResponse(order);
    }


    @Override
    public void createCart(Cart cart) {
        cartRepository.save(cart);
    }




}
