package com.example.online_bookstore.model;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "author")
public class Author {

    // Sets Primary/Foreign Keys and relationship with other tables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long authorId;

    @NotBlank(message = "Author name is required.")
    @Column(nullable = false, unique = true, length = 150 , name = "author_name")
    private String authorName;

    @OneToMany(mappedBy = "author")
    private List<Book> books;


    public List<Book> getBooks() {
        return books;
    }

    public void setBooks(List<Book> books) {
        this.books = books;
    }

    public Long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(Long authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setName(String authorName) {
        this.authorName = authorName;
    }
}
