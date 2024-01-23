package com.licenta.domain.repository;

import com.licenta.domain.Announcement;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnnouncementJPARepository extends JpaRepository<Announcement, Long> {
}
