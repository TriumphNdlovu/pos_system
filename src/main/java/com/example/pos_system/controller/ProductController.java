package com.example.pos_system.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.pos_system.model.Product;
import com.example.pos_system.service.ProductService;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return product != null ? ResponseEntity.ok(product) : ResponseEntity.notFound().build();
    }

    @PostMapping
    public Product createProduct(@RequestBody Product product) {
        return productService.saveProduct(product);
    }

    @DeleteMapping("/{barcode}")
    public ResponseEntity<String> deleteProduct(@PathVariable String barcode) {
        try {
            productService.deleteProduct(barcode);
            return ResponseEntity.noContent().build(); // Returns HTTP 204 on success
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Failed to delete product: " + e.getMessage());
        }
    }
}
