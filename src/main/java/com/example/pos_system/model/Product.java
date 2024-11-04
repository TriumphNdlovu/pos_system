package com.example.pos_system.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private String name;
    private double price;
    private int quantity;
    private int product_id;
    private String product_size;
    private String barcode;

    public Product() {
    }

    public Product(String name, double price, int quantity, int productid, String product_size, String barcode) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.product_id = productid;
        this.product_size = product_size;
        this.barcode = barcode;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getProductid() {
        return product_id;
    }

    public void setProductid(int productid) {
        this.product_id = productid;
    }

    public String getProduct_size() {
        return product_size;
    }

    public void setProduct_size(String product_size) {
        this.product_size = product_size;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }
}