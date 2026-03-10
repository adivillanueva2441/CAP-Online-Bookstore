package com.example.online_bookstore.service.impl;

import com.example.online_bookstore.dto.response.BookDtoResponse;
import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.repository.BookRepository;
import com.example.online_bookstore.service.IBookService;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookServiceImpl implements IBookService {

    @Autowired
    private BookRepository bookRepository;

    @Override
    //Retrieve book list
    public List<BookDtoResponse> getAllBooks(){

        List<Book> listOfBooks = bookRepository.findAll();

        return listOfBooks.stream()
                .map(BookDtoResponse::new)
                .collect(Collectors.toList());
    }

    @Override
    //Search books by title
    public List<BookDtoResponse> findBooksByTitle(String title){
        List<Book> listOfBooks = bookRepository.findBooksByTitleContainingIgnoreCase(title);

        return listOfBooks.stream()
                .map(BookDtoResponse::new)
                .collect(Collectors.toList());

    }

    @Override
    public List<BookDtoResponse> filterByCategory(Long categoryId) {
        return bookRepository.findByCategory_CategoryId(categoryId)
                .stream()
                .map(BookDtoResponse::new)
                .toList();
    }

    @Override
    public BookDtoResponse getBookById(Long bookId) {
        Book listOfBooks = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));

        return getBookDto(listOfBooks);
    }

    @NonNull
    private BookDtoResponse getBookDto(Book book) {
        BookDtoResponse dto = new BookDtoResponse(book);
        dto.setBookId(book.getBookId());
        dto.setTitle(book.getTitle());
        dto.setAuthorName(book.getAuthor().getAuthorName());
        dto.setCategoryName(book.getCategory().getCategoryName());
        dto.setDescription(book.getDescription());
        dto.setPrice(book.getPrice());
        return dto;
    }


}
