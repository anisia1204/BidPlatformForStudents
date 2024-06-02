package com.licenta.domain.repository;

import com.licenta.domain.FavoriteAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface FavoriteAnnouncementJPARepository extends JpaRepository<FavoriteAnnouncement, Long> {
    List<FavoriteAnnouncement> findAllByUserId(Long userId);
}
