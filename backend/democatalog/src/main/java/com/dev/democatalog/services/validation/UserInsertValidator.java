package com.dev.democatalog.services.validation;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.dev.democatalog.dto.UserInsertDTO;
import com.dev.democatalog.entities.User;
import com.dev.democatalog.repositories.UserRepository;
import com.dev.democatalog.resources.exceptions.FieldMessage;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class UserInsertValidator implements ConstraintValidator<UserInsertValid, UserInsertDTO>{

    @Autowired
    private UserRepository userRepository;

    @Override
	public void initialize(UserInsertValid ann) {
	}

    @Override
    public boolean isValid(UserInsertDTO dto, ConstraintValidatorContext context) {
        List<FieldMessage> list = new ArrayList<>();
		
		//Testes de validação, acrescentando objetos FieldMessage à lista
        User user = userRepository.findByEmail(dto.getEmail());
		if(user != null){
            list.add(new FieldMessage("email", "Email já existe!"));
        }

		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty();
    }
    
}
