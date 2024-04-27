package com.licenta.service.dto;

public class UpdateUserPointsDTO {
    private Long id;
    private Double pointsToWithdraw;
    private Double currentSold;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getPointsToWithdraw() {
        return pointsToWithdraw;
    }

    public void setPointsToWithdraw(Double pointsToWithdraw) {
        this.pointsToWithdraw = pointsToWithdraw;
    }

    public Double getCurrentSold() {
        return currentSold;
    }

    public void setCurrentSold(Double currentSold) {
        this.currentSold = currentSold;
    }
}
