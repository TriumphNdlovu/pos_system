package com.example.pos_system.repository;

import com.example.pos_system.model.Product;

import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
  Product findByBarcode(String barcode);
  @Transactional
  @Modifying
  @Query("DELETE FROM Product p WHERE p.barcode = :barcode")
  void deleteByBarcode(@Param("barcode") String barcode);
}
