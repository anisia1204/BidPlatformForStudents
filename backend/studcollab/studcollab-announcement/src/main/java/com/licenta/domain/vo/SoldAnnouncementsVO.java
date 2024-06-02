package com.licenta.domain.vo;

public class SoldAnnouncementsVO {
    private Long projects;
    private Long teachingMaterials;
    private Long tutoringServices;

    public SoldAnnouncementsVO(Long projects, Long teachingMaterials, Long tutoringServices) {
        this.projects = projects;
        this.teachingMaterials = teachingMaterials;
        this.tutoringServices = tutoringServices;
    }

    public Long getProjects() {
        return projects;
    }

    public Long getTeachingMaterials() {
        return teachingMaterials;
    }

    public Long getTutoringServices() {
        return tutoringServices;
    }
}
