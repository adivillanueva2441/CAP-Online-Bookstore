package com.example.online_bookstore.admin.service;


import com.example.online_bookstore.admin.dto.request.AdminBookDtoRequest;
import com.example.online_bookstore.admin.dto.response.AdminBookDtoResponse;
import com.example.online_bookstore.admin.service.impl.AdminBookServiceImpl;
import com.example.online_bookstore.model.Author;
import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.model.Category;
import com.example.online_bookstore.repository.AuthorRepository;
import com.example.online_bookstore.repository.BookRepository;
import com.example.online_bookstore.repository.CategoryRepository;
import com.example.online_bookstore.repository.OrderItemsRepository;
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
class AdminBookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private AuthorRepository authorRepository;

    @Mock
    private OrderItemsRepository orderItemsRepository;

    @InjectMocks
    private AdminBookServiceImpl adminBookService;

    private Book sampleBook;
    private Author sampleAuthor;
    private Category sampleCategory;
    private AdminBookDtoRequest sampleRequest;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        sampleAuthor = new Author();
        sampleAuthor.setAuthorId(1L);
        sampleAuthor.setName("Robert C. Martin");

        sampleCategory = new Category();
        sampleCategory.setCategoryId(1L);
        sampleCategory.setCategoryName("Programming");

        sampleBook = new Book();
        sampleBook.setBookId(1L);
        sampleBook.setTitle("Clean Code");
        sampleBook.setDescription("A handbook of agile software craftsmanship.");
        sampleBook.setPrice(29.99);
        sampleBook.setAuthor(sampleAuthor);
        sampleBook.setCategory(sampleCategory);

        sampleRequest = new AdminBookDtoRequest();
        sampleRequest.setTitle("Clean Code");
        sampleRequest.setDescription("A handbook of agile software craftsmanship.");
        sampleRequest.setPrice(29.99);
        sampleRequest.setAuthorId(1L);
        sampleRequest.setCategoryId(1L);

        pageable = PageRequest.of(0, 12);
    }

    // ─── getAllBooks ──────────────────────────────────────────────────────────

    @Test
    void getAllBooks_shouldReturnPageOfBooks() {
        Page<Book> bookPage = new PageImpl<>(List.of(sampleBook), pageable, 1);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<AdminBookDtoResponse> result = adminBookService.getAllBooks(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Clean Code", result.getContent().get(0).getTitle());
        assertEquals("Robert C. Martin", result.getContent().get(0).getAuthorName());
        verify(bookRepository).findAll(pageable);
    }

    @Test
    void getAllBooks_shouldReturnEmptyPageWhenNoBooksExist() {
        when(bookRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));

        Page<AdminBookDtoResponse> result = adminBookService.getAllBooks(pageable);

        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getAllBooks_shouldMapAllFieldsCorrectly() {
        Page<Book> bookPage = new PageImpl<>(List.of(sampleBook));
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        AdminBookDtoResponse result = adminBookService.getAllBooks(pageable).getContent().get(0);

        assertAll(
                () -> assertEquals(1L, result.getBookId()),
                () -> assertEquals("Clean Code", result.getTitle()),
                () -> assertEquals("A handbook of agile software craftsmanship.", result.getDescription()),
                () -> assertEquals(29.99, result.getPrice()),
                () -> assertEquals("Robert C. Martin", result.getAuthorName()),
                () -> assertEquals("Programming", result.getCategoryName())
        );
    }

    // ─── getBookById ──────────────────────────────────────────────────────────

    @Test
    void getBookById_shouldReturnBookWhenIdExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        AdminBookDtoResponse result = adminBookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getBookId());
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert C. Martin", result.getAuthorName());
        assertEquals("Programming", result.getCategoryName());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_shouldThrowExceptionWhenBookNotFound() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminBookService.getBookById(999L));

        assertEquals("Book not found", ex.getMessage());
        verify(bookRepository).findById(999L);
    }

    // ─── createBook ───────────────────────────────────────────────────────────

    @Test
    void createBook_shouldSaveAndReturnBook() {
        when(bookRepository.existsByTitle("Clean Code")).thenReturn(false);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));
        when(bookRepository.save(any(Book.class))).thenReturn(sampleBook);

        AdminBookDtoResponse result = adminBookService.createBook(sampleRequest);

        assertNotNull(result);
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert C. Martin", result.getAuthorName());
        assertEquals("Programming", result.getCategoryName());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void createBook_shouldThrowExceptionWhenBookAlreadyExists() {
        when(bookRepository.existsByTitle("Clean Code")).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminBookService.createBook(sampleRequest));

        assertEquals("Book already exists", ex.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void createBook_shouldThrowExceptionWhenAuthorNotFound() {
        when(bookRepository.existsByTitle("Clean Code")).thenReturn(false);
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminBookService.createBook(sampleRequest));

        assertEquals("Author not found", ex.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void createBook_shouldThrowExceptionWhenCategoryNotFound() {
        when(bookRepository.existsByTitle("Clean Code")).thenReturn(false);
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminBookService.createBook(sampleRequest));

        assertEquals("Category not found", ex.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    // ─── updateBook ───────────────────────────────────────────────────────────

    @Test
    void updateBook_shouldUpdateAndReturnBook() {
        AdminBookDtoRequest updateRequest = new AdminBookDtoRequest();
        updateRequest.setTitle("Clean Code Updated");
        updateRequest.setDescription("Updated description.");
        updateRequest.setPrice(34.99);
        updateRequest.setAuthorId(1L);
        updateRequest.setCategoryId(1L);

        Book updatedBook = new Book();
        updatedBook.setBookId(1L);
        updatedBook.setTitle("Clean Code Updated");
        updatedBook.setDescription("Updated description.");
        updatedBook.setPrice(34.99);
        updatedBook.setAuthor(sampleAuthor);
        updatedBook.setCategory(sampleCategory);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(sampleCategory));
        when(bookRepository.save(any(Book.class))).thenReturn(updatedBook);

        AdminBookDtoResponse result = adminBookService.updateBook(1L, updateRequest);

        assertNotNull(result);
        assertEquals("Clean Code Updated", result.getTitle());
        assertEquals(34.99, result.getPrice());
        verify(bookRepository).save(any(Book.class));
    }

    @Test
    void updateBook_shouldThrowExceptionWhenBookNotFound() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminBookService.updateBook(999L, sampleRequest));

        assertEquals("Book Not Found", ex.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void updateBook_shouldThrowExceptionWhenAuthorNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminBookService.updateBook(1L, sampleRequest));

        assertEquals("Author not found", ex.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void updateBook_shouldThrowExceptionWhenCategoryNotFound() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));
        when(authorRepository.findById(1L)).thenReturn(Optional.of(sampleAuthor));
        when(categoryRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminBookService.updateBook(1L, sampleRequest));

        assertEquals("Category not found", ex.getMessage());
        verify(bookRepository, never()).save(any(Book.class));
    }

    // ─── deleteBook ───────────────────────────────────────────────────────────

    @Test
    void deleteBook_shouldDeleteBookSuccessfully() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(orderItemsRepository.existsByBook_BookId(1L)).thenReturn(false);

        adminBookService.deleteBook(1L);

        verify(bookRepository).deleteById(1L);
    }

    @Test
    void deleteBook_shouldThrowExceptionWhenBookNotFound() {
        when(bookRepository.existsById(999L)).thenReturn(false);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminBookService.deleteBook(999L));

        assertEquals("Book not found", ex.getMessage());
        verify(bookRepository, never()).deleteById(any());
    }

    @Test
    void deleteBook_shouldThrowExceptionWhenBookHasExistingOrders() {
        when(bookRepository.existsById(1L)).thenReturn(true);
        when(orderItemsRepository.existsByBook_BookId(1L)).thenReturn(true);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> adminBookService.deleteBook(1L));

        assertEquals("Cannot delete book with existing orders", ex.getMessage());
        verify(bookRepository, never()).deleteById(any());
    }
}
