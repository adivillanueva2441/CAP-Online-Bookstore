package com.example.online_bookstore.service;


import com.example.online_bookstore.dto.response.BookDtoResponse;
import com.example.online_bookstore.model.Author;
import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.model.Category;
import com.example.online_bookstore.repository.BookRepository;
import com.example.online_bookstore.service.impl.BookServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {

    @InjectMocks
    BookServiceImpl bookService;

    @Mock
    BookRepository bookRepository;


    @Test
    public void getAllBooks_Success_Test(){

        Book book = new Book();

        List<Book> bookList = new ArrayList<>();
        bookList.add(book);

        Author author = new Author();
        author.setAuthorId(20L);
        author.setName("Author Name");
        author.setBooks(bookList);

        Category category = new Category();
        category.setName("Category Name");

        book.setTitle("Book 1");
        book.setBookId(1L);
        book.setDescription("Book 1");
        book.setPrice(123);
        book.setAuthor(author);
        book.setOrderItems(new ArrayList<>());
        book.setCategory(category);

        List<BookDtoResponse> bookDtoResponseList = new ArrayList<>();

        BookDtoResponse bookDtoResponse = new BookDtoResponse(book);
        bookDtoResponse.setBookId(1L);
        bookDtoResponse.setTitle("Book 1");
        bookDtoResponse.setAuthorName("Author Name");
        bookDtoResponse.setDescription("Book 1");
        bookDtoResponse.setPrice(123);
        bookDtoResponse.setCategoryName("Category Name");

        bookDtoResponseList.add(bookDtoResponse);


        when(bookRepository.findAll()).thenReturn(bookList);

        List<BookDtoResponse> response = bookService.getAllBooks(); // dito na papasok yung test sa actual Impl

        verify(bookRepository, times(1)).findAll();

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(bookDtoResponseList);

    }

    @Test
    public void getAllBooks_Empty_Test(){
        // Arrange
        when(bookRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<BookDtoResponse> result = bookService.getAllBooks();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void findBooksByTitle_Success_Test() {

        // Arrange
        Book book = new Book();
        book.setTitle("Spring Boot in Action");

        when(bookRepository.findBooksByTitleContainingIgnoreCase("Spring")).thenReturn(List.of(book));

        // Act
        List<BookDtoResponse> result = bookService.findBooksByTitle("Spring");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Spring Boot in Action", result.get(0).getTitle());

    }

    //Tests if no title returns an empty list of no matching titles were searched
    @Test
    void findBooksByTitle_Empty_Test() {

        // Arrange
        when(bookRepository.findBooksByTitleContainingIgnoreCase("Nonexistent"))
                .thenReturn(Collections.emptyList());

        // Act
        List<BookDtoResponse> result = bookService.findBooksByTitle("Nonexistent");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(bookRepository, times(1))
                .findBooksByTitleContainingIgnoreCase("Nonexistent");
    }

    @Test
    void filterByCategory_Success_Test() {

        // Arrange
        Category category = new Category();
        category.setCategoryId(1L);
        category.setName("Programming");

        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Clean Code");
        book.setCategory(category);

        when(bookRepository.findByCategory_CategoryId(1L))
                .thenReturn(List.of(book));

        // Act
        List<BookDtoResponse> result = bookService.filterByCategory(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
        assertEquals("Programming", result.get(0).getCategoryName());

        verify(bookRepository, times(1))
                .findByCategory_CategoryId(1L);
    }

    @Test
    void filterByCategory_Empty_Test() {

        // Arrange
        when(bookRepository.findByCategory_CategoryId(1L))
                .thenReturn(Collections.emptyList());

        // Act
        List<BookDtoResponse> result = bookService.filterByCategory(1L);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(bookRepository, times(1))
                .findByCategory_CategoryId(1L);
    }

    @Test
    void getBookById_returnBook_ifBookExists() {

        // Arrange
        Author author = new Author();
        author.setName("Robert Martin");

        Category category = new Category();
        category.setName("Programming");

        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Clean Code");
        book.setAuthor(author);
        book.setCategory(category);
        book.setDescription("A book about writing cleaner code");
        book.setPrice(45.0);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // Act
        BookDtoResponse result = bookService.getBookById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getBookId());
        assertEquals("Clean Code", result.getTitle());
        assertEquals("Robert Martin", result.getAuthorName());
        assertEquals("Programming", result.getCategoryName());

        verify(bookRepository, times(1)).findById(1L);

    }

    @Test
    void getBookById_shouldThrowException_whenBookDoesNotExist() {

        // Arrange
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> bookService.getBookById(1L)
        );

        assertEquals("Book not found", exception.getMessage());

        verify(bookRepository, times(1)).findById(1L);
    }


}
