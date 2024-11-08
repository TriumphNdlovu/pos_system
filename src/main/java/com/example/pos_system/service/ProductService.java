package com.example.pos_system.service;

import com.example.pos_system.model.Product;
import com.example.pos_system.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public void deleteProduct(String barcode) {
        Product product = productRepository.findByBarcode(barcode);
        if (product != null) {
            productRepository.deleteByBarcode(barcode);
        } else {
            throw new RuntimeException("Product with barcode " + barcode + " not found");
        }
    }
}