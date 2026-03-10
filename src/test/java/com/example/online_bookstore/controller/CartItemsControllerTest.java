package com.example.online_bookstore.controller;

import com.example.online_bookstore.controller.rest.CartItemsController;
import com.example.online_bookstore.dto.request.CartItemsDtoRequest;
import com.example.online_bookstore.dto.response.CartItemsDtoResponse;
import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.model.CartItems;
import com.example.online_bookstore.model.User;
import com.example.online_bookstore.service.ICartItemsService;
import com.example.online_bookstore.service.IUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemsControllerTest {

    @Mock
    private ICartItemsService cartItemsService;

    @Mock
    private IUserService userService;

    @InjectMocks
    private CartItemsController cartItemsController;

    @Mock
    private Authentication authentication;

    private User createTestUser() {
        User user = new User();
        user.setUserId(1L);
        user.setUsername("testUser");
        return user;
    }

    private Book createTestBook() {
        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Clean Code");
        book.setPrice(50.0);
        return book;
    }

    private CartItems createTestCartItem(User user, Book book, int quantity) {
        CartItems item = new CartItems();
        item.setCartItemsId(1L);
        item.setBook(book);
        item.setQuantity(quantity);
        return item;
    }


    @Test
    void getCartItems_shouldReturnCartItemsList() {
        User user = createTestUser();
        when(authentication.getName()).thenReturn(user.getUsername());
        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        Book book = createTestBook();
        CartItems cartItem = createTestCartItem(user, book, 2);
        CartItemsDtoResponse dto = new CartItemsDtoResponse(cartItem);

        when(cartItemsService.getCartItems(user.getUserId()))
                .thenReturn(List.of(dto));

        List<CartItemsDtoResponse> result = cartItemsController.getCartItems(authentication);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
        assertEquals(2, result.get(0).getQuantity());

        verify(userService, times(1)).findByUsername(user.getUsername());
        verify(cartItemsService, times(1)).getCartItems(user.getUserId());
    }

    @Test
    void addBookToCart_shouldReturnAddedCartItem() {
        User user = createTestUser();
        when(authentication.getName()).thenReturn(user.getUsername());
        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        Book book = createTestBook();
        CartItems cartItem = createTestCartItem(user, book, 3);
        CartItemsDtoRequest request = new CartItemsDtoRequest(book.getBookId(), 3);

        when(cartItemsService.addBookToCart(user.getUserId(), request))
                .thenReturn(cartItem);

        CartItemsDtoResponse response = cartItemsController.addBookToCart(request, authentication);

        assertNotNull(response);
        assertEquals("Clean Code", response.getTitle());
        assertEquals(3, response.getQuantity());

        verify(userService, times(1)).findByUsername(user.getUsername());
        verify(cartItemsService, times(1)).addBookToCart(user.getUserId(), request);
    }

    @Test
    void updateCartItems_shouldCallService() {
        User user = createTestUser();
        when(authentication.getName()).thenReturn(user.getUsername());
        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        CartItemsDtoRequest request = new CartItemsDtoRequest(1L, 5);

        cartItemsController.updateCartItems(request, authentication);

        verify(userService, times(1)).findByUsername(user.getUsername());
        verify(cartItemsService, times(1)).updateCartItems(user.getUserId(), request);
    }

    @Test
    void deleteCartItems_shouldCallService() {
        User user = createTestUser();
        when(authentication.getName()).thenReturn(user.getUsername());
        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        cartItemsController.deleteCartItems(1L, authentication);

        verify(userService, times(1)).findByUsername(user.getUsername());
        verify(cartItemsService, times(1)).removeBookFromCart(user.getUserId(), 1L);
    }

    @Test
    void getCartItems_shouldThrowIfUserNotFound() {
        when(authentication.getName()).thenReturn("nonexistent");
        when(userService.findByUsername("nonexistent"))
                .thenThrow(new RuntimeException("User not found"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            cartItemsController.getCartItems(authentication);
        });

        assertEquals("User not found", ex.getMessage());
    }

    @Test
    void addBookToCart_shouldThrowIfBookNotFound() {
        User user = createTestUser();
        when(authentication.getName()).thenReturn(user.getUsername());
        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        CartItemsDtoRequest request = new CartItemsDtoRequest(999L, 2);

        when(cartItemsService.addBookToCart(user.getUserId(), request))
                .thenThrow(new RuntimeException("Book not found"));

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            cartItemsController.addBookToCart(request, authentication);
        });

        assertEquals("Book not found", ex.getMessage());
    }

    @Test
    void updateCartItems_shouldThrowIfBookNotFound() {
        User user = createTestUser();
        when(authentication.getName()).thenReturn(user.getUsername());
        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        CartItemsDtoRequest request = new CartItemsDtoRequest(999L, 5);

        doThrow(new RuntimeException("Book not found"))
                .when(cartItemsService).updateCartItems(user.getUserId(), request);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            cartItemsController.updateCartItems(request, authentication);
        });

        assertEquals("Book not found", ex.getMessage());
    }

    @Test
    void deleteCartItems_shouldThrowIfBookNotFound() {
        User user = createTestUser();
        when(authentication.getName()).thenReturn(user.getUsername());
        when(userService.findByUsername(user.getUsername())).thenReturn(user);

        doThrow(new RuntimeException("Cart item not found"))
                .when(cartItemsService).removeBookFromCart(user.getUserId(), 999L);

        RuntimeException ex = assertThrows(RuntimeException.class, () -> {
            cartItemsController.deleteCartItems(999L, authentication);
        });

        assertEquals("Cart item not found", ex.getMessage());
    }

}