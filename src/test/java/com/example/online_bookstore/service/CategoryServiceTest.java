package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.response.CategoryDtoResponse;
import com.example.online_bookstore.model.Category;
import com.example.online_bookstore.repository.BookRepository;
import com.example.online_bookstore.repository.CategoryRepository;
import com.example.online_bookstore.service.impl.CategoryServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    private Category createTestCategory(Long id, String name) {
        Category category = new Category();
        category.setCategoryId(id);
        category.setCategoryName(name);
        return category;
    }

    @Test
    void getCategories_shouldReturnListOfCategoryDto() {
        // Arrange
        Category category1 = createTestCategory(1L, "Programming");
        Category category2 = createTestCategory(2L, "Fiction");

        when(categoryRepository.findAll()).thenReturn(List.of(category1, category2));

        // Act
        List<CategoryDtoResponse> result = categoryService.getCategories();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());

        assertEquals("Programming", result.get(0).getCategoryName());
        assertEquals(1L, result.get(0).getCategoryId());

        assertEquals("Fiction", result.get(1).getCategoryName());
        assertEquals(2L, result.get(1).getCategoryId());

        verify(categoryRepository, times(1)).findAll();
    }

    @Test
    void getCategories_shouldReturnEmptyListWhenNoCategories() {
        // Arrange
        when(categoryRepository.findAll()).thenReturn(List.of());

        // Act
        List<CategoryDtoResponse> result = categoryService.getCategories();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(categoryRepository, times(1)).findAll();
    }
}