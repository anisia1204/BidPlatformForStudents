package com.licenta.web;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.Announcement;
import com.licenta.domain.AnnouncementStatus;
import com.licenta.service.AnnouncementService;
import com.licenta.service.UserService;
import com.licenta.service.dto.TransactionDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class TransactionValidator implements Validator {
    private final AnnouncementService announcementService;
    private final UserService userService;

    public TransactionValidator(AnnouncementService announcementService, UserService userService) {
        this.announcementService = announcementService;
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return TransactionDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        TransactionDTO transactionDTO = (TransactionDTO) target;
        Announcement announcement = announcementService.getById(transactionDTO.getAnnouncementId());
        if(announcement.getStatus() == AnnouncementStatus.SOLD){
            errors.rejectValue("announcementId", "Anuntul este vandut!");
        }
        if(userService.findById(UserContextHolder.getUserContext().getUserId()).getPoints() < announcement.getPoints()){
            errors.rejectValue("amount", "Fonduri insuficiente!");
        }
    }
}
