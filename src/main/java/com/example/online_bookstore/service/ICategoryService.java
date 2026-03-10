package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.response.BookDtoResponse;
import com.example.online_bookstore.dto.response.CategoryDtoResponse;
import com.example.online_bookstore.model.Category;

import java.util.List;

public interface ICategoryService {
    List<CategoryDtoResponse> getCategories();
}
