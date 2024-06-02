package com.licenta.service;

import com.licenta.domain.vo.AnnouncementVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

public interface AnnouncementFilterService {
    Page<AnnouncementVO> getMyAnnouncements(Pageable pageable, String announcementTitle, String announcementType, Integer status, Double min, Double max, LocalDateTime from, LocalDateTime to);

    Page<AnnouncementVO> getDashboardAnnouncements(Pageable pageable, String announcementTitle, String announcementType, Integer status, Double min, Double max, LocalDateTime from, LocalDateTime to);

    Page<AnnouncementVO> getFavoriteAnnouncements(Pageable pageable, String announcementTitle, String announcementType, Integer status, Double min, Double max, LocalDateTime from, LocalDateTime to);
}
