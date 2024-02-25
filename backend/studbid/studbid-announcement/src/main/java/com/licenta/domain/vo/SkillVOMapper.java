package com.licenta.domain.vo;

import com.licenta.domain.Skill;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SkillVOMapper {
    public SkillVO getVOFromEntity(Skill skill) {
        return new SkillVO(
                skill.getId(),
                skill.getSkill(),
                skill.getDescription(),
                skill.getSkillPoints(),
                skill.getStatus()
        );
    }

    public List<SkillVO> getVOsFromEntities(List<Skill> skills) {
        return skills.stream().map(this::getVOFromEntity).collect(Collectors.toList());
    }
}
