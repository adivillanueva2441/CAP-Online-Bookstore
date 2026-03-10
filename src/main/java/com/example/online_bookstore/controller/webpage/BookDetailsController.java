package com.example.online_bookstore.controller.webpage;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/books")
public class BookDetailsController {

    @GetMapping("/{bookId}")
    public String bookDetailsPage(@PathVariable Long bookId, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        String username = auth.getName(); // gets logged-in username
        model.addAttribute("username", username);

        return "book_details";
    }
}