package com.licenta.service.dto;

import com.licenta.domain.SkillStatus;

public class SkillDTO {
    private Long id;
    private Long projectId;
    private String skill;
    private String description;
    private Double skillPoints;
    private SkillStatus status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getSkillPoints() {
        return skillPoints;
    }

    public void setSkillPoints(Double skillPoints) {
        this.skillPoints = skillPoints;
    }

    public SkillStatus getStatus() {
        return status;
    }

    public void setStatus(SkillStatus status) {
        this.status = status;
    }
}
