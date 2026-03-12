package com.example.online_bookstore.admin.service;

import com.example.online_bookstore.admin.dto.response.AdminAuthorDtoResponse;

import java.util.List;

public interface IAdminAuthorService {
    List<AdminAuthorDtoResponse> getAllAuthors();
}
