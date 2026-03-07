package com.example.online_bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "cart_items", uniqueConstraints = @UniqueConstraint(columnNames = {"cart_id", "book_id"}))
public class CartItems{

    // Sets Primary/Foreign Keys and relationship with other tables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemsId;

    //Columns
    @Min(value = 1, message = "Quantity should be a positive number.")
    @Column(nullable = false, name = "quantity")
    private int quantity;

    @Min(value = 1, message = "Total should be a positive number.")
    @Column(nullable = false, name = "total_price")
    private double totalPrice;


    @ManyToOne
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;


    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Long getCartItemsId() {
        return cartItemsId;
    }

    public void setCartItemsId(Long cartItemsId) {
        this.cartItemsId = cartItemsId;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}