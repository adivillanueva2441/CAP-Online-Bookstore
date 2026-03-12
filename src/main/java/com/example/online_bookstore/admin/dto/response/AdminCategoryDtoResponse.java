package com.example.online_bookstore.admin.dto.response;

import com.example.online_bookstore.model.Category;

public class AdminCategoryDtoResponse {
    private Long categoryId;
    private String categoryName;

    public AdminCategoryDtoResponse(Category category) {
        this.categoryId = category.getCategoryId();
        this.categoryName = category.getCategoryName();
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
