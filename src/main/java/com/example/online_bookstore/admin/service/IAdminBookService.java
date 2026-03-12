package com.example.online_bookstore.admin.service;

import com.example.online_bookstore.admin.dto.request.AdminBookDtoRequest;
import com.example.online_bookstore.admin.dto.response.AdminBookDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAdminBookService {
    Page<AdminBookDtoResponse> getAllBooks(Pageable pageable);
    AdminBookDtoResponse getBookById(Long id);
    AdminBookDtoResponse createBook(AdminBookDtoRequest adminBookDtoRequest);
    AdminBookDtoResponse updateBook(Long id, AdminBookDtoRequest adminBookDtoRequest);
    void deleteBook(Long id);
}
