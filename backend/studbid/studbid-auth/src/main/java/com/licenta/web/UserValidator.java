package com.licenta.web;

import com.licenta.service.UserService;
import com.licenta.service.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Component
public class UserValidator implements Validator {
    private final UserService userService;

    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if(userService.isExisting(userDTO))
            errors.rejectValue("email", "Exista deja un utilizator cu acest email!");
    }
}
