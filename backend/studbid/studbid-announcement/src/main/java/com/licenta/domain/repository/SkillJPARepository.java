package com.licenta.domain.repository;

import com.licenta.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SkillJPARepository extends JpaRepository<Skill, Long> {
}
