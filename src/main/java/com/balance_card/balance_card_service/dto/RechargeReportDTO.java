package com.balance_card.balance_card_service.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class RechargeReportDTO {

    private Long cardId;
    private BigDecimal totalAmount;
    private Long totalRecharges;
    private Integer month;
    private Integer year;

    public RechargeReportDTO() {
    }

    public RechargeReportDTO(Long cardId, BigDecimal totalAmount, Long totalRecharges, Integer month, Integer year) {
        this.cardId = cardId;
        this.totalAmount = totalAmount;
        this.totalRecharges = totalRecharges;
        this.month = month;
        this.year = year;
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
}
