package com.example.online_bookstore.controller.rest;

import com.example.online_bookstore.model.User;
import com.example.online_bookstore.service.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserServiceImpl userServiceImpl;

    @GetMapping("/profile")
    public User getProfile(Authentication authentication) {
        String username = authentication.getName();
        return userServiceImpl.findByUsername(username);
    }
}
