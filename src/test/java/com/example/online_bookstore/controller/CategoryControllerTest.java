package com.example.online_bookstore.controller.rest;

import com.example.online_bookstore.dto.response.CategoryDtoResponse;
import com.example.online_bookstore.service.ICategoryService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    @Mock
    private ICategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @Test
    void getAllCategories_shouldReturnCategoryList() {
        // Arrange
        CategoryDtoResponse category1 = new CategoryDtoResponse(
                new com.example.online_bookstore.model.Category() {{
                    setCategoryId(1L);
                    setCategoryName("Programming");
                }}
        );
        CategoryDtoResponse category2 = new CategoryDtoResponse(
                new com.example.online_bookstore.model.Category() {{
                    setCategoryId(2L);
                    setCategoryName("Fiction");
                }}
        );

        when(categoryService.getCategories()).thenReturn(List.of(category1, category2));

        // Act
        List<CategoryDtoResponse> result = categoryController.getAllCategories();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Programming", result.get(0).getCategoryName());
        assertEquals(1L, result.get(0).getCategoryId());
        assertEquals("Fiction", result.get(1).getCategoryName());
        assertEquals(2L, result.get(1).getCategoryId());

        verify(categoryService, times(1)).getCategories();
    }

    @Test
    void getAllCategories_shouldReturnEmptyListWhenNoCategories() {
        // Arrange
        when(categoryService.getCategories()).thenReturn(List.of());

        // Act
        List<CategoryDtoResponse> result = categoryController.getAllCategories();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(categoryService, times(1)).getCategories();
    }
}