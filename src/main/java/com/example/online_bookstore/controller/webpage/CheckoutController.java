package com.example.online_bookstore.controller.webpage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CheckoutController {

    @GetMapping("/cart/checkout")
    public String checkoutPage() {
        return "checkout";
    }
}
