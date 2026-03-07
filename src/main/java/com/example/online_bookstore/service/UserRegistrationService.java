package com.example.online_bookstore.service;

import com.example.online_bookstore.model.Cart;
import com.example.online_bookstore.model.Order;
import com.example.online_bookstore.model.User;
import com.example.online_bookstore.repository.CartRepository;
import com.example.online_bookstore.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.example.online_bookstore.config.SecurityConfig;
import org.springframework.util.StringUtils;

@Service
public class UserRegistrationService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    public UserRegistrationService(UserRepository userRepository,
                                   PasswordEncoder passwordEncoder,
                                   CartRepository cartRepository) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

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
        cartRepository.save(cart);

    }
}
