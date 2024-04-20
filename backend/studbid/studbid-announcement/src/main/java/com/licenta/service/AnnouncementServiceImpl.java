package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.*;
import com.licenta.domain.repository.AnnouncementJPARepository;
import com.licenta.domain.vo.*;
import com.licenta.service.exception.AnnouncementNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class AnnouncementServiceImpl implements AnnouncementService{
    private final AnnouncementJPARepository announcementJPARepository;
    private final AnnouncementVOMapper announcementVOMapper;
    private final AttachmentService attachmentService;
    private final TeachingMaterialService teachingMaterialService;
    private final TutoringServiceService tutoringServiceService;
    private final ProjectService projectService;
    public AnnouncementServiceImpl(AnnouncementJPARepository announcementJPARepository, AnnouncementVOMapper announcementVOMapper, AttachmentService attachmentService, TeachingMaterialService teachingMaterialService, TutoringServiceService tutoringServiceService, ProjectService projectService) {
        this.announcementJPARepository = announcementJPARepository;
        this.announcementVOMapper = announcementVOMapper;
        this.attachmentService = attachmentService;
        this.teachingMaterialService = teachingMaterialService;
        this.tutoringServiceService = tutoringServiceService;
        this.projectService = projectService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnnouncementVO> getMyAnnouncements(Pageable pageable) {
        Page<Announcement> announcements = announcementJPARepository.findAllByUserIdOrderByStatusAsc(UserContextHolder.getUserContext().getUserId(), pageable);
        return mapPageToVOs(announcements);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Announcement announcement = getById(id);
        if(announcement instanceof TeachingMaterial)
            teachingMaterialService.delete(id);
        else if (announcement instanceof TutoringService)
            tutoringServiceService.delete(id);
        else
            projectService.delete(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Announcement getById(Long id) {
        return announcementJPARepository.findById(id).orElseThrow(AnnouncementNotFoundException::new);
    }

    @Override
    @Transactional
    public Announcement markAsSold(Long announcementId) {
        Announcement announcement = getById(announcementId);
        announcement.setStatus(AnnouncementStatus.SOLD);
        return announcementJPARepository.saveAndFlush(announcement);
    }

    @Override
    @Transactional(readOnly = true)
    public AnnouncementVO getDetails(Long id) {
        Announcement announcement = getById(id);
        return mapEntityToVO(announcement);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnnouncementVO> getDashboardAnnouncements(Pageable pageable) {
        Page<Announcement> announcements = announcementJPARepository.findAllByUserIdIsNotAndDeletedIsFalse(UserContextHolder.getUserContext().getUserId(), pageable);
        return mapPageToVOs(announcements);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnnouncementVO> getFavoriteAnnouncements(List<Announcement> favoriteAnnouncementsOfUser, Pageable pageRequest) {
        List<Announcement> pagedFavoriteAnnouncementsOfUser = paginateAnnouncementList(favoriteAnnouncementsOfUser, pageRequest);
        return mapPageToVOs(new PageImpl<>(
                pagedFavoriteAnnouncementsOfUser,
                pageRequest,
                favoriteAnnouncementsOfUser.size()
            ));
    }

    @Override
    @Transactional(readOnly = true)
    public ChartDataVO getChartData() {
        Long loggedInUserId = UserContextHolder.getUserContext().getUserId();

        CreatedAnnouncementsVO createdAnnouncementsVO = new CreatedAnnouncementsVO(
                projectService.countAllByUserId(loggedInUserId),
                teachingMaterialService.countAllByUserId(loggedInUserId),
                tutoringServiceService.countAllByUserId(loggedInUserId)
        );

        SoldAnnouncementsVO soldAnnouncementsVO = new SoldAnnouncementsVO(
                projectService.countAllByUserIdAndStatusIsSold(loggedInUserId),
                teachingMaterialService.countAllByUserIdAndStatusIsSold(loggedInUserId),
                tutoringServiceService.countAllByUserIdAndStatusIsSold(loggedInUserId)
        );

        return new ChartDataVO(
                createdAnnouncementsVO,
                soldAnnouncementsVO
        );
    }

    private Page<AnnouncementVO> mapPageToVOs(Page<Announcement> announcements) {
        return announcements.map(this::mapEntityToVO);
    }

    private AnnouncementVO mapEntityToVO(Announcement announcement) {
        if(announcement instanceof TeachingMaterial)
            return announcementVOMapper.getVOFromEntity(announcement, attachmentService.getAttachmentsNotDeletedByTeachingMaterialId(announcement.getId()).toArray(new Attachment[0]));
        else {
            return announcementVOMapper.getVOFromEntity(announcement);
        }
    }

    private List<Announcement> paginateAnnouncementList(List<Announcement> favoriteAnnouncementsOfUser, Pageable pageRequest) {
        int start = (int) pageRequest.getOffset();
        int end = Math.min((start + pageRequest.getPageSize()), favoriteAnnouncementsOfUser.size());
        return favoriteAnnouncementsOfUser.subList(start, end);
    }


}
