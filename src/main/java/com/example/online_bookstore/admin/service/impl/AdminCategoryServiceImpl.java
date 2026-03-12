package com.example.online_bookstore.admin.service.impl;

import com.example.online_bookstore.admin.dto.request.AdminCategoryDtoRequest;
import com.example.online_bookstore.admin.dto.response.AdminCategoryDtoResponse;
import com.example.online_bookstore.admin.service.IAdminCategoryService;
import com.example.online_bookstore.model.Category;
import com.example.online_bookstore.repository.BookRepository;
import com.example.online_bookstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminCategoryServiceImpl implements IAdminCategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private BookRepository bookRepository;

    @Override
    public Page<AdminCategoryDtoResponse> getAllCategories(Pageable pageable) {
        return categoryRepository.findAll(pageable)
                .map(AdminCategoryDtoResponse::new);
    }
    
    @Override
    public AdminCategoryDtoResponse getCategoryById(Long categoryId) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        return new AdminCategoryDtoResponse(category);
    }

    @Override
    public AdminCategoryDtoResponse createCategory(AdminCategoryDtoRequest adminCategoryDtoRequest) {
        if (categoryRepository.existsByCategoryName(adminCategoryDtoRequest.getCategoryName())) {
            throw new RuntimeException("Category already exists");
        }
        Category category = new Category();
        category.setCategoryName(adminCategoryDtoRequest.getCategoryName());
        return new AdminCategoryDtoResponse(categoryRepository.save(category));
    }

    @Override
    public AdminCategoryDtoResponse updateCategory(Long categoryId, AdminCategoryDtoRequest adminCategoryDtoRequest) {
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found"));
        category.setCategoryName(adminCategoryDtoRequest.getCategoryName());
        return new AdminCategoryDtoResponse(categoryRepository.save(category));
    }


    @Override
    public void deleteCategory(Long categoryId) {
        if (bookRepository.existsByCategoryCategoryId(categoryId)) {
            throw new RuntimeException("Cannot delete category with existing books");
        }
        if(!categoryRepository.existsById(categoryId)) {
            throw new RuntimeException("Category not found");
        }
        categoryRepository.deleteById(categoryId);
    }
}
