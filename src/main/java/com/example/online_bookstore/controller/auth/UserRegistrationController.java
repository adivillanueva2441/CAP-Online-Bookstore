package com.example.online_bookstore.controller.auth;

import com.example.online_bookstore.model.User;
import com.example.online_bookstore.service.IUserRegistrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

//Renders and handles user registration page
@Controller
public class UserRegistrationController {

    @Autowired
    private IUserRegistrationService userRegistrationService;

    // Render registration page
    @GetMapping("/register")
    public String registrationPage(Model model) {
        model.addAttribute("user", new User());
        return "auth/register";
    }

    //Handle registration form submission
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("user") User user,
                               BindingResult bindingResult,
                               Model model) {

        if (userRegistrationService.usernameExists(user.getUsername())) {
            bindingResult.rejectValue("username", "error.user", "Username already exists");
        }
        if (bindingResult.hasErrors()) {
            return "auth/register"; // show registration error
        }

        userRegistrationService.registerUser(user);
        model.addAttribute("successMessage", "Registration successful!");
        model.addAttribute("user", new User()); // reset form
        return "auth/register";
    }

}
