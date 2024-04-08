package com.licenta.service;

import com.licenta.service.dto.FavoriteAnnouncementDTO;

public interface FavoriteAnnouncementService {
    FavoriteAnnouncementDTO addToFavorites(FavoriteAnnouncementDTO favoriteAnnouncementDTO);
    boolean removeFromFavorites(Long favoriteAnnouncementId);
}
