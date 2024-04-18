package com.licenta.service;

import com.licenta.domain.TeachingMaterial;
import com.licenta.service.dto.TeachingMaterialDTO;
import org.springframework.web.multipart.MultipartFile;

public interface TeachingMaterialService {
    TeachingMaterialDTO save(String teachingMaterialString, MultipartFile[] files);

    TeachingMaterialDTO update(String teachingMaterialString, MultipartFile[] files);

    TeachingMaterial getById(Long id);

    void delete(Long id);

    TeachingMaterialDTO getTemplate(Long id);
    long countAllByUserId(Long userId);
    long countAllByUserIdAndStatusIsSold(Long userId);

}
