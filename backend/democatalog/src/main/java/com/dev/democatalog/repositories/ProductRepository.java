package com.dev.democatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.democatalog.entities.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
}
