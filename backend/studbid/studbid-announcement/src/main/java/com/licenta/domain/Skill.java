package com.licenta.domain;

import javax.persistence.*;

@Entity
@Table(name = "skill")
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "SERIAL")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "project_id", foreignKey = @ForeignKey(name = "FK_SKILL__PROJECT"))
    private Project project;

    @Column(name = "skill", nullable = false)
    private String skill;

    @Column(name = "description")
    private String description;

    @Column(name = "skill_points", nullable = false)
    private Double skillPoints;

    @Column(name = "status", nullable = false)
    private SkillStatus status;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    public Skill() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
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

    public void setSkillPoints(Double points) {
        this.skillPoints = points;
    }

    public SkillStatus getStatus() {
        return status;
    }

    public void setStatus(SkillStatus status) {
        this.status = status;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }
}
