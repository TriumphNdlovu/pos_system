package com.example.pos_system.controller;

import com.example.pos_system.dto.SaleProductDTO;
import com.example.pos_system.model.Product;
import com.example.pos_system.model.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class SaleController {

    @Autowired
    private ProductRepository productRepository;

    @PostMapping("/sale")
    public ResponseEntity<String> processSale(@RequestBody List<SaleProductDTO> saleProducts) {
        for (SaleProductDTO saleProduct : saleProducts) {
            // Find product by barcode
            Product product = productRepository.findByBarcode(saleProduct.getBarcode());

            
            // Check if the product exists
            if (product == null) {
                return ResponseEntity.badRequest().body("Product with barcode " + saleProduct.getBarcode() + " not found.");
            }

            System.out.println("Product: " + product.getQuantity());
            System.out.println("SaleProduct: " + saleProduct.getQuantity());

            // Check if sufficient stock is available
            if (product.getQuantity() < saleProduct.getQuantity()) {
                return ResponseEntity.badRequest().body("Insufficient stock for product " + product.getName());
            }

            // Decrement the quantity
            int newQuantity = product.getQuantity() - saleProduct.getQuantity();

            System.out.println("New Quantity: " + newQuantity);
            product.setQuantity(newQuantity);  // Update the product's quantity

            // Save the updated product back to the database
            productRepository.save(product); // This will update the product in the database
        }

        return ResponseEntity.ok("Sale processed successfully.");
    }
}
