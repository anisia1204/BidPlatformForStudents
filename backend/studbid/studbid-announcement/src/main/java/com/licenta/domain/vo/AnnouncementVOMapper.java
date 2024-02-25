package com.licenta.domain.vo;

import com.licenta.domain.*;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AnnouncementVOMapper {
    private final SkillVOMapper skillVOMapper;
    private final AttachmentVOMapper attachmentVOMapper;

    public AnnouncementVOMapper(SkillVOMapper skillVOMapper, AttachmentVOMapper attachmentVOMapper) {
        this.skillVOMapper = skillVOMapper;
        this.attachmentVOMapper = attachmentVOMapper;
    }

    public AnnouncementVO getVOFromEntity(Announcement announcement, Attachment... attachments) {
        return new AnnouncementVO(
                announcement.getId(),
                announcement.getUser().getId(),
                announcement.getTitle(),
                announcement.getDescription(),
                announcement.getPoints(),
                announcement.getStatus(),
                announcement.getCreatedAt(),
                (announcement instanceof Project) ? ((Project) announcement).getDomain() : null,
                (announcement instanceof Project) ? ((Project) announcement).getTeamSize() : null,
                (announcement instanceof Project) ? skillVOMapper.getVOsFromEntities(((Project) announcement).getRequiredSkills()) : null,
                (announcement instanceof Project) ? "project" : (announcement instanceof TeachingMaterial ? "teachingMaterial" : "tutoringService"),
                (announcement instanceof TeachingMaterial) ? ((TeachingMaterial) announcement).getName() : null,
                (announcement instanceof TeachingMaterial) ? ((TeachingMaterial) announcement).getAuthor() : null,
                (announcement instanceof TeachingMaterial) ? ((TeachingMaterial) announcement).getEdition() : null,
                (announcement instanceof TeachingMaterial && attachments.length >= 1 ) ? attachmentVOMapper.getEncodedFileContentsFromEntities(List.of(attachments)) : null,
                (announcement instanceof TutoringService) ? ((TutoringService) announcement).getSubject() : null,
                (announcement instanceof TutoringService) ? ((TutoringService) announcement).getStartDate() : null,
                (announcement instanceof TutoringService) ? ((TutoringService) announcement).getEndDate() : null,
                (announcement instanceof TutoringService) ? ((TutoringService) announcement).getHoursPerSession() : null,
                (announcement instanceof TutoringService) ? ((TutoringService) announcement).getTutoringType() : null
        );
    }
}
