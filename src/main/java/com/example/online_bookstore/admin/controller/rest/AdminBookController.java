package com.example.online_bookstore.admin.controller.rest;

import com.example.online_bookstore.admin.dto.request.AdminBookDtoRequest;
import com.example.online_bookstore.admin.dto.response.AdminBookDtoResponse;
import com.example.online_bookstore.admin.service.IAdminBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/books")
public class AdminBookController {

    @Autowired
    private IAdminBookService adminBookService;

    @GetMapping
    public Page<AdminBookDtoResponse> getAllBooks(Pageable pageable) {
        return adminBookService.getAllBooks(pageable);
    }

    @GetMapping("/{bookId}")
    public AdminBookDtoResponse getBookById(@PathVariable Long bookId) {
        return adminBookService.getBookById(bookId);
    }

    @PostMapping("/add")
    public AdminBookDtoResponse addBook(@RequestBody AdminBookDtoRequest adminBookDtoRequest) {
        return adminBookService.createBook(adminBookDtoRequest);
    }

    @PutMapping("/update/{bookId}")
    public AdminBookDtoResponse updateBook(@PathVariable Long bookId,
                                           @RequestBody AdminBookDtoRequest adminBookDtoRequest) {
        return adminBookService.updateBook(bookId, adminBookDtoRequest);
    }

    @DeleteMapping("/delete/{bookId}")
    public void deleteBook(@PathVariable Long bookId) {
        adminBookService.deleteBook(bookId);
    }




}
