package com.example.online_bookstore.service;

import com.example.online_bookstore.model.User;
import com.example.online_bookstore.repository.UserRepository;
import com.example.online_bookstore.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService; // concrete implementation

    @Test
    void findByUsername_shouldReturnUserIfExists() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(user));

        // Act
        User result = userService.findByUsername("testuser");

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository, times(1)).findByUsername("testuser");
    }

    @Test
    void findByUsername_shouldThrowExceptionIfUserNotFound() {
        // Arrange
        when(userRepository.findByUsername("missing")).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                userService.findByUsername("missing")
        );

        assertEquals("User not found", ex.getMessage());
        verify(userRepository, times(1)).findByUsername("missing");
    }
}