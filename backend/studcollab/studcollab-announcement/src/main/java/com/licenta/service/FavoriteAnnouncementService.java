package com.licenta.service;

import com.licenta.domain.Announcement;
import com.licenta.service.dto.FavoriteAnnouncementDTO;

import java.util.List;

public interface FavoriteAnnouncementService {
    FavoriteAnnouncementDTO addToFavorites(FavoriteAnnouncementDTO favoriteAnnouncementDTO);
    boolean removeFromFavorites(Long favoriteAnnouncementId);
    List<Announcement> getFavoriteAnnouncementsOfCurrentUser();
}
