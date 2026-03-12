package com.example.online_bookstore.admin.controller.webpage;

import com.example.online_bookstore.admin.service.IAdminUserService;
import com.example.online_bookstore.model.User;
import com.example.online_bookstore.service.IUserRegistrationService;
import com.example.online_bookstore.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/users")
public class AdminUserWebController {

    @Autowired
    private IUserRegistrationService userRegistrationService;

    @GetMapping
    public String usersPage(Model model) {
        model.addAttribute("user", new User());
        return "admin/users/user_list";
    }

    @GetMapping("/create")
    public String createUserPage(Model model) {
        model.addAttribute("user", new User());
        return "admin/users/create_user";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("user") User user, Model model) {
        if (userRegistrationService.usernameExists(user.getUsername())) {
            model.addAttribute("errorMessage", "Username already exists");
            model.addAttribute("user", user); // re-bind the user so form fields retain their values
            return "admin/users/create_user";
        }
        userRegistrationService.registerUser(user);
        return "redirect:/admin/users";
    }





}
