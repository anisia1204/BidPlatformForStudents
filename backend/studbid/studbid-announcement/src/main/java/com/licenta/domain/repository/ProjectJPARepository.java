package com.licenta.domain.repository;

import com.licenta.domain.AnnouncementStatus;
import com.licenta.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectJPARepository extends JpaRepository<Project, Long> {
    long countAllByUserId(Long userId);
    long countAllByUserIdAndStatusIs(Long userId, AnnouncementStatus status);
}
