package com.licenta.service;

import com.licenta.domain.Announcement;
import com.licenta.domain.vo.AnnouncementVO;
import com.licenta.domain.vo.ChartDataVO;


public interface AnnouncementService {
    void delete(Long id);
    Announcement getById(Long id);

    Announcement markAsSold(Long announcementId);

    AnnouncementVO getDetails(Long id);

    ChartDataVO getChartData();
}
