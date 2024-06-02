package com.licenta.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(columnDefinition = "SERIAL")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_ANNOUNCEMENT__USER"))
    private User user;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "points", nullable = false)
    private Double points;

    @Column(name = "status", nullable = false)
    private AnnouncementStatus status;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "deleted", nullable = false)
    private Boolean deleted;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL)
    private List<FavoriteAnnouncement> favoriteAnnouncements;

    public Announcement() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getPoints() {
        return points;
    }

    public void setPoints(Double points) {
        this.points = points;
    }

    public AnnouncementStatus getStatus() {
        return status;
    }

    public void setStatus(AnnouncementStatus status) {
        this.status = status;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public List<FavoriteAnnouncement> getFavoriteAnnouncements() {
        return favoriteAnnouncements;
    }

    public void setFavoriteAnnouncements(List<FavoriteAnnouncement> favoriteAnnouncements) {
        this.favoriteAnnouncements = favoriteAnnouncements;
    }
}
