package com.dev.democatalog.resources;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.democatalog.entities.Category;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
    
    @GetMapping()
    public ResponseEntity<List<Category>> findAllCategory(){

        List<Category> list = new ArrayList<>();
        list.add(new Category(1L, "Books"));
        list.add(new Category(2L, "Electronics"));
        list.add(new Category(3L, "Shirts"));

        return ResponseEntity.ok().body(list);
    }

}
