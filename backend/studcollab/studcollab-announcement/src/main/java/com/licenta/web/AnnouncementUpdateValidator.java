package com.licenta.web;

import com.licenta.domain.Announcement;
import com.licenta.domain.AnnouncementStatus;
import com.licenta.domain.SkillStatus;
import com.licenta.service.AnnouncementService;
import com.licenta.service.SkillService;
import com.licenta.service.dto.AnnouncementDTO;
import com.licenta.service.dto.ProjectDTO;
import com.licenta.service.dto.SkillDTO;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.util.List;

@Component
public class AnnouncementUpdateValidator implements Validator {
    private final AnnouncementService announcementService;
    private final SkillService skillService;


    public AnnouncementUpdateValidator(AnnouncementService announcementService, SkillService skillService) {
        this.announcementService = announcementService;
        this.skillService = skillService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return AnnouncementDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AnnouncementDTO announcementDto = (AnnouncementDTO) target;
        Announcement announcement = announcementService.getById(announcementDto.getId());
        if(announcement.getStatus() == AnnouncementStatus.SOLD) {
            errors.reject("status", "Anuntul a fost deja vandut!");
        }
        if(announcementDto instanceof ProjectDTO) {
            if (isAnySkillSold(((ProjectDTO) announcementDto).getRequiredSkills())) {
                errors.reject("status", "O abilitatea a fost deja vanduta!");
            }
        }
    }

    private boolean isAnySkillSold(List<SkillDTO> requiredSkills) {
        return requiredSkills.stream()
                .map(SkillDTO::getId)
                .map(skillService::getById)
                .anyMatch(skill -> skill.getStatus() == SkillStatus.SOLD);
    }
}