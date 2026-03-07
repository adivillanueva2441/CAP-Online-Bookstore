package com.example.online_bookstore.controller.auth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//Renders login page
@Controller
public class LoginController {
    @GetMapping("/login")
    public String loginPage() {
        return "auth/login";
    }
}
