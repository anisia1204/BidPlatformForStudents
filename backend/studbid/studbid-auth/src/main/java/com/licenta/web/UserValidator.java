package com.licenta.web;

import com.licenta.domain.User;
import com.licenta.service.UserService;
import com.licenta.service.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
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
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "field.required");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "field.required");
        UserDTO userDTO = (UserDTO) target;
        if(userService.isExisting(userDTO)){
            User user = userService.getUserByEmail(userDTO.getEmail());
            if(user.isEnabled()) {
                errors.rejectValue("email", "Exista deja un utilizator cu acest email!");
            }
            else {
                errors.rejectValue("email", "Un email de confirmare a fost deja trimis catre tine! Confirma-ti adresa de email accesand link-ul din email-ul primit!");
            }
        }

    }
}
