package com.example.online_bookstore.controller.webpage;

import com.example.online_bookstore.service.impl.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Renders bookstore homepage
@Controller
public class BookStoreController {
    @Autowired
    private BookServiceImpl bookServiceImpl;

    @GetMapping("/")
    public String bookstorePage() {
        return "bookstore";
    }

}
