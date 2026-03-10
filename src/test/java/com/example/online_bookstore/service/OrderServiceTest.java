package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.response.OrderDtoResponse;
import com.example.online_bookstore.dto.response.OrderItemsDtoResponse;
import com.example.online_bookstore.model.*;
import com.example.online_bookstore.repository.OrderRepository;
import com.example.online_bookstore.service.impl.OrderServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    private User createTestUser() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUser");
        return user;
    }

    private Book createTestBook(String title, double price) {
        Book book = new Book();
        book.setBookId(1L);
        book.setTitle(title);
        book.setPrice(price);
        return book;
    }

    private Order createTestOrder(User user) {
        Order order = new Order();
        order.setOrderId(1L);
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(100.0);

        Book book = createTestBook("Clean Code", 50.0);
        OrderItems orderItem = new OrderItems();
        orderItem.setOrder(order);
        orderItem.setBook(book);
        orderItem.setQuantity(2);
        orderItem.setPrice(book.getPrice());
        order.getOrderItems().add(orderItem);

        return order;
    }

    @Test
    void getOrdersByUser_shouldReturnListOfOrders() {
        // Arrange
        User user = createTestUser();
        Order order = createTestOrder(user);

        when(orderRepository.findByUserOrderByOrderDateDesc(user))
                .thenReturn(List.of(order));

        // Act
        List<OrderDtoResponse> result = orderService.getOrdersByUser(user);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        OrderDtoResponse dto = result.get(0);
        assertEquals(order.getTotalPrice(), dto.getTotalPrice());
        assertEquals(order.getOrderItems().size(), dto.getOrderItems().size());
        assertEquals("Clean Code", dto.getOrderItems().get(0).getTitle());
        assertEquals(2, dto.getOrderItems().get(0).getQuantity());
        assertEquals(50.0, dto.getOrderItems().get(0).getPrice());

        verify(orderRepository, times(1)).findByUserOrderByOrderDateDesc(user);
    }

    @Test
    void getOrdersByUser_shouldReturnEmptyListIfNoOrders() {
        // Arrange
        User user = createTestUser();
        when(orderRepository.findByUserOrderByOrderDateDesc(user)).thenReturn(List.of());

        // Act
        List<OrderDtoResponse> result = orderService.getOrdersByUser(user);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());

        verify(orderRepository, times(1)).findByUserOrderByOrderDateDesc(user);
    }
}