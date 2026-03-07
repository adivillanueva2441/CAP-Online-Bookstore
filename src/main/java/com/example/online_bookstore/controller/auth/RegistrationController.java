package com.example.online_bookstore.controller.auth;

import com.example.online_bookstore.model.User;
import com.example.online_bookstore.service.UserRegistrationService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

//Renders and handles user registration page
@Controller
public class RegistrationController {

    private final UserRegistrationService userRegistrationService;

    public RegistrationController(UserRegistrationService userRegistrationService) {
        this.userRegistrationService = userRegistrationService;
    }

    // Render registration page
    @GetMapping("/register")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    //Handle registration form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user) {
        userRegistrationService.registerUser(user);
        return "redirect:auth/login";
    }

}
