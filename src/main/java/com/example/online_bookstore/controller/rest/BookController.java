package com.example.online_bookstore.controller.rest;

import com.example.online_bookstore.dto.response.BookDtoResponse;
import com.example.online_bookstore.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {

    @Autowired
    private IBookService bookService;

    @GetMapping
    public Page<BookDtoResponse> getBooks(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return bookService.getAllBooks(pageable);
    }

    @GetMapping("/search")
    public Page<BookDtoResponse> searchBooks(
            @RequestParam("title") String title,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return bookService.findBooksByTitle(title, pageable);
    }

    @GetMapping("/category/{categoryId}")
    public Page<BookDtoResponse> getBooksByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "12") int size) {

        Pageable pageable = PageRequest.of(page, size);
        return bookService.filterByCategory(categoryId, pageable);
    }


    // View book details individually
    @GetMapping("/{bookId}")
    public BookDtoResponse getBook(@PathVariable Long bookId) {
        return bookService.getBookById(bookId);
    }





}

