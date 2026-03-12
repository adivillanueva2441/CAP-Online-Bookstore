package com.example.online_bookstore.service;

import com.example.online_bookstore.dto.request.CartItemsDtoRequest;
import com.example.online_bookstore.dto.response.CartItemsDtoResponse;
import com.example.online_bookstore.model.Book;
import com.example.online_bookstore.model.Cart;
import com.example.online_bookstore.model.CartItems;
import com.example.online_bookstore.model.User;
import com.example.online_bookstore.repository.BookRepository;
import com.example.online_bookstore.repository.CartItemsRepository;
import com.example.online_bookstore.repository.CartRepository;
import com.example.online_bookstore.service.impl.CartItemsServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartItemsServiceTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private CartItemsRepository cartItemsRepository;

    @Mock
    private BookRepository bookRepository;

    @InjectMocks
    private CartItemsServiceImpl cartItemsService;

    private Cart createTestCart() {
        User user = new User();
        user.setUserId(1L);

        Cart cart = new Cart();
        cart.setCartId(1L);
        cart.setUser(user);

        return cart;
    }

    private Book createTestBook() {
        Book book = new Book();
        book.setBookId(1L);
        book.setTitle("Clean Code");
        book.setPrice(50.0);
        return book;
    }

    private CartItems createTestCartItem(Cart cart, Book book, int quantity) {
        CartItems item = new CartItems();
        item.setCartItemsId(1L);
        item.setCart(cart);
        item.setBook(book);
        item.setQuantity(quantity);
        return item;
    }

    @Test
    void getCartItems_shouldReturnDtoList() {
        Cart cart = createTestCart();
        Book book = createTestBook();
        CartItems cartItem = createTestCartItem(cart, book, 2);

        when(cartRepository.findByUser_UserId(1L)).thenReturn(cart);
        when(cartItemsRepository.findByCart(cart)).thenReturn(List.of(cartItem));

        List<CartItemsDtoResponse> result = cartItemsService.getCartItems(1L);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Clean Code", result.get(0).getTitle());
        assertEquals(2, result.get(0).getQuantity());
        assertEquals(50.0, result.get(0).getPrice());
    }

    @Test
    void getCartItems_shouldReturnEmptyListWhenCartEmpty() {
        Cart cart = createTestCart();
        when(cartRepository.findByUser_UserId(1L)).thenReturn(cart);
        when(cartItemsRepository.findByCart(cart)).thenReturn(List.of());

        List<CartItemsDtoResponse> result = cartItemsService.getCartItems(1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void addBookToCart_shouldAddNewBook() {
        Cart cart = createTestCart();
        Book book = createTestBook();
        CartItemsDtoRequest request = new CartItemsDtoRequest(1L, 3);

        when(cartRepository.findByUser_UserId(1L)).thenReturn(cart);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(null);

        CartItems savedItem = createTestCartItem(cart, book, 3);
        when(cartItemsRepository.save(any(CartItems.class))).thenReturn(savedItem);

        CartItems result = cartItemsService.addBookToCart(1L, request);

        assertNotNull(result);
        assertEquals(3, result.getQuantity());
        assertEquals("Clean Code", result.getBook().getTitle());
    }

    @Test
    void addBookToCart_shouldIncreaseQuantityIfExists() {
        Cart cart = createTestCart();
        Book book = createTestBook();
        CartItems existingItem = createTestCartItem(cart, book, 1);

        CartItemsDtoRequest request = new CartItemsDtoRequest(1L, 2);

        when(cartRepository.findByUser_UserId(1L)).thenReturn(cart);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(existingItem);
        when(cartItemsRepository.save(existingItem)).thenReturn(existingItem);

        CartItems result = cartItemsService.addBookToCart(1L, request);

        assertEquals(3, result.getQuantity()); // 1 + 2
    }

    @Test
    void addBookToCart_shouldThrowIfBookNotFound() {
        Cart cart = createTestCart();
        CartItemsDtoRequest request = new CartItemsDtoRequest(1L, 1);

        when(cartRepository.findByUser_UserId(1L)).thenReturn(cart);
        when(bookRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                cartItemsService.addBookToCart(1L, request)
        );
        assertEquals("Book not found", ex.getMessage());
    }

    @Test
    void updateCartItems_shouldUpdateQuantity() {
        Cart cart = createTestCart();
        Book book = createTestBook();
        CartItems cartItem = createTestCartItem(cart, book, 2);
        CartItemsDtoRequest request = new CartItemsDtoRequest(1L, 5);

        when(cartRepository.findByUser_UserId(1L)).thenReturn(cart);
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(cartItemsRepository.findByCartAndBook(cart, book)).thenReturn(cartItem);
        when(cartItemsRepository.save(cartItem)).thenReturn(cartItem);

        cartItemsService.updateCartItems(1L, request);

        assertEquals(5, cartItem.getQuantity());
        verify(cartItemsRepository).save(cartItem);
    }


    @Test
    void removeBookFromCart_shouldDeleteById() {
        Cart cart = createTestCart();
        Book book = createTestBook();
        CartItems cartItem = createTestCartItem(cart, book, 2);

        when(cartRepository.findByUser_UserId(1L)).thenReturn(cart);
        when(cartItemsRepository.findByCartAndBook_BookId(cart, 1L)).thenReturn(cartItem);

        cartItemsService.removeBookFromCart(1L, 1L);

        verify(cartItemsRepository).deleteById(cartItem.getCartItemsId());
    }

    @Test
    void removeBookFromCart_shouldThrowExceptionWhenCartItemNotFound() {
        Cart cart = createTestCart();

        when(cartRepository.findByUser_UserId(1L)).thenReturn(cart);
        when(cartItemsRepository.findByCartAndBook_BookId(cart, 1L)).thenReturn(null);

        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> cartItemsService.removeBookFromCart(1L, 1L));

        assertEquals("CartItem not found", ex.getMessage());
        verify(cartItemsRepository, never()).deleteById(any());
    }
}