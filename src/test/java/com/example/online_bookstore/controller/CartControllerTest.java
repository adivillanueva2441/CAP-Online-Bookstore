package com.example.online_bookstore.controller;

import com.example.online_bookstore.controller.rest.CartController;
import com.example.online_bookstore.dto.response.OrderDtoResponse;
import com.example.online_bookstore.dto.response.OrderItemsDtoResponse;
import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.model.Order;
import com.example.online_bookstore.model.OrderItems;
import com.example.online_bookstore.model.User;
import com.example.online_bookstore.service.ICartService;
import com.example.online_bookstore.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private ICartService cartService;

    @Mock
    private IUserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CartController cartController;

    @Test
    void checkout_shouldReturnOrderDto() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUser");

        when(authentication.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);

        OrderItemsDtoResponse orderItem = new OrderItemsDtoResponse(
                // create a mock OrderItems object or just stub the fields
                new OrderItems() {{
                    setQuantity(2);
                    setPrice(50.0);
                    setBook(new Book() {{
                        setTitle("Clean Code");
                        setPrice(50.0);
                    }});
                }}
        );

        OrderDtoResponse orderDto = new OrderDtoResponse(
                new Order() {{
                    setOrderDate(LocalDateTime.now());
                    setTotalPrice(100.0);
                    setOrderItems(List.of(
                            new OrderItems() {{
                                setBook(new Book() {{
                                    setTitle("Clean Code");
                                    setPrice(50.0);
                                }});
                                setQuantity(2);
                                setPrice(50.0);
                            }}
                    ));
                }}
        );

        when(cartService.orderCheckout(1L)).thenReturn(orderDto);

        // Act
        OrderDtoResponse result = cartController.checkout(authentication);

        // Assert
        assertNotNull(result);
        assertEquals(100.0, result.getTotalPrice());
        assertEquals(1, result.getOrderItems().size());
        assertEquals("Clean Code", result.getOrderItems().get(0).getTitle());

        verify(userService, times(1)).findByUsername("testUser");
        verify(cartService, times(1)).orderCheckout(1L);
    }

    @Test
    void checkout_shouldPropagateExceptionFromService() {
        // Arrange
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUser");

        when(authentication.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(cartService.orderCheckout(1L)).thenThrow(new IllegalStateException("Cart is empty"));

        // Act & Assert
        IllegalStateException exception = assertThrows(IllegalStateException.class, () ->
                cartController.checkout(authentication)
        );

        assertEquals("Cart is empty", exception.getMessage());

        verify(userService, times(1)).findByUsername("testUser");
        verify(cartService, times(1)).orderCheckout(1L);
    }
}