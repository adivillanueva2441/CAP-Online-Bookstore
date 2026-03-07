package com.example.online_bookstore.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

    // Sets Primary/Foreign Keys and relationship with other tables
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    //Columns
    @NotBlank(message = "Username is required.")
    @Column(nullable = false, unique = true,length = 45, name = "username")
    private String username;

    @NotBlank(message = "Password is required.")
    @Column(nullable = false,length = 150, name = "password")
    private String password;

    @NotBlank(message = "Role is required.")
    @Column(nullable = false,length = 45, name = "role")
    private String role;

    @NotBlank(message = "First Name is required.")
    @Column(nullable = false, name = "first_name")
    private String firstName;

    @NotBlank(message = "Username is required.")
    @Column(nullable = false, name = "last_name")
    private String lastName;


    @OneToOne(mappedBy = "user")
    private Cart cart;

    @OneToMany(mappedBy = "user")
    private List<Order> orders;


    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
