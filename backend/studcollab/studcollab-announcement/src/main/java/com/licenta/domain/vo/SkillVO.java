package com.licenta.domain.vo;

import com.licenta.domain.SkillStatus;

public class SkillVO {
    private Long id;
    private String skill;
    private String description;
    private Double skillPoints;
    private SkillStatus status;


    public SkillVO(Long id, String skill, String description, Double skillPoints, SkillStatus status) {
        this.id = id;
        this.skill = skill;
        this.description = description;
        this.skillPoints = skillPoints;
        this.status = status;
    }

    public Long getId() {
        return id;
    }

    public String getSkill() {
        return skill;
    }

    public String getDescription() {
        return description;
    }

    public Double getSkillPoints() {
        return skillPoints;
    }

    public SkillStatus getStatus() {
        return status;
    }
}
