package com.example.online_bookstore.admin.controller.rest;

import com.example.online_bookstore.admin.dto.response.AdminUserDtoResponse;
import com.example.online_bookstore.admin.service.IAdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/users")
public class AdminUserController {

    @Autowired
    private IAdminUserService adminUserService;

    @GetMapping
    public Page<AdminUserDtoResponse> getAllUsers(Pageable pageable) {
        return adminUserService.getAllUsers(pageable);
    }



}
