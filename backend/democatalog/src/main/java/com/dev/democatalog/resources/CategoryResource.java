package com.dev.democatalog.resources;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dev.democatalog.entities.Category;
import com.dev.democatalog.services.CategoryService;

@RestController
@RequestMapping(value = "/categories")
public class CategoryResource {
    
    @Autowired
    private CategoryService service;

    @GetMapping()
    public ResponseEntity<List<Category>> findAllCategory(){

        List<Category> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

}
