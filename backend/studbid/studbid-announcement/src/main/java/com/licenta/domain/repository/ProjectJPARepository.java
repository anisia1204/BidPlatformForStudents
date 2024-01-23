package com.licenta.domain.repository;

import com.licenta.domain.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectJPARepository extends JpaRepository<Project, Long> {
}
