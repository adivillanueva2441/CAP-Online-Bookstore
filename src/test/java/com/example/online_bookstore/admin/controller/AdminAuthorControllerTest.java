package com.example.online_bookstore.admin.controller;

import com.example.online_bookstore.admin.controller.rest.AdminAuthorController;
import com.example.online_bookstore.admin.dto.response.AdminAuthorDtoResponse;
import com.example.online_bookstore.admin.service.IAdminAuthorService;
import com.example.online_bookstore.model.Author;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminAuthorControllerTest {

    @Mock
    private IAdminAuthorService adminAuthorService;

    @InjectMocks
    private AdminAuthorController adminAuthorController;

    private AdminAuthorDtoResponse sampleResponse;

    @BeforeEach
    void setUp() {
        Author author = new Author();
        author.setAuthorId(1L);
        author.setName("Robert C. Martin");

        sampleResponse = new AdminAuthorDtoResponse(author);
    }

    // ─── GET /api/admin/authors ───────────────────────────────────────────────

    @Test
    void getAllAuthors_shouldReturnListOfAuthors() {
        when(adminAuthorService.getAllAuthors()).thenReturn(List.of(sampleResponse));

        List<AdminAuthorDtoResponse> result = adminAuthorController.getAllAuthors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Robert C. Martin", result.get(0).getAuthorName());
        verify(adminAuthorService).getAllAuthors();
    }

    @Test
    void getAllAuthors_shouldReturnEmptyListWhenNoAuthorsExist() {
        when(adminAuthorService.getAllAuthors()).thenReturn(List.of());

        List<AdminAuthorDtoResponse> result = adminAuthorController.getAllAuthors();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(adminAuthorService).getAllAuthors();
    }

    @Test
    void getAllAuthors_shouldReturnAllAuthorsWhenMultipleAuthorsExist() {
        Author secondAuthor = new Author();
        secondAuthor.setAuthorId(2L);
        secondAuthor.setName("Andrew Hunt");

        AdminAuthorDtoResponse secondResponse = new AdminAuthorDtoResponse(secondAuthor);
        when(adminAuthorService.getAllAuthors()).thenReturn(List.of(sampleResponse, secondResponse));

        List<AdminAuthorDtoResponse> result = adminAuthorController.getAllAuthors();

        assertEquals(2, result.size());
        assertEquals("Robert C. Martin", result.get(0).getAuthorName());
        assertEquals("Andrew Hunt", result.get(1).getAuthorName());
    }

    @Test
    void getAllAuthors_shouldMapAllFieldsCorrectly() {
        when(adminAuthorService.getAllAuthors()).thenReturn(List.of(sampleResponse));

        AdminAuthorDtoResponse result = adminAuthorController.getAllAuthors().get(0);

        assertAll(
                () -> assertEquals(1L, result.getAuthorId()),
                () -> assertEquals("Robert C. Martin", result.getAuthorName())
        );
    }
}
