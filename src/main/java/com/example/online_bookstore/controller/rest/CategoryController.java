package com.example.online_bookstore.controller.rest;

import com.example.online_bookstore.dto.response.BookDtoResponse;
import com.example.online_bookstore.dto.response.CategoryDtoResponse;
import com.example.online_bookstore.model.Category;
import com.example.online_bookstore.service.IBookService;
import com.example.online_bookstore.service.ICategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api")
public class CategoryController {

    @Autowired
    private ICategoryService categoryService;
    @Autowired
    private IBookService bookService;

    @GetMapping("/category")
    public List<CategoryDtoResponse> getAllCategories() {
        return categoryService.getCategories();
    }

}
