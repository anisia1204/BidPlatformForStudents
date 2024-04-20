package com.licenta.domain.vo;

import com.licenta.domain.*;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
public class AnnouncementVOMapper {
    private final SkillVOMapper skillVOMapper;
    private final AttachmentVOMapper attachmentVOMapper;
    private final UserDetailsVOMapper userDetailsVOMapper;

    private final FavoriteAnnouncementVOMapper favoriteAnnouncementVOMapper;

    public AnnouncementVOMapper(SkillVOMapper skillVOMapper, AttachmentVOMapper attachmentVOMapper, UserDetailsVOMapper userDetailsVOMapper, FavoriteAnnouncementVOMapper favoriteAnnouncementVOMapper) {
        this.skillVOMapper = skillVOMapper;
        this.attachmentVOMapper = attachmentVOMapper;
        this.userDetailsVOMapper = userDetailsVOMapper;
        this.favoriteAnnouncementVOMapper = favoriteAnnouncementVOMapper;
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
                userDetailsVOMapper.getVOFromEntity(announcement.getUser()),
                (announcement instanceof Project) ? ((Project) announcement).getDomain() : null,
                (announcement instanceof Project) ? ((Project) announcement).getTeamSize() : null,
                (announcement instanceof Project) ? skillVOMapper.getVOsFromEntities(((Project) announcement).getRequiredSkills().stream().filter(skill -> !skill.getDeleted()).sorted(Comparator.comparing(Skill::getStatus)).toList()) : null,
                (announcement instanceof Project) ? "project" : (announcement instanceof TeachingMaterial ? "teachingMaterial" : "tutoringService"),
                (announcement instanceof TeachingMaterial) ? ((TeachingMaterial) announcement).getName() : null,
                (announcement instanceof TeachingMaterial) ? ((TeachingMaterial) announcement).getAuthor() : null,
                (announcement instanceof TeachingMaterial) ? ((TeachingMaterial) announcement).getEdition() : null,
                (announcement instanceof TeachingMaterial && attachments.length >= 1 ) ? attachmentVOMapper.getEncodedFileContentsFromEntities(List.of(attachments)) : null,
                (announcement instanceof TutoringService) ? ((TutoringService) announcement).getSubject() : null,
                (announcement instanceof TutoringService) ? ((TutoringService) announcement).getStartDate() : null,
                (announcement instanceof TutoringService) ? ((TutoringService) announcement).getEndDate() : null,
                (announcement instanceof TutoringService) ? ((TutoringService) announcement).getHoursPerSession() : null,
                (announcement instanceof TutoringService) ? ((TutoringService) announcement).getTutoringType() : null,
                favoriteAnnouncementVOMapper.getVOsFromEntities(announcement.getFavoriteAnnouncements()));
    }
}
