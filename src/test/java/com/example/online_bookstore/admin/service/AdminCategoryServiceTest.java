package com.example.online_bookstore.admin.service;

import com.example.online_bookstore.admin.dto.request.AdminCategoryDtoRequest;
import com.example.online_bookstore.admin.dto.response.AdminCategoryDtoResponse;
import com.example.online_bookstore.admin.service.impl.AdminCategoryServiceImpl;
import com.example.online_bookstore.model.Category;
import com.example.online_bookstore.repository.BookRepository;
import com.example.online_bookstore.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminCategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private AdminCategoryServiceImpl adminCategoryService;

    private Category sampleCategory;
    private AdminCategoryDtoRequest sampleRequest;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        sampleCategory = new Category();
        sampleCategory.setCategoryId(1L);
        sampleCategory.setCategoryName("Programming");

        sampleRequest = new AdminCategoryDtoRequest();
        sampleRequest.setCategoryName("Programming");

        pageable = PageRequest.of(0, 12);
    }

    // ─── getAllCategories ─────────────────────────────────────────────────────

    @Test
    void getAllCategories_shouldReturnPageOfCategories() {
        Page<Category> categoryPage = new PageImpl<>(List.of(sampleCategory), pageable, 1);
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        Page<AdminCategoryDtoResponse> result = adminCategoryService.getAllCategories(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Programming", result.getContent().get(0).getCategoryName());
        verify(categoryRepository).findAll(pageable);
    }

    @Test
    void getAllCategories_shouldReturnEmptyPageWhenNoCategoriesExist() {
        when(categoryRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));

        Page<AdminCategoryDtoResponse> result = adminCategoryService.getAllCategories(pageable);

        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getAllCategories_shouldReturnAllCategoriesWhenMultipleCategoriesExist() {
        Category secondCategory = new Category();
        secondCategory.setCategoryId(2L);
        secondCategory.setCategoryName("Fiction");

        Page<Category> categoryPage = new PageImpl<>(List.of(sampleCategory, secondCategory));
        when(categoryRepository.findAll(pageable)).thenReturn(categoryPage);

        Page<AdminCategoryDtoResponse> result = adminCategoryService.getAllCategories(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("Programming", result.getContent().get(0).getCategoryName());
        assertEquals("Fiction", result.getContent().get(1).getCategoryName());
    }

    // ─── getCategoryById ──────────────────────────────────────────────────────

    @Test
    void getCategoryById_shouldReturnCategoryWhenIdExists() {
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));

        AdminCategoryDtoResponse result = adminCategoryService.getCategoryById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getCategoryId());
        assertEquals("Programming", result.getCategoryName());
        verify(categoryRepository).findById(1L);
    }

    @Test
    void getCategoryById_shouldThrowExceptionWhenCategoryNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminCategoryService.getCategoryById(999L));

        assertEquals("Category not found", ex.getMessage());
        verify(categoryRepository).findById(999L);
    }

    // ─── createCategory ───────────────────────────────────────────────────────

    @Test
    void createCategory_shouldSaveAndReturnCategory() {
        when(categoryRepository.existsByCategoryName("Programming")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(sampleCategory);

        AdminCategoryDtoResponse result = adminCategoryService.createCategory(sampleRequest);

        assertNotNull(result);
        assertEquals("Programming", result.getCategoryName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void createCategory_shouldThrowExceptionWhenCategoryAlreadyExists() {
        when(categoryRepository.existsByCategoryName("Programming")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminCategoryService.createCategory(sampleRequest));

        assertEquals("Category already exists", ex.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    @Test
    void createCategory_shouldMapAllFieldsCorrectly() {
        when(categoryRepository.existsByCategoryName("Programming")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenReturn(sampleCategory);

        AdminCategoryDtoResponse result = adminCategoryService.createCategory(sampleRequest);

        assertAll(
                () -> assertEquals(1L, result.getCategoryId()),
                () -> assertEquals("Programming", result.getCategoryName())
        );
    }

    // ─── updateCategory ───────────────────────────────────────────────────────

    @Test
    void updateCategory_shouldUpdateAndReturnCategory() {
        AdminCategoryDtoRequest updateRequest = new AdminCategoryDtoRequest();
        updateRequest.setCategoryName("Science Fiction");

        Category updatedCategory = new Category();
        updatedCategory.setCategoryId(1L);
        updatedCategory.setCategoryName("Science Fiction");

        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));
        when(categoryRepository.save(any(Category.class))).thenReturn(updatedCategory);

        AdminCategoryDtoResponse result = adminCategoryService.updateCategory(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Science Fiction", result.getCategoryName());
        verify(categoryRepository).save(any(Category.class));
    }

    @Test
    void updateCategory_shouldThrowExceptionWhenCategoryNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminCategoryService.updateCategory(999L, sampleRequest));

        assertEquals("Category not found", ex.getMessage());
        verify(categoryRepository, never()).save(any(Category.class));
    }

    // ─── deleteCategory ───────────────────────────────────────────────────────

    @Test
    void deleteCategory_shouldDeleteCategorySuccessfully() {
        when(bookRepository.existsByCategoryCategoryId(1L)).thenReturn(false);
        when(categoryRepository.existsById(1L)).thenReturn(true);

        adminCategoryService.deleteCategory(1L);

        verify(categoryRepository).deleteById(1L);
    }

    @Test
    void deleteCategory_shouldThrowExceptionWhenCategoryHasExistingBooks() {
        when(bookRepository.existsByCategoryCategoryId(1L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminCategoryService.deleteCategory(1L));

        assertEquals("Cannot delete category with existing books", ex.getMessage());
        verify(categoryRepository, never()).deleteById(any());
    }

    @Test
    void deleteCategory_shouldThrowExceptionWhenCategoryNotFound() {
        when(bookRepository.existsByCategoryCategoryId(999L)).thenReturn(false);
        when(categoryRepository.existsById(999L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminCategoryService.deleteCategory(999L));

        assertEquals("Category not found", ex.getMessage());
        verify(categoryRepository, never()).deleteById(any());
    }
}
