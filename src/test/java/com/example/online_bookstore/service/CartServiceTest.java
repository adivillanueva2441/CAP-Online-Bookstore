package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.response.OrderDtoResponse;
import com.example.online_bookstore.model.*;
import com.example.online_bookstore.repository.CartItemsRepository;
import com.example.online_bookstore.repository.CartRepository;
import com.example.online_bookstore.repository.OrderItemsRepository;
import com.example.online_bookstore.repository.OrderRepository;
import com.example.online_bookstore.service.impl.CartServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemsRepository cartItemsRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemsRepository orderItemsRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private User createTestUser() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUser");
        return user;
    }

    private Book createTestBook(Long id, String title, double price) {
        Book book = new Book();
        book.setBookId(id);
        book.setTitle(title);
        book.setPrice(price);
        return book;
    }

    private CartItems createCartItem(Cart cart, Book book, int quantity) {
        CartItems item = new CartItems();
        item.setCartItemsId((long) book.getBookId());
        item.setCart(cart);
        item.setBook(book);
        item.setQuantity(quantity);
        return item;
    }

    private Cart createCart(User user) {
        Cart cart = new Cart();
        cart.setCartId(1L);
        cart.setUser(user);
        return cart;
    }

    @Test
    void orderCheckout_shouldReturnOrderDto() {
        // Arrange
        User user = createTestUser();
        Cart cart = createCart(user);

        Book book1 = createTestBook(1L, "Clean Code", 50.0);
        Book book2 = createTestBook(2L, "Spring Boot", 70.0);

        CartItems item1 = createCartItem(cart, book1, 2); // subtotal = 100
        CartItems item2 = createCartItem(cart, book2, 1); // subtotal = 70

        List<CartItems> cartItems = List.of(item1, item2);

        when(cartRepository.findByUser_UserId(user.getUserId())).thenReturn(cart);
        when(cartItemsRepository.findByCart(cart)).thenReturn(cartItems);

        // Act
        OrderDtoResponse orderDto = cartService.orderCheckout(user.getUserId());

        // Assert
        assertNotNull(orderDto);
        assertEquals(170.0, orderDto.getTotalPrice()); // 100 + 70
        assertEquals(2, orderDto.getOrderItems().size());

        assertEquals("Clean Code", orderDto.getOrderItems().get(0).getTitle());
        assertEquals(2, orderDto.getOrderItems().get(0).getQuantity());
        assertEquals(50.0, orderDto.getOrderItems().get(0).getPrice());
        assertEquals(100.0, orderDto.getOrderItems().get(0).getSubTotal());

        assertEquals("Spring Boot", orderDto.getOrderItems().get(1).getTitle());
        assertEquals(1, orderDto.getOrderItems().get(1).getQuantity());
        assertEquals(70.0, orderDto.getOrderItems().get(1).getPrice());
        assertEquals(70.0, orderDto.getOrderItems().get(1).getSubTotal());

        verify(orderItemsRepository, times(2)).save(any());
        verify(orderRepository, times(1)).save(any());
        verify(cartItemsRepository, times(1)).deleteAll(cartItems);
    }

    @Test
    void orderCheckout_shouldThrowIfCartEmpty() {
        // Arrange
        User user = createTestUser();
        Cart cart = createCart(user);

        when(cartRepository.findByUser_UserId(user.getUserId())).thenReturn(cart);
        when(cartItemsRepository.findByCart(cart)).thenReturn(new ArrayList<>()); // empty cart

        // Act & Assert
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                () -> cartService.orderCheckout(user.getUserId()));

        assertEquals("Cart is empty", ex.getMessage());
    }

}