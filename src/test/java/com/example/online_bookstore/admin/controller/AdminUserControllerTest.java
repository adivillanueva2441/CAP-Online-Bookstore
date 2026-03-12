package com.example.online_bookstore.admin.controller;

import com.example.online_bookstore.admin.controller.rest.AdminUserController;
import com.example.online_bookstore.admin.dto.response.AdminUserDtoResponse;
import com.example.online_bookstore.admin.service.IAdminUserService;
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
class AdminUserControllerTest {

    @Mock
    private IAdminUserService adminUserService;

    @InjectMocks
    private AdminUserController adminUserController;

    private AdminUserDtoResponse sampleResponse;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        sampleResponse = new AdminUserDtoResponse(
                1L, "johndoe", "USER", "John", "Doe"
        );

        pageable = PageRequest.of(0, 12);
    }

    // ─── GET /api/admin/users ─────────────────────────────────────────────────

    @Test
    void getAllUsers_shouldReturnPageOfUsers() {
        Page<AdminUserDtoResponse> userPage = new PageImpl<>(List.of(sampleResponse), pageable, 1);
        when(adminUserService.getAllUsers(pageable)).thenReturn(userPage);

        Page<AdminUserDtoResponse> result = adminUserController.getAllUsers(pageable);

        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("johndoe", result.getContent().get(0).getUsername());
        verify(adminUserService).getAllUsers(pageable);
    }

    @Test
    void getAllUsers_shouldReturnEmptyPageWhenNoUsersExist() {
        when(adminUserService.getAllUsers(pageable)).thenReturn(new PageImpl<>(List.of()));

        Page<AdminUserDtoResponse> result = adminUserController.getAllUsers(pageable);

        assertTrue(result.getContent().isEmpty());
        assertEquals(0, result.getTotalElements());
    }

    @Test
    void getAllUsers_shouldMapAllFieldsCorrectly() {
        Page<AdminUserDtoResponse> userPage = new PageImpl<>(List.of(sampleResponse));
        when(adminUserService.getAllUsers(pageable)).thenReturn(userPage);

        AdminUserDtoResponse result = adminUserController.getAllUsers(pageable).getContent().get(0);

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
        AdminUserDtoResponse secondResponse = new AdminUserDtoResponse(
                2L, "janedoe", "ADMIN", "Jane", "Doe"
        );

        Page<AdminUserDtoResponse> userPage = new PageImpl<>(List.of(sampleResponse, secondResponse));
        when(adminUserService.getAllUsers(pageable)).thenReturn(userPage);

        Page<AdminUserDtoResponse> result = adminUserController.getAllUsers(pageable);

        assertEquals(2, result.getContent().size());
        assertEquals("johndoe", result.getContent().get(0).getUsername());
        assertEquals("janedoe", result.getContent().get(1).getUsername());
    }

    @Test
    void getAllUsers_shouldReturnCorrectPageWhenCustomParamsProvided() {
        Pageable customPageable = PageRequest.of(1, 5);
        Page<AdminUserDtoResponse> userPage = new PageImpl<>(List.of(sampleResponse), customPageable, 6);
        when(adminUserService.getAllUsers(customPageable)).thenReturn(userPage);

        Page<AdminUserDtoResponse> result = adminUserController.getAllUsers(customPageable);

        assertEquals(1, result.getNumber());
        assertEquals(5, result.getSize());
        verify(adminUserService).getAllUsers(customPageable);
    }
}
