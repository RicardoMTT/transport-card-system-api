package com.balance_card.balance_card_service.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CardHistoryDTO {

    private Long id;
    private String type; // "RECHARGE" o "USAGE"
    private BigDecimal amount;
    private LocalDateTime date;
    private String description;

    public CardHistoryDTO(){

    }
    public CardHistoryDTO(Long id, String type, BigDecimal amount, LocalDateTime date, String description) {

    }


    public static CardHistoryDTO fromRecharge(Recharge recharge, String transportType) {
        CardHistoryDTO dto = new CardHistoryDTO();
        dto.setId(recharge.getId());
        dto.setType("RECHARGE");
        dto.setAmount(recharge.getAmount());
        dto.setDate(recharge.getCreatedAt());
        dto.setDescription("Recarga de saldo - " + transportType);
        return dto;
    }

    public static CardHistoryDTO fromUsage(Usage usage, String transportType) {
        CardHistoryDTO dto = new CardHistoryDTO();
        dto.setId(usage.getId());
        dto.setType("USAGE");
        dto.setAmount(usage.getTotalFare().negate());
        dto.setDate(usage.getCreatedAt());
        dto.setDescription("Uso del " + transportType + " - " + usage.getPassengers() + " pasajeros");
        return dto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
