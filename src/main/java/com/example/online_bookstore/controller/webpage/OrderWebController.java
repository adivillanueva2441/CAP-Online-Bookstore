package com.example.online_bookstore.controller.webpage;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderWebController {
    @GetMapping("/orders")
    public String ordersPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        String username = auth.getName(); // gets logged-in username
        model.addAttribute("username", username);

        return "orders";
    }
}
