package com.example.online_bookstore.controller;

import com.example.online_bookstore.controller.rest.BookController;
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
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @InjectMocks
    private BookController bookController;

    @Mock
    private IBookService bookService;

    @Test
    void getBooks_shouldReturnBookList() {
        // Arrange
        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Clean Code");
        Author author = new Author();
        author.setName("Robert Martin");
        book.setAuthor(author);
        Category category = new Category();
        category.setName("Programming");
        book.setCategory(category);

        BookDtoResponse dto = new BookDtoResponse(book);

        when(bookService.getAllBooks()).thenReturn(List.of(dto));

        // Act
        List<BookDtoResponse> result = bookController.getBooks();

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
        assertEquals("Robert Martin", result.get(0).getAuthorName());
        assertEquals("Programming", result.get(0).getCategoryName());

        verify(bookService, times(1)).getAllBooks();
    }

    @Test
    void searchBooks_shouldReturnBooksByTitle() {
        // Arrange
        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Spring Boot");
        Author author = new Author();
        author.setName("Robert Martin");
        book.setAuthor(author);
        Category category = new Category();
        category.setName("Programming");
        book.setCategory(category);

        BookDtoResponse dto = new BookDtoResponse(book);

        when(bookService.findBooksByTitle("Spring")).thenReturn(List.of(dto));

        // Act
        List<BookDtoResponse> result = bookController.searchBooks("Spring");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Spring Boot", result.get(0).getTitle());

        verify(bookService, times(1)).findBooksByTitle("Spring");
    }

    @Test
    void getBooksByCategory_shouldReturnBooks() {
        // Arrange
        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Clean Code");
        Category category = new Category();
        category.setName("Programming");
        book.setCategory(category);
        Author author = new Author();
        author.setName("Robert Martin");
        book.setAuthor(author);

        BookDtoResponse dto = new BookDtoResponse(book);

        when(bookService.filterByCategory(1L)).thenReturn(List.of(dto));

        // Act
        List<BookDtoResponse> result = bookController.getBooksByCategory(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Programming", result.get(0).getCategoryName());

        verify(bookService, times(1)).filterByCategory(1L);
    }

    @Test
    void getBook_shouldReturnBook() {
        // Arrange
        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Clean Code");
        Author author = new Author();
        author.setName("Robert Martin");
        book.setAuthor(author);
        Category category = new Category();
        category.setName("Programming");
        book.setCategory(category);

        BookDtoResponse dto = new BookDtoResponse(book);

        when(bookService.getBookById(1L)).thenReturn(dto);

        // Act
        BookDtoResponse result = bookController.getBook(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getBookId());
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert Martin", result.getAuthorName());
        assertEquals("Programming", result.getCategoryName());

        verify(bookService, times(1)).getBookById(1L);
    }

    @Test
    void getBook_shouldThrowException_whenBookNotFound() {
        // Arrange
        when(bookService.getBookById(1L)).thenThrow(new RuntimeException("Book not found"));

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            bookController.getBook(1L);
        });

        assertEquals("Book not found", exception.getMessage());

        verify(bookService, times(1)).getBookById(1L);
    }
}