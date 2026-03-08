package com.example.online_bookstore.service.impl;

import com.example.online_bookstore.model.Cart;
import com.example.online_bookstore.model.User;
import com.example.online_bookstore.repository.UserRepository;
import com.example.online_bookstore.service.ICartService;
import com.example.online_bookstore.service.IUserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserRegistrationServiceImpl implements IUserRegistrationService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ICartService cartService;

    @Override
    public void registerUser(User user) {
        String username = userRepository.findByUsername(user.getUsername()).toString();
        if(username.isEmpty()){
            throw new RuntimeException("Username is already in use");
        }
        //mask password
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        //save user
        user.setRole("USER");
        User savedUser = userRepository.save(user);


        //Creates and saves cart once user registration is successful
        Cart cart = new Cart();
        cart.setUser(savedUser);
        cartService.createCart(cart);

    }
}
