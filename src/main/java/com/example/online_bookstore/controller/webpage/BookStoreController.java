package com.example.online_bookstore.controller.webpage;

import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

//Renders bookstore homepage
@Controller
public class BookStoreController {
    @Autowired
    private BookService bookService;

    @GetMapping("/")
    public String bookstorePage() {
        return "bookstore";
    }

}
