package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.response.BookDtoResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBookService {
    Page<BookDtoResponse> getAllBooks(Pageable pageable);
    Page<BookDtoResponse> findBooksByTitle(String title, Pageable pageable);
    Page<BookDtoResponse> filterByCategory(Long categoryId, Pageable pageable);
    BookDtoResponse getBookById(Long bookId);
}
