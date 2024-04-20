package com.licenta.domain.repository;

import com.licenta.domain.Skill;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkillJPARepository extends JpaRepository<Skill, Long> {
    List<Skill> findAllByProjectIdAndDeletedEquals(Long id, boolean deleted);
    List<Skill> findAllByProjectIdAndDeletedEqualsOrderByStatusAsc(Long id, boolean deleted);
}
