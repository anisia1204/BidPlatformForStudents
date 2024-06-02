package com.licenta.domain.repository;

import com.licenta.domain.AnnouncementStatus;
import com.licenta.domain.TutoringService;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TutoringServiceJPARepository extends JpaRepository<TutoringService, Long> {
    long countAllByUserId(Long userId);
    long countAllByUserIdAndStatusIs(Long userId, AnnouncementStatus status);

}
