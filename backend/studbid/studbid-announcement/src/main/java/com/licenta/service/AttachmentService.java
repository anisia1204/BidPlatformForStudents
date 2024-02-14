package com.licenta.service;

import com.licenta.domain.TeachingMaterial;
import com.licenta.service.dto.AttachmentDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AttachmentService {
    List<AttachmentDTO> saveAll(MultipartFile[] files, TeachingMaterial teachingMaterial);
    AttachmentDTO save(AttachmentDTO attachmentDTO, TeachingMaterial teachingMaterial);
}
