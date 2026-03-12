package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.response.BookDtoResponse;
import com.example.online_bookstore.model.Author;
import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.model.Category;
import com.example.online_bookstore.repository.BookRepository;
import com.example.online_bookstore.service.impl.BookServiceImpl;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    private Book sampleBook;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        Author author = new Author();
        author.setName("Robert C. Martin");  // uses setName()

        Category category = new Category();
        category.setCategoryName("Programming");     // uses setName()

        sampleBook = new Book();
        sampleBook.setBookId(1L);
        sampleBook.setTitle("Clean Code");
        sampleBook.setAuthor(author);
        sampleBook.setCategory(category);
        sampleBook.setDescription("A handbook of agile software craftsmanship.");
        sampleBook.setPrice(29.99);

        pageable = PageRequest.of(0, 12);
    }

    // ─── getAllBooks ──────────────────────────────────────────────────────────

    @Test
    void getAllBooks_shouldReturnPageOfBooks() {
        Page<Book> bookPage = new PageImpl<>(List.of(sampleBook), pageable, 1);
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        Page<BookDtoResponse> result = bookService.getAllBooks(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Clean Code", result.getContent().get(0).getTitle());
        assertEquals("Robert C. Martin", result.getContent().get(0).getAuthorName());
        verify(bookRepository).findAll(pageable);
    }

    @Test
    void getAllBooks_shouldReturnEmptyPageWhenNoBooksExist() {
        when(bookRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));

        Page<BookDtoResponse> result = bookService.getAllBooks(pageable);

        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getAllBooks_shouldMapAllFieldsCorrectly() {
        Page<Book> bookPage = new PageImpl<>(List.of(sampleBook));
        when(bookRepository.findAll(pageable)).thenReturn(bookPage);

        BookDtoResponse result = bookService.getAllBooks(pageable).getContent().get(0);

        assertAll(
                () -> assertEquals(1L, result.getBookId()),
                () -> assertEquals("Clean Code", result.getTitle()),
                () -> assertEquals("Robert C. Martin", result.getAuthorName()),
                () -> assertEquals("Programming", result.getCategoryName()),
                () -> assertEquals(29.99, result.getPrice())
        );
    }

    // ─── findBooksByTitle ─────────────────────────────────────────────────────

    @Test
    void findBooksByTitle_shouldReturnMatchingBooks() {
        Page<Book> bookPage = new PageImpl<>(List.of(sampleBook));
        when(bookRepository.findBooksByTitleContainingIgnoreCase("Clean", pageable)).thenReturn(bookPage);

        Page<BookDtoResponse> result = bookService.findBooksByTitle("Clean", pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Clean Code", result.getContent().get(0).getTitle());
        verify(bookRepository).findBooksByTitleContainingIgnoreCase("Clean", pageable);
    }

    @Test
    void findBooksByTitle_shouldReturnEmptyPageWhenNoMatchFound() {
        when(bookRepository.findBooksByTitleContainingIgnoreCase("xyz123", pageable))
                .thenReturn(new PageImpl<>(List.of()));

        Page<BookDtoResponse> result = bookService.findBooksByTitle("xyz123", pageable);

        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void findBooksByTitle_shouldReturnAllMatchesWhenMultipleBooksFound() {
        Book secondBook = new Book();
        secondBook.setBookId(2L);
        secondBook.setTitle("Code Complete");
        secondBook.setAuthor(sampleBook.getAuthor());
        secondBook.setCategory(sampleBook.getCategory());
        secondBook.setDescription("A comprehensive guide to software construction.");
        secondBook.setPrice(39.99);

        Page<Book> bookPage = new PageImpl<>(List.of(sampleBook, secondBook));
        when(bookRepository.findBooksByTitleContainingIgnoreCase("Code", pageable)).thenReturn(bookPage);

        Page<BookDtoResponse> result = bookService.findBooksByTitle("Code", pageable);

        assertEquals(2, result.getContent().size());
    }

    // ─── filterByCategory ────────────────────────────────────────────────────

    @Test
    void filterByCategory_shouldReturnBooksInCategory() {
        Page<Book> bookPage = new PageImpl<>(List.of(sampleBook));
        when(bookRepository.findByCategory_CategoryId(1L, pageable)).thenReturn(bookPage);

        Page<BookDtoResponse> result = bookService.filterByCategory(1L, pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Programming", result.getContent().get(0).getCategoryName());
        verify(bookRepository).findByCategory_CategoryId(1L, pageable);
    }

    @Test
    void filterByCategory_shouldReturnEmptyPageWhenCategoryHasNoBooks() {
        when(bookRepository.findByCategory_CategoryId(99L, pageable))
                .thenReturn(new PageImpl<>(List.of()));

        Page<BookDtoResponse> result = bookService.filterByCategory(99L, pageable);

        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void filterByCategory_shouldReturnAllBooksMatchingCategory() {
        Book secondBook = new Book();
        secondBook.setBookId(2L);
        secondBook.setTitle("The Pragmatic Programmer");
        secondBook.setAuthor(sampleBook.getAuthor());
        secondBook.setCategory(sampleBook.getCategory());
        secondBook.setDescription("Your journey to mastery.");
        secondBook.setPrice(34.99);

        Page<Book> bookPage = new PageImpl<>(List.of(sampleBook, secondBook));
        when(bookRepository.findByCategory_CategoryId(1L, pageable)).thenReturn(bookPage);

        Page<BookDtoResponse> result = bookService.filterByCategory(1L, pageable);

        assertEquals(2, result.getContent().size());
        result.getContent().forEach(book ->
                assertEquals("Programming", book.getCategoryName()));
    }

    // ─── getBookById ──────────────────────────────────────────────────────────

    @Test
    void getBookById_shouldReturnBookWhenIdExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        BookDtoResponse result = bookService.getBookById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getBookId());
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert C. Martin", result.getAuthorName());
        assertEquals("Programming", result.getCategoryName());
        assertEquals("A handbook of agile software craftsmanship.", result.getDescription());
        assertEquals(29.99, result.getPrice());
        verify(bookRepository).findById(1L);
    }

    @Test
    void getBookById_shouldThrowExceptionWhenBookNotFound() {
        when(bookRepository.findById(999L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookService.getBookById(999L));

        assertEquals("Book not found", ex.getMessage());
        verify(bookRepository).findById(999L);
    }

    @Test
    void getBookById_shouldHaveNoNullFieldsWhenBookExists() {
        when(bookRepository.findById(1L)).thenReturn(Optional.of(sampleBook));

        BookDtoResponse result = bookService.getBookById(1L);

        assertAll(
                () -> assertNotNull(result.getBookId()),
                () -> assertNotNull(result.getTitle()),
                () -> assertNotNull(result.getAuthorName()),
                () -> assertNotNull(result.getCategoryName()),
                () -> assertNotNull(result.getDescription())
        );
    }
}