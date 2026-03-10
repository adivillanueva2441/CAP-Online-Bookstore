package com.example.online_bookstore.controller.auth;

import com.example.online_bookstore.model.User;
import com.example.online_bookstore.service.IUserRegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserRegistrationControllerTest {

    @Mock
    private IUserRegistrationService userRegistrationService;

    @Mock
    private Model model;

    @Mock
    private BindingResult bindingResult;

    @InjectMocks
    private UserRegistrationController registrationController;

    @Test
    void registerUser_shouldRegisterSuccessfully() {
        // Arrange
        User user = new User();
        user.setUsername("newUser");

        when(userRegistrationService.usernameExists("newUser")).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(false);

        // Act
        String view = registrationController.registerUser(user, bindingResult, model);

        // Assert
        assertEquals("auth/register", view);
        verify(userRegistrationService, times(1)).registerUser(user);
        verify(model, times(1)).addAttribute(eq("successMessage"), anyString());
        verify(model, times(1)).addAttribute(eq("user"), any(User.class));
    }

    @Test
    void registerUser_shouldReturnErrorWhenUsernameExists() {
        // Arrange
        User user = new User();
        user.setUsername("existingUser");

        when(userRegistrationService.usernameExists("existingUser")).thenReturn(true);
        when(bindingResult.hasErrors()).thenReturn(true); // simulate binding error after rejectValue

        // Act
        String view = registrationController.registerUser(user, bindingResult, model);

        // Assert
        assertEquals("auth/register", view);
        verify(userRegistrationService, never()).registerUser(any());
        verify(bindingResult, times(1))
                .rejectValue(eq("username"), eq("error.user"), eq("Username already exists"));
    }

    @Test
    void registerUser_shouldReturnFormIfBindingHasErrors() {
        // Arrange
        User user = new User();
        user.setUsername("anyUser");

        when(userRegistrationService.usernameExists("anyUser")).thenReturn(false);
        when(bindingResult.hasErrors()).thenReturn(true);

        // Act
        String view = registrationController.registerUser(user, bindingResult, model);

        // Assert
        assertEquals("auth/register", view);
        verify(userRegistrationService, never()).registerUser(any());
    }
}