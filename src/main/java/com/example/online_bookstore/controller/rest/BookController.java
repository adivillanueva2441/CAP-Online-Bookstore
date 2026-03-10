package com.example.online_bookstore.controller.rest;

import com.example.online_bookstore.dto.response.BookDtoResponse;
import com.example.online_bookstore.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/books")
public class BookController {
    @Autowired
    private IBookService bookService;

    //Retrieve book list
    @GetMapping
    public List<BookDtoResponse> getBooks() {
        return bookService.getAllBooks();
    }

    //Search books by title
    @GetMapping("/search")
    public List<BookDtoResponse> searchBooks(@RequestParam("title") String title){
        return bookService.findBooksByTitle(title);
    }

    @GetMapping("/category/{categoryId}")
    public List<BookDtoResponse> getBooksByCategory(@PathVariable Long categoryId) {
        return bookService.filterByCategory(categoryId);
    }


    // View book details individually
    @GetMapping("/{bookId}")
    public BookDtoResponse getBook(@PathVariable Long bookId) {
        return bookService.getBookById(bookId);
    }





}

