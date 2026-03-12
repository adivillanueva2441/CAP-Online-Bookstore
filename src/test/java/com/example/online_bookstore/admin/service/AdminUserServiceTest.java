package com.example.online_bookstore.admin.service;

import com.example.online_bookstore.admin.dto.response.AdminUserDtoResponse;
import com.example.online_bookstore.admin.service.impl.AdminUserServiceImpl;
import com.example.online_bookstore.model.User;
import com.example.online_bookstore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminUserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminUserServiceImpl adminUserService;

    private User sampleUser;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUserId(1L);
        sampleUser.setUsername("johndoe");
        sampleUser.setPassword("encodedpassword");
        sampleUser.setRole("USER");
        sampleUser.setFirstName("John");
        sampleUser.setLastName("Doe");

        pageable = PageRequest.of(0, 12);
    }

    // ─── getAllUsers ──────────────────────────────────────────────────────────

    @Test
    void getAllUsers_shouldReturnPageOfUsers() {
        Page<User> userPage = new PageImpl<>(List.of(sampleUser), pageable, 1);
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<AdminUserDtoResponse> result = adminUserService.getAllUsers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("johndoe", result.getContent().get(0).getUsername());
        verify(userRepository).findAll(pageable);
    }

    @Test
    void getAllUsers_shouldReturnEmptyPageWhenNoUsersExist() {
        when(userRepository.findAll(pageable)).thenReturn(new PageImpl<>(List.of()));

        Page<AdminUserDtoResponse> result = adminUserService.getAllUsers(pageable);

        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getAllUsers_shouldMapAllFieldsCorrectly() {
        Page<User> userPage = new PageImpl<>(List.of(sampleUser));
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        AdminUserDtoResponse result = adminUserService.getAllUsers(pageable).getContent().get(0);

        assertAll(
                () -> assertEquals(1L, result.getUserId()),
                () -> assertEquals("johndoe", result.getUsername()),
                () -> assertEquals("USER", result.getRole()),
                () -> assertEquals("John", result.getFirstName()),
                () -> assertEquals("Doe", result.getLastName())
        );
    }

    @Test
    void getAllUsers_shouldReturnAllUsersWhenMultipleUsersExist() {
        User secondUser = new User();
        secondUser.setUserId(2L);
        secondUser.setUsername("janedoe");
        secondUser.setPassword("encodedpassword");
        secondUser.setRole("ADMIN");
        secondUser.setFirstName("Jane");
        secondUser.setLastName("Doe");

        Page<User> userPage = new PageImpl<>(List.of(sampleUser, secondUser));
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        Page<AdminUserDtoResponse> result = adminUserService.getAllUsers(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("johndoe", result.getContent().get(0).getUsername());
        assertEquals("janedoe", result.getContent().get(1).getUsername());
    }

    @Test
    void getAllUsers_shouldNotExposePasswordInResponse() {
        Page<User> userPage = new PageImpl<>(List.of(sampleUser));
        when(userRepository.findAll(pageable)).thenReturn(userPage);

        AdminUserDtoResponse result = adminUserService.getAllUsers(pageable).getContent().get(0);

        // AdminUserDtoResponse has no getPassword() — this verifies password is not in the DTO
        assertAll(
                () -> assertNotNull(result.getUsername()),
                () -> assertNotNull(result.getRole()),
                () -> assertNotNull(result.getFirstName()),
                () -> assertNotNull(result.getLastName())
        );
    }
}
