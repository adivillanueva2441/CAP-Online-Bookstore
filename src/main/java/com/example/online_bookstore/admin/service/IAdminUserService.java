package com.example.online_bookstore.admin.service;

import com.example.online_bookstore.admin.dto.response.AdminUserDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAdminUserService {
    Page<AdminUserDtoResponse> getAllUsers(Pageable pageable);
}
