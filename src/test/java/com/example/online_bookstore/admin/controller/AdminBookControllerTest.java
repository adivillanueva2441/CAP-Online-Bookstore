package com.example.online_bookstore.admin.controller;


import com.example.online_bookstore.admin.controller.rest.AdminBookController;
import com.example.online_bookstore.admin.dto.request.AdminBookDtoRequest;
import com.example.online_bookstore.admin.dto.response.AdminBookDtoResponse;
import com.example.online_bookstore.admin.service.IAdminBookService;
import com.example.online_bookstore.model.Author;
import com.example.online_bookstore.model.Book;
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
class AdminBookControllerTest {

    @Mock
    private IAdminBookService adminBookService;

    @InjectMocks
    private AdminBookController adminBookController;

    private AdminBookDtoResponse sampleResponse;
    private AdminBookDtoRequest sampleRequest;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        Author author = new Author();
        author.setName("Robert C. Martin");

        Category category = new Category();
        category.setCategoryName("Programming");

        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Clean Code");
        book.setDescription("A handbook of agile software craftsmanship.");
        book.setPrice(29.99);
        book.setAuthor(author);
        book.setCategory(category);

        sampleResponse = new AdminBookDtoResponse(book);

        sampleRequest = new AdminBookDtoRequest();
        sampleRequest.setTitle("Clean Code");
        sampleRequest.setDescription("A handbook of agile software craftsmanship.");
        sampleRequest.setPrice(29.99);
        sampleRequest.setAuthorId(1L);
        sampleRequest.setCategoryId(1L);

        pageable = PageRequest.of(0, 12);
    }

    // ─── GET /api/admin/books ─────────────────────────────────────────────────

    @Test
    void getAllBooks_shouldReturnPageOfBooks() {
        Page<AdminBookDtoResponse> bookPage = new PageImpl<>(List.of(sampleResponse), pageable, 1);
        when(adminBookService.getAllBooks(pageable)).thenReturn(bookPage);

        Page<AdminBookDtoResponse> result = adminBookController.getAllBooks(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Clean Code", result.getContent().get(0).getTitle());
        verify(adminBookService).getAllBooks(pageable);
    }

    @Test
    void getAllBooks_shouldReturnEmptyPageWhenNoBooksExist() {
        when(adminBookService.getAllBooks(pageable)).thenReturn(new PageImpl<>(List.of()));

        Page<AdminBookDtoResponse> result = adminBookController.getAllBooks(pageable);

        assertTrue(result.getContent().isEmpty());
    }

    // ─── GET /api/admin/books/{bookId} ────────────────────────────────────────

    @Test
    void getBookById_shouldReturnBookWhenIdExists() {
        when(adminBookService.getBookById(1L)).thenReturn(sampleResponse);

        AdminBookDtoResponse result = adminBookController.getBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getBookId());
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert C. Martin", result.getAuthorName());
        assertEquals("Programming", result.getCategoryName());
        verify(adminBookService).getBookById(1L);
    }

    @Test
    void getBookById_shouldThrowExceptionWhenBookNotFound() {
        when(adminBookService.getBookById(999L)).thenThrow(new RuntimeException("Book not found"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminBookController.getBookById(999L));

        assertEquals("Book not found", ex.getMessage());
    }

    // ─── POST /api/admin/books/add ────────────────────────────────────────────

    @Test
    void addBook_shouldCreateAndReturnBook() {
        when(adminBookService.createBook(sampleRequest)).thenReturn(sampleResponse);

        AdminBookDtoResponse result = adminBookController.addBook(sampleRequest);

        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert C. Martin", result.getAuthorName());
        verify(adminBookService).createBook(sampleRequest);
    }

    @Test
    void addBook_shouldThrowExceptionWhenBookAlreadyExists() {
        when(adminBookService.createBook(sampleRequest))
                .thenThrow(new RuntimeException("Book already exists"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminBookController.addBook(sampleRequest));

        assertEquals("Book already exists", ex.getMessage());
    }

    // ─── PUT /api/admin/books/update/{bookId} ─────────────────────────────────

    @Test
    void updateBook_shouldUpdateAndReturnBook() {
        when(adminBookService.updateBook(eq(1L), any(AdminBookDtoRequest.class)))
                .thenReturn(sampleResponse);

        AdminBookDtoResponse result = adminBookController.updateBook(1L, sampleRequest);

        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
        verify(adminBookService).updateBook(eq(1L), any(AdminBookDtoRequest.class));
    }

    @Test
    void updateBook_shouldThrowExceptionWhenBookNotFound() {
        when(adminBookService.updateBook(eq(999L), any(AdminBookDtoRequest.class)))
                .thenThrow(new RuntimeException("Book Not Found"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminBookController.updateBook(999L, sampleRequest));

        assertEquals("Book Not Found", ex.getMessage());
    }

    // ─── DELETE /api/admin/books/delete/{bookId} ──────────────────────────────

    @Test
    void deleteBook_shouldDeleteBookSuccessfully() {
        doNothing().when(adminBookService).deleteBook(1L);

        adminBookController.deleteBook(1L);

        verify(adminBookService).deleteBook(1L);
    }

    @Test
    void deleteBook_shouldThrowExceptionWhenBookNotFound() {
        doThrow(new RuntimeException("Book not found"))
                .when(adminBookService).deleteBook(999L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminBookController.deleteBook(999L));

        assertEquals("Book not found", ex.getMessage());
    }

    @Test
    void deleteBook_shouldThrowExceptionWhenBookHasExistingOrders() {
        doThrow(new RuntimeException("Cannot delete book with existing orders"))
                .when(adminBookService).deleteBook(1L);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminBookController.deleteBook(1L));

        assertEquals("Cannot delete book with existing orders", ex.getMessage());
    }
}
