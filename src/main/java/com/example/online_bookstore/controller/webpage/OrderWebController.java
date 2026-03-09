package com.example.online_bookstore.controller.webpage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderWebController {
    @GetMapping("/orders")
    public String ordersPage() {
        return "orders";
    }
}
