package com.example.online_bookstore.repository;

import com.example.online_bookstore.model.Book;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    //Search book by matching keywords
    Page<Book> findBooksByTitleContainingIgnoreCase(String title, Pageable pageable);
    Page<Book> findByCategory_CategoryId(Long categoryId, Pageable pageable);
    boolean existsByTitle(String title);

    boolean existsByCategoryCategoryId(Long categoryId);
}
