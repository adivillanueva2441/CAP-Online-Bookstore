package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.BookDto;
import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    //Retrieve book list
    public List<BookDto> getAllBooks(){
        List<Book> books = bookRepository.findAll();

        return books.stream()
                .map(BookDto::new)
                .collect(Collectors.toList());
    }

    //Search books by title
    public List<BookDto> findBooksByTitle(String title){
        List<Book> listOfBooks = bookRepository.findBooksByTitleContainingIgnoreCase(title);

        return listOfBooks.stream()
                .map(BookDto::new)
                .collect(Collectors.toList());

    }




}
