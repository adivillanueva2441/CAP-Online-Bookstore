package com.example.online_bookstore.service;

import com.example.online_bookstore.model.User;

public interface IUserRegistrationService {
    void registerUser(User user);
    boolean usernameExists(String username);
}
