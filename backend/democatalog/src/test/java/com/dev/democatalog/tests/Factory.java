package com.dev.democatalog.tests;

import java.time.Instant;

import com.dev.democatalog.dto.ProductDTO;
import com.dev.democatalog.entities.Category;
import com.dev.democatalog.entities.Product;

public class Factory {
    
    public static Product createProduct(){

        Product product = new Product(
            1L, "Phone", 
            "Good Phone", 
            800.0, 
            "url:img", 
            Instant.parse("2020-07-14T10:00:00Z"));

        product.getCategories().add(new Category(2L, "Electronics"));
        return product;
    }

    public static ProductDTO createProductDTO(){

        Product product = createProduct();
        return new ProductDTO(product, product.getCategories());
    }
}
