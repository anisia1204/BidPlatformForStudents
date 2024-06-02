package com.licenta.domain;

import javax.persistence.*;

@Entity
@Table(name = "favorite_announcement")
public class FavoriteAnnouncement {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(columnDefinition = "SERIAL")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "FK_FAVORITE_ANNOUNCEMENT__USER"))
    private User user;

    @ManyToOne(optional = false)
    @JoinColumn(name = "announcement_id", foreignKey = @ForeignKey(name = "FK_FAVORITE_ANNOUNCEMENT__ANNOUNCEMENT"))
    private Announcement announcement;

    public FavoriteAnnouncement() {
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

    public Announcement getAnnouncement() {
        return announcement;
    }

    public void setAnnouncement(Announcement announcement) {
        this.announcement = announcement;
    }
}
