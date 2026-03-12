package com.example.online_bookstore.admin.service.impl;

import com.example.online_bookstore.admin.dto.response.AdminUserDtoResponse;
import com.example.online_bookstore.admin.service.IAdminUserService;
import com.example.online_bookstore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminUserServiceImpl implements IAdminUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public Page<AdminUserDtoResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> new AdminUserDtoResponse
                        (user.getUserId(),
                        user.getUsername(),
                        user.getRole(),
                        user.getFirstName(),
                        user.getLastName()));
    }
}
