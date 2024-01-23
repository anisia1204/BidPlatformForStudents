package com.licenta.domain;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "project")
public class Project extends Announcement{
    @Column(name = "domain", nullable = false)
    private String domain;

    @Column(name = "team_size", nullable = false)
    private Integer teamSize;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL)
    private List<Skill> requiredSkills;

    public Project() {
    }

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

    public List<Skill> getRequiredSkills() {
        return requiredSkills;
    }

    public void setRequiredSkills(List<Skill> requiredSkills) {
        this.requiredSkills = requiredSkills;
    }
}
