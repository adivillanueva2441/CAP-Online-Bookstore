package com.example.online_bookstore.dto;
import com.example.online_bookstore.model.Book;

public class BookDto {
    private String title;
    private String authorName;
    private String categoryName;
    private String description;
    private double price;

    public BookDto(Book book) {
        this.title = book.getTitle();
        this.authorName = book.getAuthor() != null ? book.getAuthor().getAuthorName() : "Unknown";
        this.categoryName = book.getCategory() != null ? book.getCategory().getCategoryName() : "Unknown";
        this.description = book.getDescription();
        this.price = book.getPrice();
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getTitle() {
        return title;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }
}
