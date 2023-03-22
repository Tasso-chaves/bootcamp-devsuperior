package com.dev.democatalog.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.democatalog.dto.ProductDTO;
import com.dev.democatalog.entities.Product;
import com.dev.democatalog.repositories.ProductRepository;
import com.dev.democatalog.services.exceptions.DatabaseException;
import com.dev.democatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;;

@Service
public class ProductService {
    
    @Autowired
    private ProductRepository repository;

    @Transactional(readOnly = true)
    public Page<ProductDTO> findAllPaged(PageRequest pageRequest){
        
        Page<Product> list = repository.findAll(pageRequest);

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
        Optional<Product> obj = repository.findById(id);
        Product entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

        return new ProductDTO(entity, entity.getCategories());
    }

    @Transactional
    public ProductDTO insert(ProductDTO dto) {
        
        Product entity = new Product();
        //entity.setName(dto.getName());

        entity = repository.save(entity);

        return new ProductDTO(entity);
    }

    @Transactional
    public ProductDTO update(Long id, ProductDTO dto) {

        try {

            Product entity = repository.getReferenceById(id);
            //entity.setName(dto.getName());
            entity = repository.save(entity);
            return new ProductDTO(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id: " + id + " not found ");
        }
    }

    public void delete(Long id) {

        if(repository.existsById(id)){
            try {
                repository.deleteById(id);              
            } catch (DataIntegrityViolationException e) {
                throw new DatabaseException("Integrity Violation");
            }
        }else{
            throw new ResourceNotFoundException("Id: " + id + " not found ");
        }
        
    }
}
