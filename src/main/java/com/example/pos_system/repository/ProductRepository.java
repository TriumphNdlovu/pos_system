package com.example.pos_system.repository;

import com.example.pos_system.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  Product findByBarcode(String barcode);
}
