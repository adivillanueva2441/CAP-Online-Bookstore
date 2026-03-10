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
    @DisplayName("getAllBooks_Success_Scenario")
    public void getAllBooks_Success_Test(){
        /*
            TODO STEP 1
            Initate and set test values == dito sineset ung mga dummy data
             and objects nagagamitin sa test

             e.g requestDtos, resonses, fields na kailangan
         */
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

        // YUNG MGA NASA TAAS NITONG COMMENT, ITO UNG MGA NEED NA DATA SA METHOD NA TINETEST
        List<BookDtoResponse> bookDtoResponseList = new ArrayList<>();

        // Expected Result -- ITO UNG RESPONSE NA GUSTO NATIN MA-CHECK KUNG TAMA BA YUNG BINATO NG METHOD MO
        BookDtoResponse bookDtoResponse = new BookDtoResponse(book);
        bookDtoResponse.setBookId(1L);
        bookDtoResponse.setTitle("Book 1");
        bookDtoResponse.setAuthorName("Author Name");
        bookDtoResponse.setDescription("Book 1");
        bookDtoResponse.setPrice(123);
        bookDtoResponse.setCategoryName("Category Name");

        bookDtoResponseList.add(bookDtoResponse);

        /*
            TODO STEP 2
            This where you Mock services, repositories and other configs
            e.g BookService, Book Repository etc
         */

        //Only use "when()" if may ineexpect na return
        when(bookRepository.findAll()).thenReturn(bookList);

        //kapag  "return type" is void ang gagamitin ito doNothing().when(bookRepository).saveAll(bookList);
//        doNothing().when(bookRepository).saveAll(bookList);

        //TODO Step 3
        // Call yung actual book service /service para ma-run yung
        // code (tapos papasa rin dito ung mga values na sinet sa step 1 kung kailangan)

        List<BookDtoResponse> response = bookService.getAllBooks(); // dito na papasok yung test sa actual Impl

        /*
            TODO STEP 4 (OPTIONAL)
            Verification == ensures na yung mga repositories, services
            or configs na minock mo ay natawag talaga or hindi natawag
         */
        verify(bookRepository, times(1)).findAll(); // scenario na natatawag ung mock
//        verify(bookRepository, never()).findAll(); // ito ay kapag hindi natawag ung mockedd service or repo or method

        /*
            TODO STEP 5 (Assertion)
             Dito kino-compare yung values na minock
             or nireturn sa Step 2 vs sa nireturn ni step 3
         */

        assertThat(response)
                .usingRecursiveComparison()
                .isEqualTo(bookDtoResponseList);

    }

    /**
     * tests the failed scenario -- null return/no data found
     */
    @DisplayName("getAllBooks_Empty_Return_Scenario")
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
    @DisplayName("findBooksByTitle_Success_Scenario")
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

    @Test
    @DisplayName("findBooksByTitle_Success_Scenario")
    void findBooksByTitle_shouldReturnEmptyList_whenNoMatches() {

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

}
