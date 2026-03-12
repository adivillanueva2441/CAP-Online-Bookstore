package com.example.online_bookstore.service;

import com.example.online_bookstore.model.Cart;
import com.example.online_bookstore.model.User;
import com.example.online_bookstore.repository.UserRepository;
import com.example.online_bookstore.service.impl.UserRegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
    private UserRegistrationServiceImpl userRegistrationService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUsername("johndoe");
        sampleUser.setPassword("plainpassword");
        sampleUser.setRole("USER");
        sampleUser.setFirstName("John");
        sampleUser.setLastName("Doe");
    }

    // ─── registerUser ─────────────────────────────────────────────────────────

    @Test
    void registerUser_shouldSaveUserSuccessfully() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainpassword")).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        userRegistrationService.registerUser(sampleUser);

        verify(userRepository).save(any(User.class));
        verify(cartService).createCart(any(Cart.class));
    }

    @Test
    void registerUser_shouldEncodePasswordBeforeSaving() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());
        when(passwordEncoder.encode("plainpassword")).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        userRegistrationService.registerUser(sampleUser);

        verify(passwordEncoder).encode("plainpassword");
        assertEquals("encodedpassword", sampleUser.getPassword());
    }

    @Test
    void registerUser_shouldThrowExceptionWhenUsernameAlreadyExists() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(sampleUser));

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> userRegistrationService.registerUser(sampleUser));

        assertEquals("Username is already in use", ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
        verify(cartService, never()).createCart(any(Cart.class));
    }

    @Test
    void registerUser_shouldSetDefaultRoleWhenRoleIsNull() {
        sampleUser.setRole(null);

        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        userRegistrationService.registerUser(sampleUser);

        assertEquals("USER", sampleUser.getRole());
    }

    @Test
    void registerUser_shouldSetDefaultRoleWhenRoleIsEmpty() {
        sampleUser.setRole("");

        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        userRegistrationService.registerUser(sampleUser);

        assertEquals("USER", sampleUser.getRole());
    }

    @Test
    void registerUser_shouldCreateCartAfterSavingUser() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("encodedpassword");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);

        userRegistrationService.registerUser(sampleUser);

        verify(cartService, times(1)).createCart(any(Cart.class));
    }

    // ─── usernameExists ───────────────────────────────────────────────────────

    @Test
    void usernameExists_shouldReturnTrueWhenUsernameIsTaken() {
        when(userRepository.findByUsername("johndoe")).thenReturn(Optional.of(sampleUser));

        boolean result = userRegistrationService.usernameExists("johndoe");

        assertTrue(result);
    }

    @Test
    void usernameExists_shouldReturnFalseWhenUsernameIsAvailable() {
        when(userRepository.findByUsername("newuser")).thenReturn(Optional.empty());

        boolean result = userRegistrationService.usernameExists("newuser");

        assertFalse(result);
    }
}