package com.example.online_bookstore.admin.service.impl;

import com.example.online_bookstore.admin.dto.response.AdminAuthorDtoResponse;
import com.example.online_bookstore.admin.service.IAdminAuthorService;
import com.example.online_bookstore.repository.AuthorRepository;
import com.example.online_bookstore.service.IBookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminAuthorServiceImpl implements IAdminAuthorService {
    @Autowired
    private AuthorRepository authorRepository;

    @Override
    public List<AdminAuthorDtoResponse> getAllAuthors() {
        return authorRepository.findAll()
                .stream()
                .map(AdminAuthorDtoResponse::new)
                .collect(Collectors.toList());
    }
}
