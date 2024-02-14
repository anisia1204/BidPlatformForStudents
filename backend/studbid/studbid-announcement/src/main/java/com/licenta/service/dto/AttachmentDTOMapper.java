package com.licenta.service.dto;

import com.licenta.domain.Attachment;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;


@Component
public class AttachmentDTOMapper {
    public Attachment getEntityFromDTO(AttachmentDTO attachmentDTO){
        Attachment attachment = new Attachment();

        updateEntityFields(attachment, attachmentDTO);

        return attachment;
    }

    public AttachmentDTO getDTOFromEntity(Attachment attachment) {
        AttachmentDTO attachmentDTO = new AttachmentDTO();

        attachmentDTO.setId(attachment.getId());
        attachmentDTO.setTeachingMaterialId(attachment.getTeachingMaterial().getId());
        attachmentDTO.setName(attachment.getName());
        attachmentDTO.setSize(attachment.getSize());
        attachmentDTO.setFileContent(attachment.getFileContent());

        return attachmentDTO;
    }

    public AttachmentDTO getDTOFromFile(MultipartFile file){
        AttachmentDTO attachmentDTO = new AttachmentDTO();

        attachmentDTO.setName(file.getOriginalFilename());
        attachmentDTO.setSize(file.getSize());
        try {
            attachmentDTO.setFileContent(file.getBytes());
        }
        catch(IOException e) {
            throw new ArithmeticException("Can't upload file " + file.getOriginalFilename());
        }
        return attachmentDTO;
    }

    public void updateEntityFields(Attachment attachment, AttachmentDTO attachmentDTO) {
        attachment.setName(attachmentDTO.getName());
        attachment.setSize(attachmentDTO.getSize());
        attachment.setFileContent(attachmentDTO.getFileContent());
    }
}
