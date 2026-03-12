package com.example.online_bookstore.admin.service;

import com.example.online_bookstore.admin.dto.request.AdminCategoryDtoRequest;
import com.example.online_bookstore.admin.dto.response.AdminCategoryDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAdminCategoryService {
    Page<AdminCategoryDtoResponse> getAllCategories(Pageable pageable);
    AdminCategoryDtoResponse createCategory(AdminCategoryDtoRequest adminCategoryDtoRequest);
    AdminCategoryDtoResponse updateCategory(Long categoryId, AdminCategoryDtoRequest adminCategoryDtoRequest);
    void deleteCategory(Long categoryId);
    AdminCategoryDtoResponse getCategoryById(Long categoryId);
}
