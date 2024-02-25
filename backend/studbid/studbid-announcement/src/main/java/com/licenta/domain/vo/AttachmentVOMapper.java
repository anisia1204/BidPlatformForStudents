package com.licenta.domain.vo;

import com.licenta.domain.Attachment;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AttachmentVOMapper {
    public String getEncodedFileContentFromEntity(Attachment attachment){
        return Base64.getEncoder().encodeToString(attachment.getFileContent());
    }

    public List<String> getEncodedFileContentsFromEntities(List<Attachment> attachments){
        return attachments.stream().map(this::getEncodedFileContentFromEntity).collect(Collectors.toList());
    }
}
