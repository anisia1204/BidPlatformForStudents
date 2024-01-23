package com.licenta.domain.repository;

import com.licenta.domain.TeachingMaterial;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeachingMaterialJPARepository extends JpaRepository<TeachingMaterial, Long> {
}
