package com.licenta.service.dto;

import com.licenta.domain.Skill;
import org.springframework.stereotype.Component;

@Component
public class SkillDTOMapper {

    public Skill getEntityFromDTO(SkillDTO skillDTO){
        Skill skill = new Skill();

        updateEntityFields(skill, skillDTO);

        return skill;
    }

    public SkillDTO getDTOFromEntity(Skill skill) {
        SkillDTO skillDTO = new SkillDTO();

        skillDTO.setId(skill.getId());
        skillDTO.setProjectId(skill.getProject().getId());
        skillDTO.setSkill(skill.getSkill());
        skillDTO.setDescription(skill.getDescription());
        skillDTO.setSkillPoints(skill.getSkillPoints());
        skillDTO.setStatus(skill.getStatus());

        return skillDTO;
    }

    public void updateEntityFields(Skill skill, SkillDTO skillDTO) {
        skill.setSkill(skillDTO.getSkill());
        skill.setDescription(skillDTO.getDescription());
        skill.setSkillPoints(skillDTO.getSkillPoints());
    }
}
