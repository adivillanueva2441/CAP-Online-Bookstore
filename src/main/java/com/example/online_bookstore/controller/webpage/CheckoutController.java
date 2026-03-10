package com.example.online_bookstore.controller.webpage;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckoutController {

    @GetMapping("/cart/checkout")
    public String checkoutPage(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        assert auth != null;
        String username = auth.getName(); // gets logged-in username
        model.addAttribute("username", username);
        return "checkout";
    }
}
