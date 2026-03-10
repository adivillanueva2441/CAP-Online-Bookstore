package com.example.online_bookstore.service;

import com.example.online_bookstore.model.Cart;
import com.example.online_bookstore.model.User;
import com.example.online_bookstore.repository.UserRepository;
import com.example.online_bookstore.service.impl.UserRegistrationServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private ICartService cartService;

    @InjectMocks
    private UserRegistrationServiceImpl registrationService;

    @Test
    void registerUser_shouldSaveUserAndCreateCart() {
        // Arrange
        User user = new User();
        user.setUsername("newUser");
        user.setPassword("plainPass");

        when(userRepository.findByUsername("newUser")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainPass")).thenReturn("encodedPass");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        registrationService.registerUser(user);

        // Assert
        assertEquals("encodedPass", user.getPassword());
        assertEquals("USER", user.getRole());
        verify(userRepository, times(1)).save(user);
        verify(cartService, times(1)).createCart(any(Cart.class));
    }

    @Test
    void registerUser_shouldThrowExceptionIfUsernameExists() {
        // Arrange
        User user = new User();
        user.setUsername("existingUser");

        when(userRepository.findByUsername("existingUser")).thenReturn(Optional.of(new User()));

        // Act & Assert
        RuntimeException ex = assertThrows(RuntimeException.class, () -> registrationService.registerUser(user));
        assertEquals("Username is already in use", ex.getMessage());
        verify(userRepository, never()).save(any());
        verify(cartService, never()).createCart(any());
    }

    @Test
    void usernameExists_shouldReturnTrueIfUserExists() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.of(new User()));
        assertTrue(registrationService.usernameExists("test"));
    }

    @Test
    void usernameExists_shouldReturnFalseIfUserDoesNotExist() {
        when(userRepository.findByUsername("test")).thenReturn(Optional.empty());
        assertFalse(registrationService.usernameExists("test"));
    }
}