package com.balance_card.balance_card_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RechargeReportDTO {

    private Long cardId;
    private BigDecimal totalAmount;
    private Long totalRecharges;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    public RechargeReportDTO() {
    }

    public RechargeReportDTO(Long cardId, BigDecimal totalAmount, Long totalRecharges, LocalDateTime startDate, LocalDateTime endDate) {
        this.cardId = cardId;
        this.totalAmount = totalAmount;
        this.totalRecharges = totalRecharges;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Long getTotalRecharges() {
        return totalRecharges;
    }

    public void setTotalRecharges(Long totalRecharges) {
        this.totalRecharges = totalRecharges;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }
}
