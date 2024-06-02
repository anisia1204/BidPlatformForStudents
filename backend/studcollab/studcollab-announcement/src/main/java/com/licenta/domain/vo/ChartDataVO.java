package com.licenta.domain.vo;

public class ChartDataVO {
    private CreatedAnnouncementsVO createdAnnouncementsVO;
    private SoldAnnouncementsVO soldAnnouncementsVO;

    public ChartDataVO(CreatedAnnouncementsVO createdAnnouncementsVO, SoldAnnouncementsVO soldAnnouncementsVO) {
        this.createdAnnouncementsVO = createdAnnouncementsVO;
        this.soldAnnouncementsVO = soldAnnouncementsVO;
    }

    public CreatedAnnouncementsVO getCreatedAnnouncementsVO() {
        return createdAnnouncementsVO;
    }

    public SoldAnnouncementsVO getSoldAnnouncementsVO() {
        return soldAnnouncementsVO;
    }
}
