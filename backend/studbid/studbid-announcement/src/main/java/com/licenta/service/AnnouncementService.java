package com.licenta.service;

import com.licenta.domain.Announcement;
import com.licenta.domain.vo.AnnouncementVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AnnouncementService {
    Page<AnnouncementVO> getMyAnnouncements(Pageable pageable);

    void delete(Long id);
    Announcement getById(Long id);

    Announcement markAsSold(Long announcementId);
}
