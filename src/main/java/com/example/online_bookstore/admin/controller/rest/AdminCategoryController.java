package com.example.online_bookstore.admin.controller.rest;

import com.example.online_bookstore.admin.dto.request.AdminCategoryDtoRequest;
import com.example.online_bookstore.admin.dto.response.AdminCategoryDtoResponse;
import com.example.online_bookstore.admin.service.IAdminCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/categories")
public class AdminCategoryController {

    @Autowired
    private IAdminCategoryService adminCategoryService;

    @GetMapping
    public Page<AdminCategoryDtoResponse> getAllCategories(Pageable pageable) {
        return adminCategoryService.getAllCategories(pageable);
    }

    @GetMapping("/{categoryId}")
    public AdminCategoryDtoResponse getCategoryById(@PathVariable Long categoryId) {
        return adminCategoryService.getCategoryById(categoryId);
    }


    @PostMapping("/add")
    public AdminCategoryDtoResponse createCategory(@RequestBody AdminCategoryDtoRequest adminCategoryDtoRequest) {
        return adminCategoryService.createCategory(adminCategoryDtoRequest);
    }

    @PutMapping("/update/{categoryId}")
    public AdminCategoryDtoResponse updateCategory(@PathVariable Long categoryId,
                                                   @RequestBody AdminCategoryDtoRequest adminCategoryDtoRequest) {
        return adminCategoryService.updateCategory(categoryId, adminCategoryDtoRequest);
    }

    @DeleteMapping("/delete/{categoryId}")
    public void deleteCategory(@PathVariable Long categoryId) {
        adminCategoryService.deleteCategory(categoryId);
    }
}
