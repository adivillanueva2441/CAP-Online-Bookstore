package com.example.online_bookstore.admin.dto.response;

import com.example.online_bookstore.model.Author;

public class AdminAuthorDtoResponse {
    private Long authorId;
    private String authorName;

    public AdminAuthorDtoResponse(Author author) {
        this.authorId = author.getAuthorId();
        this.authorName = author.getAuthorName();
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

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }
}
