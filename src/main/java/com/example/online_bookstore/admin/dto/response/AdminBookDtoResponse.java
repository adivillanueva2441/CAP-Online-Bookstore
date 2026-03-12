package com.example.online_bookstore.admin.dto.response;

import com.example.online_bookstore.model.Book;

public class AdminBookDtoResponse {
    private Long bookId;
    private String title;
    private String description;
    private Double price;
    private String authorName;
    private String categoryName;

    public AdminBookDtoResponse(Book book) {
        this.bookId = book.getBookId();
        this.title = book.getTitle();
        this.description = book.getDescription();
        this.price = book.getPrice();
        this.authorName = book.getAuthor().getAuthorName();
        this.categoryName = book.getCategory().getCategoryName();
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
}
