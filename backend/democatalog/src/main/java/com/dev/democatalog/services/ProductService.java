package com.dev.democatalog.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.democatalog.dto.CategoryDTO;
import com.dev.democatalog.dto.ProductDTO;
import com.dev.democatalog.entities.Category;
import com.dev.democatalog.entities.Product;
import com.dev.democatalog.repositories.CategoryRepository;
import com.dev.democatalog.repositories.ProductRepository;
import com.dev.democatalog.services.exceptions.DatabaseException;
import com.dev.democatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(Pageable pageable){
        
        Page<Product> list = productRepository.findAll(pageable);

        //Converter lista Product em uma Lista de ProductDTO
        /*List<ProductDTO> listDto = new ArrayList<>();
        for (Product cat : list) {
            listDto.add(new ProductDTO(cat));
        }*/

        //Alternativa resumida do método para a conversão
        return list.map(x -> new ProductDTO(x));
    }

    @Transactional(readOnly = true)
    public ProductDTO findById(Long id) {
        Optional<Product> obj = productRepository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        
        Product entity = new Product();
        copyDtoToEntity(dto, entity);

        entity = productRepository.save(entity);

        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {

        try {

            Product entity = productRepository.getReferenceById(id);
            copyDtoToEntity(dto, entity);
            entity = productRepository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id: " + id + " not found ");
        }
    }

    public void delete(Long id) {

        if(productRepository.existsById(id)){
            try {
                productRepository.deleteById(id);              
            } catch (DataIntegrityViolationException e) {
                throw new DatabaseException("Integrity Violation");
            }
        }else{
            throw new ResourceNotFoundException("Id: " + id + " not found ");
        }  
    }

    private void copyDtoToEntity(ProductDTO dto, Product entity) {

        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();

        for (CategoryDTO catDto : dto.getCategories()) {
            Category category = categoryRepository.getReferenceById(catDto.getId());

            entity.getCategories().add(category);
        }
    }
}
