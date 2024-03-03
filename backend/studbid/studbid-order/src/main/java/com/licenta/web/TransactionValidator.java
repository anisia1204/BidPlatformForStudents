package com.licenta.web;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.Announcement;
import com.licenta.domain.AnnouncementStatus;
import com.licenta.domain.Project;
import com.licenta.domain.SkillStatus;
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
        if(userService.findById(UserContextHolder.getUserContext().getUserId()).getPoints() < announcement.getPoints()){
            errors.rejectValue("amount", "Fonduri insuficiente!");
        }
        if(announcement instanceof Project){
            long soldSkills = transactionDTO.getSkillIds()
                    .stream()
                    .map(skillService::getById)
                    .filter(skill -> skill.getStatus() == SkillStatus.SOLD)
                    .count();
            if(soldSkills > 0){
                errors.rejectValue("skillIds", "Abilitatea este vanduta!");
            }
        }
    }
}
