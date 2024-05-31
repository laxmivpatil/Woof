package com.example.techverse.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.techverse.Model.Product;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByProductNameContainingIgnoreCase(String productName);
    
    List<Product> findByPetCategoryIgnoreCase(String petCategory);
    
    List<Product> findByProductCategoryIgnoreCase(String productCategory);
}

