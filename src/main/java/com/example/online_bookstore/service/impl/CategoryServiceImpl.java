package com.example.online_bookstore.service.impl;

import com.example.online_bookstore.dto.response.BookDtoResponse;
import com.example.online_bookstore.dto.response.CategoryDtoResponse;
import com.example.online_bookstore.model.Category;
import com.example.online_bookstore.repository.BookRepository;
import com.example.online_bookstore.repository.CategoryRepository;
import com.example.online_bookstore.service.IBookService;
import com.example.online_bookstore.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImpl implements ICategoryService {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BookRepository bookRepository;

    @Override
    public List<CategoryDtoResponse> getCategories() {
        List<Category> category = categoryRepository.findAll();
        return category.stream()
                .map(CategoryDtoResponse::new)
                .collect(Collectors.toList());
    }

}
