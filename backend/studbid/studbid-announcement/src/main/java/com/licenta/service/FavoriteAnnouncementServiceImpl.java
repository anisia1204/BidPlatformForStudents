package com.licenta.service;

import com.licenta.domain.Announcement;
import com.licenta.domain.FavoriteAnnouncement;
import com.licenta.domain.User;
import com.licenta.domain.repository.FavoriteAnnouncementJPARepository;
import com.licenta.service.dto.FavoriteAnnouncementDTO;
import com.licenta.service.dto.FavoriteAnnouncementDTOMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FavoriteAnnouncementServiceImpl implements FavoriteAnnouncementService{

    private final FavoriteAnnouncementJPARepository favoriteAnnouncementJPARepository;
    private final AnnouncementService announcementService;
    private final UserService userService;
    private final FavoriteAnnouncementDTOMapper favoriteAnnouncementDTOMapper;

    public FavoriteAnnouncementServiceImpl(FavoriteAnnouncementJPARepository favoriteAnnouncementJPARepository, AnnouncementService announcementService, UserService userService, FavoriteAnnouncementDTOMapper favoriteAnnouncementDTOMapper) {
        this.favoriteAnnouncementJPARepository = favoriteAnnouncementJPARepository;
        this.announcementService = announcementService;
        this.userService = userService;
        this.favoriteAnnouncementDTOMapper = favoriteAnnouncementDTOMapper;
    }

    @Override
    @Transactional
    public FavoriteAnnouncementDTO addToFavorites(FavoriteAnnouncementDTO favoriteAnnouncementDTO) {
        User user = getUserById(favoriteAnnouncementDTO.getUserId());
        Announcement announcement = getAnnouncementById(favoriteAnnouncementDTO.getAnnouncementId());

        FavoriteAnnouncement favoriteAnnouncement = new FavoriteAnnouncement();
        favoriteAnnouncement.setAnnouncement(announcement);
        favoriteAnnouncement.setUser(user);
        return favoriteAnnouncementDTOMapper.getDTOFromEntity(favoriteAnnouncementJPARepository.save(favoriteAnnouncement));
    }

    private Announcement getAnnouncementById(Long announcementId) {
        return announcementService.getById(announcementId);
    }

    private User getUserById(Long userId) {
        return userService.findById(userId);
    }

    @Override
    @Transactional
    public boolean removeFromFavorites(Long favoriteAnnouncementId) {
        FavoriteAnnouncement favoriteAnnouncement = favoriteAnnouncementJPARepository.findById(favoriteAnnouncementId).orElseThrow();
        favoriteAnnouncementJPARepository.delete(favoriteAnnouncement);
        return true;
    }
}
