package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.Announcement;
import com.licenta.domain.Attachment;
import com.licenta.domain.TeachingMaterial;
import com.licenta.domain.repository.AnnouncementJPARepository;
import com.licenta.domain.vo.AnnouncementVO;
import com.licenta.domain.vo.AnnouncementVOMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;


@Service
public class AnnouncementServiceImpl implements AnnouncementService{
    private final AnnouncementJPARepository announcementJPARepository;
    private final AnnouncementVOMapper announcementVOMapper;
    private final AttachmentService attachmentService;

    public AnnouncementServiceImpl(AnnouncementJPARepository announcementJPARepository, AnnouncementVOMapper announcementVOMapper, AttachmentService attachmentService) {
        this.announcementJPARepository = announcementJPARepository;
        this.announcementVOMapper = announcementVOMapper;
        this.attachmentService = attachmentService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnnouncementVO> getMyAnnouncements(Pageable pageable) {
        Page<Announcement> announcements = announcementJPARepository.findAllByUserId(UserContextHolder.getUserContext().getUserId(), pageable);
        return announcements.map(announcement -> {
            if(announcement instanceof TeachingMaterial)
                return announcementVOMapper.getVOFromEntity(announcement, attachmentService.getAttachmentsNotDeletedByTeachingMaterialId(announcement.getId()).toArray(new Attachment[0]));
            else {
                return announcementVOMapper.getVOFromEntity(announcement);
            }
        });
    }
}
