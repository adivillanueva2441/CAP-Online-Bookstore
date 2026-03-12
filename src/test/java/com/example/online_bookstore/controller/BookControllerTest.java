package com.example.online_bookstore.controller.rest;

import com.example.online_bookstore.dto.response.BookDtoResponse;
import com.example.online_bookstore.model.Author;
import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.model.Category;
import com.example.online_bookstore.service.IBookService;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private IBookService bookService;

    @InjectMocks
    private BookController bookController;

    private Book sampleBookEntity;
    private BookDtoResponse sampleBook;
    private Page<BookDtoResponse> samplePage;

    @BeforeEach
    void setUp() {
        Author author = new Author();
        author.setName("Robert C. Martin");

        Category category = new Category();
        category.setName("Programming");

        sampleBookEntity = new Book();
        sampleBookEntity.setBookId(1L);
        sampleBookEntity.setTitle("Clean Code");
        sampleBookEntity.setAuthor(author);
        sampleBookEntity.setCategory(category);
        sampleBookEntity.setDescription("A handbook of agile software craftsmanship.");
        sampleBookEntity.setPrice(29.99);

        sampleBook = new BookDtoResponse(sampleBookEntity);
        samplePage = new PageImpl<>(List.of(sampleBook), PageRequest.of(0, 12), 1);
    }

    // helper to reduce repetition inside tests
    private BookDtoResponse buildBookDto(Long id, String title, String authorName,
                                         String categoryName, double price) {
        Author author = new Author();
        author.setName(authorName);

        Category category = new Category();
        category.setName(categoryName);

        Book book = new Book();
        book.setBookId(id);
        book.setTitle(title);
        book.setAuthor(author);
        book.setCategory(category);
        book.setDescription("Some description.");
        book.setPrice(price);

        return new BookDtoResponse(book);
    }

    // ─── GET /api/books ───────────────────────────────────────────────────────

    @Test
    void getBooks_shouldReturnPageOfBooks() {
        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(samplePage);

        Page<BookDtoResponse> result = bookController.getBooks(0, 12);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Clean Code", result.getContent().get(0).getTitle());
        verify(bookService, times(1)).getAllBooks(PageRequest.of(0, 12));
    }

    @Test
    void getBooks_shouldReturnCorrectPageWhenCustomParamsProvided() {
        Page<BookDtoResponse> customPage = new PageImpl<>(List.of(sampleBook), PageRequest.of(1, 5), 6);
        when(bookService.getAllBooks(PageRequest.of(1, 5))).thenReturn(customPage);

        Page<BookDtoResponse> result = bookController.getBooks(1, 5);

        assertEquals(1, result.getNumber());
        assertEquals(5, result.getSize());
        verify(bookService).getAllBooks(PageRequest.of(1, 5));
    }

    @Test
    void getBooks_shouldReturnEmptyPageWhenNoBooksExist() {
        when(bookService.getAllBooks(any(Pageable.class))).thenReturn(new PageImpl<>(List.of()));

        Page<BookDtoResponse> result = bookController.getBooks(0, 12);

        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    // ─── GET /api/books/search ────────────────────────────────────────────────

    @Test
    void searchBooks_shouldReturnBooksByTitle() {
        when(bookService.findBooksByTitle(eq("Clean"), any(Pageable.class))).thenReturn(samplePage);

        Page<BookDtoResponse> result = bookController.searchBooks("Clean", 0, 12);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Clean Code", result.getContent().get(0).getTitle());
        verify(bookService).findBooksByTitle("Clean", PageRequest.of(0, 12));
    }

    @Test
    void searchBooks_shouldReturnEmptyPageWhenTitleNotFound() {
        when(bookService.findBooksByTitle(eq("xyz123"), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        Page<BookDtoResponse> result = bookController.searchBooks("xyz123", 0, 12);

        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void searchBooks_shouldReturnMatchingBooksWhenPartialTitleProvided() {
        when(bookService.findBooksByTitle(eq("Cod"), any(Pageable.class))).thenReturn(samplePage);

        Page<BookDtoResponse> result = bookController.searchBooks("Cod", 0, 12);

        assertFalse(result.getContent().isEmpty());
        assertTrue(result.getContent().get(0).getTitle().contains("Code"));
    }

    @Test
    void searchBooks_shouldReturnAllMatchesWhenMultipleBooksShareTitle() {
        BookDtoResponse secondBook = buildBookDto(2L, "Code Complete", "Steve McConnell", "Programming", 39.99);
        Page<BookDtoResponse> multiPage = new PageImpl<>(List.of(sampleBook, secondBook));
        when(bookService.findBooksByTitle(eq("Code"), any(Pageable.class))).thenReturn(multiPage);

        Page<BookDtoResponse> result = bookController.searchBooks("Code", 0, 12);

        assertEquals(2, result.getContent().size());
    }

    // ─── GET /api/books/category/{categoryId} ────────────────────────────────

    @Test
    void getBooksByCategory_shouldReturnBooksFilteredByCategory() {
        when(bookService.filterByCategory(eq(1L), any(Pageable.class))).thenReturn(samplePage);

        Page<BookDtoResponse> result = bookController.getBooksByCategory(1L, 0, 12);

        assertNotNull(result);
        assertEquals("Programming", result.getContent().get(0).getCategoryName());
        verify(bookService).filterByCategory(1L, PageRequest.of(0, 12));
    }

    @Test
    void getBooksByCategory_shouldReturnEmptyPageWhenCategoryHasNoBooks() {
        when(bookService.filterByCategory(eq(99L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of()));

        Page<BookDtoResponse> result = bookController.getBooksByCategory(99L, 0, 12);

        assertTrue(result.getContent().isEmpty());
    }

    @Test
    void getBooksByCategory_shouldReturnAllBooksMatchingCategory() {
        BookDtoResponse secondBook = buildBookDto(2L, "The Pragmatic Programmer", "Andrew Hunt", "Programming", 34.99);
        Page<BookDtoResponse> multiPage = new PageImpl<>(List.of(sampleBook, secondBook));
        when(bookService.filterByCategory(eq(1L), any(Pageable.class))).thenReturn(multiPage);

        Page<BookDtoResponse> result = bookController.getBooksByCategory(1L, 0, 12);

        assertEquals(2, result.getContent().size());
        result.getContent().forEach(book ->
                assertEquals("Programming", book.getCategoryName()));
    }

    // ─── GET /api/books/{bookId} ──────────────────────────────────────────────

    @Test
    void getBook_shouldReturnBookWhenIdExists() {
        when(bookService.getBookById(1L)).thenReturn(sampleBook);

        BookDtoResponse result = bookController.getBook(1L);

        assertNotNull(result);
        assertEquals(1L, result.getBookId());
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert C. Martin", result.getAuthorName());
        assertEquals("Programming", result.getCategoryName());
        assertEquals("A handbook of agile software craftsmanship.", result.getDescription());
        assertEquals(29.99, result.getPrice());
        verify(bookService).getBookById(1L);
    }

    @Test
    void getBook_shouldThrowExceptionWhenBookNotFound() {
        when(bookService.getBookById(999L)).thenThrow(new RuntimeException("Book not found"));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> bookController.getBook(999L));

        assertEquals("Book not found", ex.getMessage());
        verify(bookService).getBookById(999L);
    }

    @Test
    void getBook_shouldHaveNoNullFieldsWhenBookExists() {
        when(bookService.getBookById(1L)).thenReturn(sampleBook);

        BookDtoResponse result = bookController.getBook(1L);

        assertAll(
                () -> assertNotNull(result.getBookId()),
                () -> assertNotNull(result.getTitle()),
                () -> assertNotNull(result.getAuthorName()),
                () -> assertNotNull(result.getCategoryName()),
                () -> assertNotNull(result.getDescription())
        );
    }
}