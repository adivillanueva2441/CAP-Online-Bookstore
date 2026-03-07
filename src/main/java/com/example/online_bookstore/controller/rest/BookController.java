package com.example.online_bookstore.controller.rest;

import com.example.online_bookstore.dto.BookDto;
import com.example.online_bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;

    //Retrieve book list
    @GetMapping
    public List<BookDto> getBooks() {
        return bookService.getAllBooks();
    }

    //Search books by title
    @GetMapping("/search")
    public List<BookDto> searchBooks(@RequestParam("title") String title){
        return bookService.findBooksByTitle(title);
    }



}

