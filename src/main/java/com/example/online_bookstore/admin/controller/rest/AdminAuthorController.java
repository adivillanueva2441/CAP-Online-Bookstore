package com.example.online_bookstore.admin.controller.rest;

import com.example.online_bookstore.admin.dto.response.AdminAuthorDtoResponse;
import com.example.online_bookstore.admin.service.IAdminAuthorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin/authors")
public class AdminAuthorController {
    @Autowired
    private IAdminAuthorService adminAuthorService;

    @GetMapping
    public List<AdminAuthorDtoResponse> getAllAuthors() {
        return adminAuthorService.getAllAuthors();
    }
}
