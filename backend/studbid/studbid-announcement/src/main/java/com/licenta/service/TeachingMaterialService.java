package com.licenta.service;

import com.licenta.domain.TeachingMaterial;
import com.licenta.service.dto.TeachingMaterialDTO;

public interface TeachingMaterialService {
    TeachingMaterialDTO save(TeachingMaterialDTO teachingMaterialDTO);

    TeachingMaterialDTO update(TeachingMaterialDTO teachingMaterialDTO);

    TeachingMaterial getById(Long id);

    void delete(Long id);

    TeachingMaterialDTO getTemplate(Long id);
}
