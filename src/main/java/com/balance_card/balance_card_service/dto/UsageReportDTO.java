package com.balance_card.balance_card_service.dto;

import java.math.BigDecimal;

public class UsageReportDTO {

    private Long cardId;
    private Integer month;
    private Integer year;
    private Long totalTrips;
    private BigDecimal totalSpent;

    public UsageReportDTO() {
    }

    public UsageReportDTO(Long cardId, Integer month, Integer year, Long totalTrips, BigDecimal totalSpent) {
        this.cardId = cardId;
        this.month = month;
        this.year = year;
        this.totalTrips = totalTrips;
        this.totalSpent = totalSpent;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public Integer getMonth() {
        return month;
    }

    public void setMonth(Integer month) {
        this.month = month;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Long getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(Long totalTrips) {
        this.totalTrips = totalTrips;
    }

    public BigDecimal getTotalSpent() {
        return totalSpent;
    }

    public void setTotalSpent(BigDecimal totalSpent) {
        this.totalSpent = totalSpent;
    }
}
