package com.example.online_bookstore.admin.controller.webpage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/books")
public class AdminBookWebController {

    @GetMapping
    public String adminBooks() {
        return "admin/books/admin_books";
    }

    @GetMapping("/create")
    public String adminBookCreate() {
        return "admin/books/admin_book_create";
    }

    @GetMapping("/update/{bookId}")
    public String adminBookUpdate() {
        return "admin/books/admin_book_edit";
    }
}
