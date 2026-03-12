package com.example.online_bookstore.admin.service;

import com.example.online_bookstore.admin.dto.response.AdminAuthorDtoResponse;
import com.example.online_bookstore.admin.service.impl.AdminAuthorServiceImpl;
import com.example.online_bookstore.model.Author;
import com.example.online_bookstore.repository.AuthorRepository;
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
class AdminAuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    @InjectMocks
    private AdminAuthorServiceImpl adminAuthorService;

    private Author sampleAuthor;

    @BeforeEach
    void setUp() {
        sampleAuthor = new Author();
        sampleAuthor.setAuthorId(1L);
        sampleAuthor.setName("Robert C. Martin");
    }

    // ─── getAllAuthors ────────────────────────────────────────────────────────

    @Test
    void getAllAuthors_shouldReturnListOfAuthors() {
        when(authorRepository.findAll()).thenReturn(List.of(sampleAuthor));

        List<AdminAuthorDtoResponse> result = adminAuthorService.getAllAuthors();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Robert C. Martin", result.get(0).getAuthorName());
        verify(authorRepository).findAll();
    }

    @Test
    void getAllAuthors_shouldReturnEmptyListWhenNoAuthorsExist() {
        when(authorRepository.findAll()).thenReturn(List.of());

        List<AdminAuthorDtoResponse> result = adminAuthorService.getAllAuthors();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(authorRepository).findAll();
    }

    @Test
    void getAllAuthors_shouldReturnAllAuthorsWhenMultipleAuthorsExist() {
        Author secondAuthor = new Author();
        secondAuthor.setAuthorId(2L);
        secondAuthor.setName("Andrew Hunt");

        when(authorRepository.findAll()).thenReturn(List.of(sampleAuthor, secondAuthor));

        List<AdminAuthorDtoResponse> result = adminAuthorService.getAllAuthors();

        assertEquals(2, result.size());
        assertEquals("Robert C. Martin", result.get(0).getAuthorName());
        assertEquals("Andrew Hunt", result.get(1).getAuthorName());
    }

    @Test
    void getAllAuthors_shouldMapAllFieldsCorrectly() {
        when(authorRepository.findAll()).thenReturn(List.of(sampleAuthor));

        AdminAuthorDtoResponse result = adminAuthorService.getAllAuthors().get(0);

        assertAll(
                () -> assertEquals(1L, result.getAuthorId()),
                () -> assertEquals("Robert C. Martin", result.getAuthorName())
        );
    }
}
