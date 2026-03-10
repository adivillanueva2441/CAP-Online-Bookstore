package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.response.BookDtoResponse;

import java.util.List;

public interface IBookService {
    List<BookDtoResponse> getAllBooks();
    List<BookDtoResponse> findBooksByTitle(String title);
    BookDtoResponse getBookById(Long bookId);
    List<BookDtoResponse> filterByCategory(Long categoryId);
}
