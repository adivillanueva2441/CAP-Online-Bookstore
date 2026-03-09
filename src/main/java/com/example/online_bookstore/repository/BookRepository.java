package com.example.online_bookstore.repository;

import com.example.online_bookstore.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    //Search book by matching keywords
    List<Book> findBooksByTitleContainingIgnoreCase(String title);
    List<Book> findByCategory(String category);
}
