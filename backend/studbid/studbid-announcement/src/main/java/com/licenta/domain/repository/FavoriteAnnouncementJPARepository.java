package com.licenta.domain.repository;

import com.licenta.domain.FavoriteAnnouncement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FavoriteAnnouncementJPARepository extends JpaRepository<FavoriteAnnouncement, Long> {
}
