package com.licenta.web;

import com.licenta.domain.ConfirmationToken;
import com.licenta.domain.User;
import com.licenta.service.ConfirmationTokenService;
import com.licenta.service.UserService;
import com.licenta.service.dto.UserDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.time.LocalDateTime;

@Component
public class UserValidator implements Validator {
    private final UserService userService;
    private final ConfirmationTokenService confirmationTokenService;

    public UserValidator(UserService userService, ConfirmationTokenService confirmationTokenService) {
        this.userService = userService;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userDTO = (UserDTO) target;
        if(userService.isExisting(userDTO)){
            User user = userService.getUserByEmail(userDTO.getEmail());
            if(user.isEnabled()) {
                errors.reject("email", "Exista deja un utilizator cu acest email!");
            }
            else {
                ConfirmationToken confirmationToken = confirmationTokenService.getTokenByUserId(user.getId());
                LocalDateTime currentDateAndTime = LocalDateTime.now();
                if(!confirmationToken.getExpiresAt().isBefore(currentDateAndTime)) {
                    errors.reject("email", "Un email de confirmare a fost deja trimis catre tine! Confirma-ti adresa de email accesand link-ul din email-ul primit sau reincearca mai tarziu! ");
                }
            }
        }

    }
}
