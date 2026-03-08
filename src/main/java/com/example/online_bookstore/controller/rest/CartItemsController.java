package com.example.online_bookstore.controller.rest;

import com.example.online_bookstore.dto.request.CartItemsDtoRequest;
import com.example.online_bookstore.dto.response.CartItemsDtoResponse;
import com.example.online_bookstore.model.CartItems;
import com.example.online_bookstore.model.User;
import com.example.online_bookstore.service.ICartItemsService;
import com.example.online_bookstore.service.IUserService;
import com.example.online_bookstore.service.impl.UserServiceImpl;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
public class CartItemsController {

    @Autowired
    private ICartItemsService cartItemsService;
    @Autowired
    private IUserService userService;


    @GetMapping
    public List<CartItemsDtoResponse> getCartItems(Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        return  cartItemsService.getCartItems(user.getUserId());
    }

    @PostMapping("/add")
    public CartItemsDtoResponse addBookToCart(@RequestBody CartItemsDtoRequest cartItemsDtoRequest,
                                                  Authentication authentication) {
        //Authenticate a user is logged in before adding to cart
        String username = authentication.getName();
        User user = getAuthenticatedUser(authentication);

        return getCartItemsDtoResponse(cartItemsDtoRequest, user);

    }

    @PutMapping("/update")
    public void updateCartItems(@RequestBody CartItemsDtoRequest cartItemsDtoRequest,
                                                Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        cartItemsService.updateCartItems(user.getUserId(), cartItemsDtoRequest);

    }

    @DeleteMapping("/remove/{bookId}")
    public void  deleteCartItems(@PathVariable Long bookId, Authentication authentication) {
        User user = getAuthenticatedUser(authentication);
        cartItemsService.removeBookFromCart(user.getUserId(), bookId);
    }

    @NonNull
    private CartItemsDtoResponse getCartItemsDtoResponse(CartItemsDtoRequest cartItemsDtoRequest, User user) {
        CartItems addedBookToCart = cartItemsService.addBookToCart(user.getUserId(), cartItemsDtoRequest);

        CartItemsDtoResponse cartItemsDtoResponse = new CartItemsDtoResponse();
        cartItemsDtoResponse.setCartItemId(addedBookToCart.getCartItemsId());
        cartItemsDtoResponse.setBookId(addedBookToCart.getBook().getBookId());
        cartItemsDtoResponse.setQuantity(addedBookToCart.getQuantity());

        return cartItemsDtoResponse;

    }

    private User getAuthenticatedUser(Authentication authentication) {
        return userService.findByUsername(authentication.getName());
    }
}
