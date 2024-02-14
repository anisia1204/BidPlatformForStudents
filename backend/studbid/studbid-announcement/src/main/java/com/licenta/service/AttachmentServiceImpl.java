package com.licenta.service;

import com.licenta.domain.Attachment;
import com.licenta.domain.TeachingMaterial;
import com.licenta.domain.repository.AttachmentJPARepository;
import com.licenta.service.dto.AttachmentDTO;
import com.licenta.service.dto.AttachmentDTOMapper;
import com.licenta.service.exception.AttachmentNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class AttachmentServiceImpl implements AttachmentService{
    private final AttachmentDTOMapper attachmentDTOMapper;
    private final AttachmentJPARepository attachmentJPARepository;

    public AttachmentServiceImpl(AttachmentDTOMapper attachmentDTOMapper, AttachmentJPARepository attachmentJPARepository) {
        this.attachmentDTOMapper = attachmentDTOMapper;
        this.attachmentJPARepository = attachmentJPARepository;
    }

    @Override
    @Transactional
    public List<AttachmentDTO> saveAll(MultipartFile[] files, TeachingMaterial teachingMaterial) {
        List<AttachmentDTO> savedAttachmentDTOs = new ArrayList<>();
        if (files != null) {
            for (MultipartFile file : files) {
                AttachmentDTO attachmentDTO = attachmentDTOMapper.getDTOFromFile(file);
                AttachmentDTO savedAttachmentDTO = save(attachmentDTO, teachingMaterial);
                savedAttachmentDTOs.add(savedAttachmentDTO);
            }
        }
        return savedAttachmentDTOs;
    }

    @Override
    @Transactional
    public AttachmentDTO save(AttachmentDTO attachmentDTO, TeachingMaterial teachingMaterial) {
        Attachment attachment = attachmentDTOMapper.getEntityFromDTO(attachmentDTO);
        attachment.setDeleted(false);
        attachment.setTeachingMaterial(teachingMaterial);
        attachmentJPARepository.save(attachment);
        attachmentDTO = attachmentDTOMapper.getDTOFromEntity(attachment);
        return  attachmentDTO;

    }

    @Override
    @Transactional(readOnly = true)
    public List<Attachment> getAttachmentsByTeachingMaterialId(Long id) {
        return attachmentJPARepository.findAllByTeachingMaterialId(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<AttachmentDTO> getAllDTOSNotDeletedByTeachingMaterialId(Long id) {
        return attachmentJPARepository.findAllByTeachingMaterialIdAndDeletedEquals(id, false)
                .stream()
                .map(attachmentDTOMapper::getDTOFromEntity)
                .toList();
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Attachment attachment = getById(id);
        attachment.setDeleted(true);
        attachmentJPARepository.save(attachment);
    }

    @Override
    @Transactional(readOnly = true)
    public Attachment getById(Long id) {
        return attachmentJPARepository.findById(id).orElseThrow(AttachmentNotFoundException::new);
    }

    @Override
    @Transactional
    public void updateAll(List<Attachment> existingAttachments, List<AttachmentDTO> attachmentDTOS) {
        existingAttachments.forEach(
                existingAttachment -> {
                    boolean deleted = true;
                    for(AttachmentDTO attachmentDTO : attachmentDTOS) {
                        if(Objects.equals(attachmentDTO.getId(), existingAttachment.getId()))
                            deleted = false;
                    }
                    if(deleted){
                        delete(existingAttachment.getId());
                    }
                }
        );
    }
}
