package com.dev.democatalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.dev.democatalog.entities.Category;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
}
