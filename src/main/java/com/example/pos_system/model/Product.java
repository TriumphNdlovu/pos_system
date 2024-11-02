package com.example.pos_system.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long productId;
    private String productName;
    private double price;
    private int quantity;
    private String productSize;

    // Getters and Setters
    // Constructor

    public Product() {
    }

    public Product(Long productId, String productName, double price, int quantity, String productSize) {
        this.productId = productId;
        this.productName = productName;
        this.price = price;
        this.quantity = quantity;
        this.productSize = productSize;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public double getProductPrice() {
        return price;
    }

    public boolean isAvailable() {
        return this.quantity > 0;
    }
    
    public String getProductSize() {
        return productSize;
    }

    public boolean decreaseQuantity(int quantity) {
        if (this.quantity >= quantity) {
            this.quantity -= quantity;
            return true;
        }
        return false;
    }

    public void increaseQuantity(int quantity) {
        this.quantity += quantity;
    }

}