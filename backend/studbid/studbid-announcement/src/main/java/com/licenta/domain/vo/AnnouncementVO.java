package com.licenta.domain.vo;

import com.licenta.domain.AnnouncementStatus;
import com.licenta.domain.TutoringType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class AnnouncementVO {
    private Long id;
    private Long userId;
    private String title;
    private String description;
    private Double points;
    private AnnouncementStatus status;
    private LocalDateTime createdAt;
    private String domain;
    private Integer teamSize;
    private List<SkillVO> requiredSkills;

    private String announcementType;
    private String name;
    private String author;
    private Integer edition;
    private List<String> attachmentVOs;
    private String subject;
    private LocalDate startDate;
    private LocalDate endDate;
    private Integer hoursPerSession;
    private TutoringType tutoringType;
    private UserDetailsVO userDetailsVO;


    public AnnouncementVO(Long id, Long userId, String title, String description, Double points, AnnouncementStatus status, LocalDateTime createdAt, UserDetailsVO userDetailsVO, String domain, Integer teamSize, List<SkillVO> requiredSkills, String announcementType, String name, String author, Integer edition, List<String> attachmentVOs, String subject, LocalDate startDate, LocalDate endDate, Integer hoursPerSession, TutoringType tutoringType) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.points = points;
        this.status = status;
        this.createdAt = createdAt;
        this.userDetailsVO = userDetailsVO;
        this.domain = domain;
        this.teamSize = teamSize;
        this.requiredSkills = requiredSkills;
        this.announcementType = announcementType;
        this.name = name;
        this.author = author;
        this.edition = edition;
        this.attachmentVOs = attachmentVOs;
        this.subject = subject;
        this.startDate = startDate;
        this.endDate = endDate;
        this.hoursPerSession = hoursPerSession;
        this.tutoringType = tutoringType;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Double getPoints() {
        return points;
    }

    public AnnouncementStatus getStatus() {
        return status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public UserDetailsVO getUserDetailsVO() {
        return userDetailsVO;
    }

    public String getDomain() {
        return domain;
    }

    public Integer getTeamSize() {
        return teamSize;
    }

    public List<SkillVO> getRequiredSkills() {
        return requiredSkills;
    }

    public String getAnnouncementType() {
        return announcementType;
    }

    public String getName() {
        return name;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getEdition() {
        return edition;
    }

    public List<String> getAttachmentVOs() {
        return attachmentVOs;
    }

    public String getSubject() {
        return subject;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Integer getHoursPerSession() {
        return hoursPerSession;
    }

    public TutoringType getTutoringType() {
        return tutoringType;
    }
}
