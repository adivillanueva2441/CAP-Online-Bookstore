package com.example.online_bookstore.controller.webpage;

import com.example.online_bookstore.service.impl.BookServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

//Renders bookstore homepage
@Controller
public class BookStoreController {
    @Autowired
    private BookServiceImpl bookServiceImpl;

    @GetMapping("/")
    public String bookstorePage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        String username = auth.getName(); // gets logged-in username
        model.addAttribute("username", username);

        return "bookstore";
    }

}
