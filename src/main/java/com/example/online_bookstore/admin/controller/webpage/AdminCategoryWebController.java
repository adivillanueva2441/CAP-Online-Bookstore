package com.example.online_bookstore.admin.controller.webpage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/categories")
public class AdminCategoryWebController {
    @GetMapping
    public String adminCategories() {
        return "admin/categories/admin_categories";
    }

    @GetMapping("/create")
    public String adminCategoryCreate() {
        return "admin/categories/admin_category_create";
    }

    @GetMapping("/update/{categoryId}")
    public String adminCategoryUpdate() {
        return "admin/categories/admin_category_edit";
    }
}
