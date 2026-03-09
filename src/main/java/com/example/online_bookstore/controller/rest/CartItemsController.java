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
        User user = userService.findByUsername(authentication.getName());
        return  cartItemsService.getCartItems(user.getUserId());
    }

    @PostMapping("/add")
    public CartItemsDtoResponse addBookToCart(@RequestBody CartItemsDtoRequest cartItemsDtoRequest,
                                                  Authentication authentication) {
        //Authenticate a user is logged in before adding to cart
        User user = userService.findByUsername(authentication.getName());

        CartItems cartItems = cartItemsService.addBookToCart(user.getUserId(), cartItemsDtoRequest);

        return new CartItemsDtoResponse(cartItems);

    }

    @PutMapping("/update")
    public void updateCartItems(@RequestBody CartItemsDtoRequest cartItemsDtoRequest,
                                                Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        cartItemsService.updateCartItems(user.getUserId(), cartItemsDtoRequest);

    }

    @DeleteMapping("/remove/{bookId}")
    public void deleteCartItems(@PathVariable Long bookId, Authentication authentication) {
        User user = userService.findByUsername(authentication.getName());
        cartItemsService.removeBookFromCart(user.getUserId(), bookId);
    }

}
