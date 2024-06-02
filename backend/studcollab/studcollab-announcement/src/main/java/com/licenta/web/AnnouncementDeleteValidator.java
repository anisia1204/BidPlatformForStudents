package com.licenta.web;

import com.licenta.domain.*;
import com.licenta.service.AnnouncementService;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class AnnouncementDeleteValidator implements Validator {
    private final AnnouncementService announcementService;

    public AnnouncementDeleteValidator(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return Long.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Long announcementId = (Long) target;
        Announcement announcement = announcementService.getById(announcementId);
        if(announcement.getStatus() == AnnouncementStatus.SOLD) {
            errors.reject("status", "Anuntul a fost deja vandut!");
        }
        if(announcement instanceof Project) {
            if (isAnySkillSold(((Project) announcement).getRequiredSkills())) {
                errors.reject("status", "O abilitatea a fost deja vanduta!");
            }
        }
    }

    private boolean isAnySkillSold(List<Skill> requiredSkills) {
        return requiredSkills.stream().anyMatch(skill -> skill.getStatus() == SkillStatus.SOLD);
    }
}
