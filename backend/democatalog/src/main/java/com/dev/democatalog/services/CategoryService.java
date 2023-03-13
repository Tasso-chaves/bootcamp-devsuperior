package com.dev.democatalog.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.democatalog.dto.CategoryDTO;
import com.dev.democatalog.entities.Category;
import com.dev.democatalog.repositories.CategoryRepository;
import com.dev.democatalog.services.exceptions.EntityNotFoundException;;

@Service
public class CategoryService {
    
    @Autowired
    private CategoryRepository repository;

    @Transactional(readOnly = true)
    public List<CategoryDTO> findAll(){
        
        List<Category> list =repository.findAll();

        //Converter lista Category em uma Lista de CategoryDTO
        /*List<CategoryDTO> listDto = new ArrayList<>();
        for (Category cat : list) {
            listDto.add(new CategoryDTO(cat));
        }*/

        //Alternativa resumida do método para a conversão
        return list.stream().map(x -> new CategoryDTO(x)).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public CategoryDTO findById(Long id) {
        Optional<Category> obj = repository.findById(id);
        Category entity = obj.orElseThrow(() -> new EntityNotFoundException("Entity not found"));

        return new CategoryDTO(entity);
    }
}
