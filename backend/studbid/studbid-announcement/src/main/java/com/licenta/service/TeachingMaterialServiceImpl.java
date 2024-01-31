package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.AnnouncementStatus;
import com.licenta.domain.TeachingMaterial;
import com.licenta.domain.repository.TeachingMaterialJPARepository;
import com.licenta.service.dto.TeachingMaterialDTO;
import com.licenta.service.dto.TeachingMaterialDTOMapper;
import com.licenta.service.exception.TeachingMaterialNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class TeachingMaterialServiceImpl implements TeachingMaterialService{
    private final TeachingMaterialJPARepository teachingMaterialJPARepository;
    private final TeachingMaterialDTOMapper teachingMaterialDTOMapper;

    public TeachingMaterialServiceImpl(TeachingMaterialJPARepository teachingMaterialJPARepository, TeachingMaterialDTOMapper teachingMaterialDTOMapper) {
        this.teachingMaterialJPARepository = teachingMaterialJPARepository;
        this.teachingMaterialDTOMapper = teachingMaterialDTOMapper;
    }

    @Override
    @Transactional
    public TeachingMaterialDTO save(TeachingMaterialDTO teachingMaterialDTO) {
        teachingMaterialDTO.setUserId(UserContextHolder.getUserContext().getUserId());
        TeachingMaterial teachingMaterial = teachingMaterialDTOMapper.getEntityFromDTO(teachingMaterialDTO);
        teachingMaterial.setStatus(AnnouncementStatus.ACTIVE);
        teachingMaterial.setCreatedAt(LocalDateTime.now());
        teachingMaterial.setDeleted(false);
        teachingMaterialJPARepository.saveAndFlush(teachingMaterial);
        teachingMaterialDTO = teachingMaterialDTOMapper.getDTOFromEntity(teachingMaterial);
        return teachingMaterialDTO;
    }

    @Override
    @Transactional
    public TeachingMaterialDTO update(TeachingMaterialDTO teachingMaterialDTO) {
        TeachingMaterial teachingMaterial = getById(teachingMaterialDTO.getId());
        teachingMaterialDTOMapper.updateEntityFields(teachingMaterial, teachingMaterialDTO);
        teachingMaterialDTO = teachingMaterialDTOMapper.getDTOFromEntity(teachingMaterial);
        return teachingMaterialDTO;
    }

    @Transactional(readOnly = true)
    public TeachingMaterial getById(Long id) {
        return teachingMaterialJPARepository.findById(id).orElseThrow(TeachingMaterialNotFoundException::new);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        TeachingMaterial teachingMaterial = getById(id);
        teachingMaterial.setDeleted(true);
        teachingMaterialJPARepository.save(teachingMaterial);
    }

    @Override
    @Transactional(readOnly = true)
    public TeachingMaterialDTO getTemplate(Long id) {
        TeachingMaterial teachingMaterial = getById(id);
        return teachingMaterialDTOMapper.getDTOFromEntity(teachingMaterial);
    }
}
