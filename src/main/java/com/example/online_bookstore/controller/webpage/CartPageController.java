package com.example.online_bookstore.controller.webpage;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CartPageController {

    @GetMapping("/cart")
    public String cartPage(){
        return "cart";
    }
}
