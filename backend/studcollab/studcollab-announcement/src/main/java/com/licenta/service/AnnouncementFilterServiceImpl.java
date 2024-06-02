package com.licenta.service;

import com.licenta.context.UserContextHolder;
import com.licenta.domain.Announcement;
import com.licenta.domain.AnnouncementStatus;
import com.licenta.domain.Attachment;
import com.licenta.domain.TeachingMaterial;
import com.licenta.domain.vo.AnnouncementVO;
import com.licenta.domain.vo.AnnouncementVOMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class AnnouncementFilterServiceImpl implements AnnouncementFilterService {
    @PersistenceContext
    private EntityManager entityManager;
    private final AnnouncementVOMapper announcementVOMapper;
    private final AttachmentService attachmentService;

    public AnnouncementFilterServiceImpl(AnnouncementVOMapper announcementVOMapper, AttachmentService attachmentService) {
        this.announcementVOMapper = announcementVOMapper;
        this.attachmentService = attachmentService;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnnouncementVO> getMyAnnouncements(Pageable pageable,
                                                   String announcementTitle,
                                                   String announcementType,
                                                   Integer status,
                                                   Double min,
                                                   Double max,
                                                   LocalDateTime from,
                                                   LocalDateTime to) {
        StringBuilder jpqlBuilder = new StringBuilder("SELECT a FROM Announcement a WHERE a.user.id = :userId ");
        StringBuilder countQueryBuilder = new StringBuilder("SELECT COUNT(a) FROM Announcement a WHERE a.user.id = :userId ");

        return getFilteredPaginatedAndSortedAnnouncementVOS(pageable, announcementTitle, announcementType, status, min, max, from, to, jpqlBuilder, countQueryBuilder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnnouncementVO> getDashboardAnnouncements(Pageable pageable, String announcementTitle, String announcementType, Integer status, Double min, Double max, LocalDateTime from, LocalDateTime to) {
        StringBuilder jpqlBuilder = new StringBuilder("SELECT a FROM Announcement a WHERE a.user.id != :userId AND a.deleted = false ");
        StringBuilder countQueryBuilder = new StringBuilder("SELECT COUNT(a) FROM Announcement a WHERE a.user.id != :userId AND a.deleted = false ");

        return getFilteredPaginatedAndSortedAnnouncementVOS(pageable, announcementTitle, announcementType, status, min, max, from, to, jpqlBuilder, countQueryBuilder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AnnouncementVO> getFavoriteAnnouncements(Pageable pageable, String announcementTitle, String announcementType, Integer status, Double min, Double max, LocalDateTime from, LocalDateTime to) {
        StringBuilder jpqlBuilder = new StringBuilder("SELECT a FROM Announcement a JOIN FavoriteAnnouncement fa ON a.id = fa.announcement.id WHERE fa.user.id = :userId ");
        StringBuilder countQueryBuilder = new StringBuilder("SELECT COUNT(a) FROM Announcement a JOIN FavoriteAnnouncement fa ON a.id = fa.announcement.id WHERE fa.user.id = :userId  ");

        return getFilteredPaginatedAndSortedAnnouncementVOS(pageable, announcementTitle, announcementType, status, min, max, from, to, jpqlBuilder, countQueryBuilder);
    }

    private Page<AnnouncementVO> getFilteredPaginatedAndSortedAnnouncementVOS(Pageable pageable, String announcementTitle, String announcementType, Integer status, Double min, Double max, LocalDateTime from, LocalDateTime to, StringBuilder jpqlBuilder, StringBuilder countQueryBuilder) {
        if (announcementTitle != null && !announcementTitle.isEmpty()) {
            jpqlBuilder.append("AND (LOWER(a.title) LIKE :announcementTitle OR LOWER(a.description) LIKE :announcementTitle) ");
            countQueryBuilder.append("AND (LOWER(a.title) LIKE :announcementTitle OR LOWER(a.description) LIKE :announcementTitle) ");
        }

        if (announcementType != null && !announcementType.isEmpty()) {
            switch (announcementType) {
                case "Project":
                    jpqlBuilder.append("AND TYPE(a) = Project ");
                    countQueryBuilder.append("AND TYPE(a) = Project ");
                    break;
                case "TeachingMaterial":
                    jpqlBuilder.append("AND TYPE(a) = TeachingMaterial ");
                    countQueryBuilder.append("AND TYPE(a) = TeachingMaterial ");
                    break;
                case "TutoringService":
                    jpqlBuilder.append("AND TYPE(a) = TutoringService ");
                    countQueryBuilder.append("AND TYPE(a) = TutoringService ");
                    break;
                default:
                    break;
            }
        }

        if(status != null) {
            jpqlBuilder.append("AND a.status = :status ");
            countQueryBuilder.append("AND a.status = :status ");
        }

        if(from != null && to != null) {
            jpqlBuilder.append("AND a.createdAt BETWEEN :from AND :to ");
            countQueryBuilder.append("AND a.createdAt BETWEEN :from AND :to ");
        } else if(from != null) {
            jpqlBuilder.append("AND a.createdAt >= :from ");
            countQueryBuilder.append("AND a.createdAt >= :from ");
        } else if(to != null) {
            jpqlBuilder.append("AND a.createdAt <= :to ");
            countQueryBuilder.append("AND a.createdAt <= :to ");
        }

        if(min != null && max != null) {
            jpqlBuilder.append("AND a.points BETWEEN :min AND :max ");
            countQueryBuilder.append("AND a.points BETWEEN :min AND :max ");
        } else if(min != null) {
            jpqlBuilder.append("AND a.points >= :min ");
            countQueryBuilder.append("AND a.points >= :min ");
        } else if(max != null) {
            jpqlBuilder.append("AND a.points <= :max ");
            countQueryBuilder.append("AND a.points <= :max ");
        }

        if (pageable.getSort().isSorted()) {
            jpqlBuilder.append("ORDER BY ");
            for (Sort.Order order : pageable.getSort()) {
                jpqlBuilder.append("a.").append(order.getProperty()).append(" ").append(order.getDirection()).append(", ");
            }
            jpqlBuilder.setLength(jpqlBuilder.length() - 2);
        }


        TypedQuery<Announcement> query = entityManager.createQuery(jpqlBuilder.toString(), Announcement.class);
        TypedQuery<Long> countQuery = entityManager.createQuery(countQueryBuilder.toString(), Long.class);
        query.setParameter("userId", UserContextHolder.getUserContext().getUserId());
        countQuery.setParameter("userId", UserContextHolder.getUserContext().getUserId());

        if (announcementTitle != null && !announcementTitle.isEmpty()) {
            query.setParameter("announcementTitle", "%" + announcementTitle.toLowerCase() + "%");
            countQuery.setParameter("announcementTitle", "%" + announcementTitle.toLowerCase() + "%");
        }

        if (status != null) {
            AnnouncementStatus announcementStatus = AnnouncementStatus.values()[status];
            query.setParameter("status", announcementStatus);
            countQuery.setParameter("status", announcementStatus);
        }

        if(from != null) {
            query.setParameter("from", from);
            countQuery.setParameter("from", from);
        }

        if(to != null) {
            query.setParameter("to", to);
            countQuery.setParameter("to", to);
        }

        if(min != null) {
            query.setParameter("min", min);
            countQuery.setParameter("min", min);
        }

        if(max != null) {
            query.setParameter("max", max);
            countQuery.setParameter("max", max);
        }

        query.setFirstResult((int) pageable.getOffset());
        query.setMaxResults(pageable.getPageSize());

        List<Announcement> resultList = query.getResultList();
        long total = countQuery.getSingleResult();

        Page<Announcement> announcements = new PageImpl<>(resultList, pageable, total);
        return mapPageToVOs(announcements);
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
}
