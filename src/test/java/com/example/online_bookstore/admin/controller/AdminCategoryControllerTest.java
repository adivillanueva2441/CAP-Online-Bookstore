package com.example.online_bookstore.admin.controller;


import com.example.online_bookstore.admin.controller.rest.AdminCategoryController;
import com.example.online_bookstore.admin.dto.request.AdminCategoryDtoRequest;
import com.example.online_bookstore.admin.dto.response.AdminCategoryDtoResponse;
import com.example.online_bookstore.admin.service.IAdminCategoryService;
import com.example.online_bookstore.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminCategoryControllerTest {

    @Mock
    private IAdminCategoryService adminCategoryService;

    @InjectMocks
    private AdminCategoryController adminCategoryController;

    private AdminCategoryDtoResponse sampleResponse;
    private AdminCategoryDtoRequest sampleRequest;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        Category category = new Category();
        category.setCategoryId(1L);
        category.setCategoryName("Programming");

        sampleResponse = new AdminCategoryDtoResponse(category);

        sampleRequest = new AdminCategoryDtoRequest();
        sampleRequest.setCategoryName("Programming");

        pageable = PageRequest.of(0, 12);
    }

    // ─── GET /api/admin/categories ────────────────────────────────────────────

    @Test
    void getAllCategories_shouldReturnPageOfCategories() {
        Page<AdminCategoryDtoResponse> categoryPage = new PageImpl<>(List.of(sampleResponse), pageable, 1);
        when(adminCategoryService.getAllCategories(pageable)).thenReturn(categoryPage);

        Page<AdminCategoryDtoResponse> result = adminCategoryController.getAllCategories(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Programming", result.getContent().get(0).getCategoryName());
        verify(adminCategoryService).getAllCategories(pageable);
    }

    @Test
    void getAllCategories_shouldReturnEmptyPageWhenNoCategoriesExist() {
        when(adminCategoryService.getAllCategories(pageable)).thenReturn(new PageImpl<>(List.of()));

        Page<AdminCategoryDtoResponse> result = adminCategoryController.getAllCategories(pageable);

        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getAllCategories_shouldReturnAllCategoriesWhenMultipleCategoriesExist() {
        Category secondCategory = new Category();
        secondCategory.setCategoryId(2L);
        secondCategory.setCategoryName("Fiction");

        AdminCategoryDtoResponse secondResponse = new AdminCategoryDtoResponse(secondCategory);
        Page<AdminCategoryDtoResponse> categoryPage = new PageImpl<>(List.of(sampleResponse, secondResponse));
        when(adminCategoryService.getAllCategories(pageable)).thenReturn(categoryPage);

        Page<AdminCategoryDtoResponse> result = adminCategoryController.getAllCategories(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("Programming", result.getContent().get(0).getCategoryName());
        assertEquals("Fiction", result.getContent().get(1).getCategoryName());
    }

    // ─── GET /api/admin/categories/{categoryId} ───────────────────────────────

    @Test
    void getCategoryById_shouldReturnCategoryWhenIdExists() {
        when(adminCategoryService.getCategoryById(1L)).thenReturn(sampleResponse);

        AdminCategoryDtoResponse result = adminCategoryController.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getCategoryId());
        assertEquals("Programming", result.getCategoryName());
        verify(adminCategoryService).getCategoryById(1L);
    }

    @Test
    void getCategoryById_shouldThrowExceptionWhenCategoryNotFound() {
        when(adminCategoryService.getCategoryById(999L))
                .thenThrow(new RuntimeException("Category not found"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminCategoryController.getCategoryById(999L));

        assertEquals("Category not found", ex.getMessage());
    }

    // ─── POST /api/admin/categories/add ──────────────────────────────────────

    @Test
    void createCategory_shouldCreateAndReturnCategory() {
        when(adminCategoryService.createCategory(sampleRequest)).thenReturn(sampleResponse);

        AdminCategoryDtoResponse result = adminCategoryController.createCategory(sampleRequest);

        assertNotNull(result);
        assertEquals("Programming", result.getCategoryName());
        verify(adminCategoryService).createCategory(sampleRequest);
    }

    @Test
    void createCategory_shouldThrowExceptionWhenCategoryAlreadyExists() {
        when(adminCategoryService.createCategory(sampleRequest))
                .thenThrow(new RuntimeException("Category already exists"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminCategoryController.createCategory(sampleRequest));

        assertEquals("Category already exists", ex.getMessage());
    }

    // ─── PUT /api/admin/categories/update/{categoryId} ────────────────────────

    @Test
    void updateCategory_shouldUpdateAndReturnCategory() {
        when(adminCategoryService.updateCategory(eq(1L), any(AdminCategoryDtoRequest.class)))
                .thenReturn(sampleResponse);

        AdminCategoryDtoResponse result = adminCategoryController.updateCategory(1L, sampleRequest);

        assertNotNull(result);
        assertEquals("Programming", result.getCategoryName());
        verify(adminCategoryService).updateCategory(eq(1L), any(AdminCategoryDtoRequest.class));
    }

    @Test
    void updateCategory_shouldThrowExceptionWhenCategoryNotFound() {
        when(adminCategoryService.updateCategory(eq(999L), any(AdminCategoryDtoRequest.class)))
                .thenThrow(new RuntimeException("Category not found"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminCategoryController.updateCategory(999L, sampleRequest));

        assertEquals("Category not found", ex.getMessage());
    }

    // ─── DELETE /api/admin/categories/delete/{categoryId} ────────────────────

    @Test
    void deleteCategory_shouldDeleteCategorySuccessfully() {
        doNothing().when(adminCategoryService).deleteCategory(1L);

        adminCategoryController.deleteCategory(1L);

        verify(adminCategoryService).deleteCategory(1L);
    }

    @Test
    void deleteCategory_shouldThrowExceptionWhenCategoryNotFound() {
        doThrow(new RuntimeException("Category not found"))
                .when(adminCategoryService).deleteCategory(999L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminCategoryController.deleteCategory(999L));

        assertEquals("Category not found", ex.getMessage());
    }

    @Test
    void deleteCategory_shouldThrowExceptionWhenCategoryHasExistingBooks() {
        doThrow(new RuntimeException("Cannot delete category with existing books"))
                .when(adminCategoryService).deleteCategory(1L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminCategoryController.deleteCategory(1L));

        assertEquals("Cannot delete category with existing books", ex.getMessage());
    }
}