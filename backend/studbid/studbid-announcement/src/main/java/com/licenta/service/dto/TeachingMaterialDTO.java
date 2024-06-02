package com.licenta.service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)

public class TeachingMaterialDTO extends AnnouncementDTO{
    private String name;
    private String author;
    private Integer edition;
    private List<AttachmentDTO> attachmentDTOs;
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public Integer getEdition() {
        return edition;
    }

    public void setEdition(Integer edition) {
        this.edition = edition;
    }

    public List<AttachmentDTO> getAttachmentDTOs() {
        return attachmentDTOs;
    }

    public void setAttachmentDTOs(List<AttachmentDTO> attachmentDTOs) {
        this.attachmentDTOs = attachmentDTOs;
    }
}
