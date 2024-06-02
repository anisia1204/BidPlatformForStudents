package com.licenta.web;

import com.licenta.domain.User;
import com.licenta.service.UserService;
import com.licenta.service.dto.UpdateUserPointsDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class WithdrawValidator implements Validator {
    private final UserService userService;

    public WithdrawValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UpdateUserPointsDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UpdateUserPointsDTO updateUserPointsDTO = (UpdateUserPointsDTO) target;
        User user = userService.findById(updateUserPointsDTO.getId());
        if(user.getPoints() < updateUserPointsDTO.getPointsToWithdraw()) {
            errors.rejectValue("pointsToWithdraw", "Fonduri insuficiente!");
        }
    }
}
