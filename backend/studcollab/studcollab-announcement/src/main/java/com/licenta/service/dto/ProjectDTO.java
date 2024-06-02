package com.licenta.service.dto;

import java.util.List;

public class ProjectDTO extends AnnouncementDTO{

    private String domain;
    private Integer teamSize;
    private List<SkillDTO> requiredSkills;


    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public Integer getTeamSize() {
        return teamSize;
    }

    public void setTeamSize(Integer teamSize) {
        this.teamSize = teamSize;
    }

    public List<SkillDTO> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<SkillDTO> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
}
