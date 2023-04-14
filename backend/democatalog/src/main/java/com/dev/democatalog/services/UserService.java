package com.dev.democatalog.services;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.dev.democatalog.dto.RoleDTO;
import com.dev.democatalog.dto.UserDTO;
import com.dev.democatalog.dto.UserInsertDTO;
import com.dev.democatalog.entities.Role;
import com.dev.democatalog.entities.User;
import com.dev.democatalog.repositories.RoleRepository;
import com.dev.democatalog.repositories.UserRepository;
import com.dev.democatalog.services.exceptions.DatabaseException;
import com.dev.democatalog.services.exceptions.ResourceNotFoundException;

import jakarta.persistence.EntityNotFoundException;;

@Service
public class UserService {
    
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Transactional(readOnly = true)
    public Page<UserDTO> findAllPaged(Pageable pageable){
        
        Page<User> list = userRepository.findAll(pageable);

        //Converter lista User em uma Lista de UserDTO
        /*List<UserDTO> listDto = new ArrayList<>();
        for (User cat : list) {
            listDto.add(new UserDTO(cat));
        }*/

        //Alternativa resumida do método para a conversão
        return list.map(x -> new UserDTO(x));
    }

    @Transactional(readOnly = true)
    public UserDTO findById(Long id) {
        Optional<User> obj = userRepository.findById(id);
        User entity = obj.orElseThrow(() -> new ResourceNotFoundException("Entity not found"));

        return new UserDTO(entity);
    }

    @Transactional
    public UserDTO insert(UserInsertDTO dto) {
        
        User user = new User();
        copyDtoToEntity(dto, user);
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user = userRepository.save(user);

        return new UserDTO(user);
    }

    @Transactional
    public UserDTO update(Long id, UserDTO dto) {

        try {

            User user = userRepository.getReferenceById(id);
            copyDtoToEntity(dto, user);
            user = userRepository.save(user);
            return new UserDTO(user);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id: " + id + " not found ");
        }
    }

    public void delete(Long id) {

        if(userRepository.existsById(id)){
            try {
                userRepository.deleteById(id);              
            } catch (DataIntegrityViolationException e) {
                throw new DatabaseException("Integrity Violation");
            }
        }else{
            throw new ResourceNotFoundException("Id: " + id + " not found ");
        }  
    }

    private void copyDtoToEntity(UserDTO dto, User user) {

        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());

        user.getRoles().clear();

        for (RoleDTO roleDto : dto.getRoles()) {
            Role role = roleRepository.getReferenceById(roleDto.getId());

            user.getRoles().add(role);
        }
    }
}
