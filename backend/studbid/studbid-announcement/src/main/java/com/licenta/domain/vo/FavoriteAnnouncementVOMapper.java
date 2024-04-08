package com.licenta.domain.vo;

import com.licenta.domain.FavoriteAnnouncement;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FavoriteAnnouncementVOMapper {
    public FavoriteAnnouncementVO getVOFromEntity(FavoriteAnnouncement favoriteAnnouncement) {
        return new FavoriteAnnouncementVO(
                favoriteAnnouncement.getId(),
                favoriteAnnouncement.getUser().getId(),
                favoriteAnnouncement.getAnnouncement().getId()
        );
    }

    public List<FavoriteAnnouncementVO> getVOsFromEntities(List<FavoriteAnnouncement> favoriteAnnouncements){
        return favoriteAnnouncements
                .stream()
                .map(this::getVOFromEntity)
                .toList();
    }
}
