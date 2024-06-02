package com.licenta.service.dto;

import com.licenta.domain.FavoriteAnnouncement;
import org.springframework.stereotype.Component;

@Component
public class FavoriteAnnouncementDTOMapper {
    public FavoriteAnnouncementDTO getDTOFromEntity(FavoriteAnnouncement favoriteAnnouncement) {
        FavoriteAnnouncementDTO favoriteAnnouncementDTO = new FavoriteAnnouncementDTO();
        favoriteAnnouncementDTO.setId(favoriteAnnouncement.getId());
        favoriteAnnouncementDTO.setUserId(favoriteAnnouncement.getUser().getId());
        favoriteAnnouncementDTO.setAnnouncementId(favoriteAnnouncement.getAnnouncement().getId());
        return favoriteAnnouncementDTO;
    }
}
