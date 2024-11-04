package com.example.pos_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String productname;
    private double price;
    private int quantity;
    private int productid;

    // Getters and setters
    public int getId() {
        return productid;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return productname;
    }

    public void setName(String name) {
        this.productname = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }
}



