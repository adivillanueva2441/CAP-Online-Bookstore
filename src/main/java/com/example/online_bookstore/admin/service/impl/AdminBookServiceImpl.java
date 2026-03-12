package com.example.online_bookstore.admin.service.impl;

import com.example.online_bookstore.admin.dto.request.AdminBookDtoRequest;
import com.example.online_bookstore.admin.dto.response.AdminBookDtoResponse;
import com.example.online_bookstore.admin.service.IAdminBookService;
import com.example.online_bookstore.model.Author;
import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.model.Category;
import com.example.online_bookstore.repository.AuthorRepository;
import com.example.online_bookstore.repository.BookRepository;
import com.example.online_bookstore.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AdminBookServiceImpl implements IAdminBookService {

    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public Page<AdminBookDtoResponse> getAllBooks(Pageable pageable){
        return bookRepository.findAll(pageable).map(AdminBookDtoResponse::new);
    }

    @Override
    public AdminBookDtoResponse getBookById(Long bookId) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book not found"));
        return new AdminBookDtoResponse(book);
    }

    @Override
    public AdminBookDtoResponse createBook(AdminBookDtoRequest adminBookDtoRequest) {
        if (bookRepository.existsByTitle(adminBookDtoRequest.getTitle())) {
            throw new RuntimeException("Book already exists");
        }
        Author author = authorRepository.findById(adminBookDtoRequest.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Category category = categoryRepository.findById(adminBookDtoRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        Book book = new Book();
        book.setTitle(adminBookDtoRequest.getTitle());
        book.setDescription(adminBookDtoRequest.getDescription());
        book.setPrice(adminBookDtoRequest.getPrice());
        book.setAuthor(author);
        book.setCategory(category);

        return new AdminBookDtoResponse(bookRepository.save(book));
    }

    @Override
    public AdminBookDtoResponse updateBook(Long bookId, AdminBookDtoRequest adminBookDtoRequest) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new RuntimeException("Book Not Found"));
        Author author = authorRepository.findById(adminBookDtoRequest.getAuthorId())
                .orElseThrow(() -> new RuntimeException("Author not found"));

        Category category = categoryRepository.findById(adminBookDtoRequest.getCategoryId())
                .orElseThrow(() -> new RuntimeException("Category not found"));

        book.setTitle(adminBookDtoRequest.getTitle());
        book.setDescription(adminBookDtoRequest.getDescription());
        book.setPrice(adminBookDtoRequest.getPrice());
        book.setAuthor(author);
        book.setCategory(category);

        return new AdminBookDtoResponse(bookRepository.save(book));
    }

    @Override
    public void deleteBook(Long bookId) {
        if (!bookRepository.existsById(bookId)) {
            throw new RuntimeException("Book not found");
        }
        bookRepository.deleteById(bookId);
    }
}
