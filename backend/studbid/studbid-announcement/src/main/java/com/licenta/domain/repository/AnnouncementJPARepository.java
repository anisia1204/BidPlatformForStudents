package com.licenta.domain.repository;

import com.licenta.domain.Announcement;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementJPARepository extends JpaRepository<Announcement, Long> {
    Page<Announcement> findAllByUserIdOrderByStatusAsc(Long id, Pageable pageable);
    Page<Announcement> findAllByUserIdIsNotAndDeletedIsFalse(Long id, Pageable pageable);
}
