package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.AnnouncementStatus;
import com.licenta.domain.Attachment;
import com.licenta.domain.TeachingMaterial;
import com.licenta.domain.repository.TeachingMaterialJPARepository;
import com.licenta.service.dto.AttachmentDTO;
import com.licenta.service.dto.TeachingMaterialDTO;
import com.licenta.service.dto.TeachingMaterialDTOMapper;
import com.licenta.service.exception.TeachingMaterialNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class TeachingMaterialServiceImpl implements TeachingMaterialService{
    private final TeachingMaterialJPARepository teachingMaterialJPARepository;
    private final TeachingMaterialDTOMapper teachingMaterialDTOMapper;
    private final AttachmentService attachmentService;

    public TeachingMaterialServiceImpl(TeachingMaterialJPARepository teachingMaterialJPARepository, TeachingMaterialDTOMapper teachingMaterialDTOMapper, AttachmentService attachmentService) {
        this.teachingMaterialJPARepository = teachingMaterialJPARepository;
        this.teachingMaterialDTOMapper = teachingMaterialDTOMapper;
        this.attachmentService = attachmentService;
    }

    @Override
    @Transactional
    public TeachingMaterialDTO save(String teachingMaterialString, MultipartFile[] files) {
        TeachingMaterialDTO teachingMaterialDTO = teachingMaterialDTOMapper.getDTOFromString(teachingMaterialString);
        teachingMaterialDTO.setUserId(UserContextHolder.getUserContext().getUserId());
        TeachingMaterial teachingMaterial = teachingMaterialDTOMapper.getEntityFromDTO(teachingMaterialDTO);
        teachingMaterial.setStatus(AnnouncementStatus.ACTIVE);
        teachingMaterial.setCreatedAt(LocalDateTime.now());
        teachingMaterial.setDeleted(false);
        teachingMaterialJPARepository.saveAndFlush(teachingMaterial);
        List<AttachmentDTO> attachmentDTOS = saveAttachments(files, teachingMaterial);
        teachingMaterialDTO = teachingMaterialDTOMapper.getDTOFromEntity(teachingMaterial);
        teachingMaterialDTO.setAttachmentDTOs(attachmentDTOS);
        return teachingMaterialDTO;
    }

    private List<AttachmentDTO> saveAttachments(MultipartFile[] files, TeachingMaterial teachingMaterial) {
        return attachmentService.saveAll(files, teachingMaterial);
    }

    @Override
    @Transactional
    public TeachingMaterialDTO update(String teachingMaterialString, MultipartFile[] files) {
        TeachingMaterialDTO teachingMaterialDTO = teachingMaterialDTOMapper.getDTOFromString(teachingMaterialString);
        TeachingMaterial teachingMaterial = getById(teachingMaterialDTO.getId());
        teachingMaterialDTOMapper.updateEntityFields(teachingMaterial, teachingMaterialDTO);
        List<Attachment> existingAttachments = attachmentService.getAttachmentsByTeachingMaterialId(teachingMaterial.getId());
        saveNewAttachments(files, teachingMaterial);
        updateOldAttachments(existingAttachments, teachingMaterialDTO.getAttachmentDTOs());
        List<AttachmentDTO> updatedAttachmentDTOs = attachmentService.getAllDTOSNotDeletedByTeachingMaterialId(teachingMaterial.getId());
        teachingMaterialDTO = teachingMaterialDTOMapper.getDTOFromEntity(teachingMaterial);
        teachingMaterialDTO.setAttachmentDTOs(updatedAttachmentDTOs);
        return teachingMaterialDTO;
    }

    private void saveNewAttachments(MultipartFile[] files, TeachingMaterial teachingMaterial) {
        saveAttachments(files, teachingMaterial);
    }

    private void updateOldAttachments(List<Attachment> existingAttachments, List<AttachmentDTO> attachmentDTOS) {
        attachmentService.updateAll(existingAttachments, attachmentDTOS);
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
        teachingMaterial.setStatus(AnnouncementStatus.INACTIVE);
        teachingMaterialJPARepository.save(teachingMaterial);
    }

    @Override
    @Transactional(readOnly = true)
    public TeachingMaterialDTO getTemplate(Long id) {
        TeachingMaterial teachingMaterial = getById(id);
        TeachingMaterialDTO teachingMaterialDTO = teachingMaterialDTOMapper.getDTOFromEntity(teachingMaterial);
        teachingMaterialDTO.setAttachmentDTOs(attachmentService.getAllDTOSNotDeletedByTeachingMaterialId(id));
        return teachingMaterialDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllByUserId(Long userId) {
        return teachingMaterialJPARepository.countAllByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public long countAllByUserIdAndStatusIsSold(Long userId) {
        return teachingMaterialJPARepository.countAllByUserIdAndStatusIs(userId, AnnouncementStatus.SOLD);
    }
}
