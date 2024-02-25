package com.licenta.service;

import com.licenta.domain.vo.AnnouncementVO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface AnnouncementService {
    Page<AnnouncementVO> getMyAnnouncements(Pageable pageable);
}
