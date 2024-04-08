package com.licenta.domain.vo;

public class FavoriteAnnouncementVO {
    private Long id;
    private Long userId;
    private Long announcementId;

    public FavoriteAnnouncementVO(Long id, Long userId, Long announcementId) {
        this.id = id;
        this.userId = userId;
        this.announcementId = announcementId;
    }

    public Long getId() {
        return id;
    }

    public Long getUserId() {
        return userId;
    }

    public Long getAnnouncementId() {
        return announcementId;
    }
}
