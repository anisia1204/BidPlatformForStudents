package com.licenta.web;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.*;
import com.licenta.service.AnnouncementService;
import com.licenta.service.SkillService;
import com.licenta.service.UserService;
import com.licenta.service.dto.TransactionDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
@Component
public class TransactionValidator implements Validator {
    private final AnnouncementService announcementService;
    private final UserService userService;
    private final SkillService skillService;

    public TransactionValidator(AnnouncementService announcementService, UserService userService, SkillService skillService) {
        this.announcementService = announcementService;
        this.userService = userService;
        this.skillService = skillService;
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
        if(announcement.getStatus() == AnnouncementStatus.INACTIVE){
            errors.rejectValue("announcementId", "Anuntul este sters!");
        }
        if(announcement instanceof Project){
            if(isAnySkillSold(transactionDTO)){
                errors.rejectValue("skillIds", "Abilitatea este vanduta!");
            }
            if(isAnySkillDeleted(transactionDTO)){
                errors.rejectValue("skillIds", "Abilitatea este stearsa!");
            }
            Double userCreatorPoints = announcement.getUser().getPoints();
            if(isSkillPointsSumBiggerThanUserCreatorPoints(transactionDTO, userCreatorPoints)) {
                errors.rejectValue("skillIds", "Creatorul anuntului nu are fonduri suficiente pentru a finaliza tranzactia!");
            }
        }
        else {
            if(userService.findById(UserContextHolder.getUserContext().getUserId()).getPoints() < announcement.getPoints()){
                errors.rejectValue("amount", "Fonduri insuficiente!");
            }
        }
    }

    private boolean isAnySkillDeleted(TransactionDTO transactionDTO) {
        return transactionDTO.getSkillIds()
                .stream()
                .map(skillService::getById)
                .anyMatch(Skill::getDeleted);
    }

    private boolean isAnySkillSold(TransactionDTO transactionDTO) {
        return transactionDTO.getSkillIds()
                .stream()
                .map(skillService::getById)
                .anyMatch(skill -> skill.getStatus() == SkillStatus.SOLD);
    }

    private boolean isSkillPointsSumBiggerThanUserCreatorPoints(TransactionDTO transactionDTO, Double userCreatorPoints) {
        return transactionDTO.getSkillIds()
                .stream()
                .map(skillService::getById)
                .mapToDouble(Skill::getSkillPoints)
                .sum() > userCreatorPoints;
    }
}
