package com.example.online_bookstore.service;

import com.example.online_bookstore.model.User;

public interface IUserService {
    User findByUsername(String username);
}
