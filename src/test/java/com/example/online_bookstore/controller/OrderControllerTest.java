package com.example.online_bookstore.controller;

import com.example.online_bookstore.controller.rest.OrderController;
import com.example.online_bookstore.dto.response.OrderDtoResponse;
import com.example.online_bookstore.dto.response.OrderItemsDtoResponse;
import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.model.Order;
import com.example.online_bookstore.model.OrderItems;
import com.example.online_bookstore.model.User;
import com.example.online_bookstore.service.IOrderService;
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
class OrderControllerTest {

    @Mock
    private IOrderService orderService;

    @Mock
    private IUserService userService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrderController orderController;

    private User createTestUser() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUser");
        return user;
    }

    private Order createTestOrder(User user) {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(100.0);

        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Clean Code");
        book.setPrice(50.0);

        OrderItems orderItem = new OrderItems();
        orderItem.setOrder(order);
        orderItem.setBook(book);
        orderItem.setQuantity(2);
        orderItem.setPrice(book.getPrice());
        order.getOrderItems().add(orderItem);

        return order;
    }

    @Test
    void getOrders_shouldReturnListOfOrders() {
        // Arrange
        User user = createTestUser();
        Order order = createTestOrder(user);
        OrderDtoResponse orderDto = new OrderDtoResponse(order);

        when(authentication.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(orderService.getOrdersByUser(user)).thenReturn(List.of(orderDto));

        // Act
        List<OrderDtoResponse> result = orderController.getOrders(authentication);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(order.getTotalPrice(), result.get(0).getTotalPrice());
        assertEquals(1, result.get(0).getOrderItems().size());
        assertEquals("Clean Code", result.get(0).getOrderItems().get(0).getTitle());

        verify(authentication, times(1)).getName();
        verify(userService, times(1)).findByUsername("testUser");
        verify(orderService, times(1)).getOrdersByUser(user);
    }

    @Test
    void getOrders_shouldReturnEmptyListWhenNoOrders() {
        // Arrange
        User user = createTestUser();

        when(authentication.getName()).thenReturn("testUser");
        when(userService.findByUsername("testUser")).thenReturn(user);
        when(orderService.getOrdersByUser(user)).thenReturn(List.of());

        // Act
        List<OrderDtoResponse> result = orderController.getOrders(authentication);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(authentication, times(1)).getName();
        verify(userService, times(1)).findByUsername("testUser");
        verify(orderService, times(1)).getOrdersByUser(user);
    }
}