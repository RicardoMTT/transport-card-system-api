package com.balance_card.balance_card_service.service;

import com.balance_card.balance_card_service.dto.RechargeReportDTO;
import com.balance_card.balance_card_service.dto.UsageReportDTO;
import com.balance_card.balance_card_service.entity.Card;
import com.balance_card.balance_card_service.entity.CardHistoryDTO;
import com.balance_card.balance_card_service.entity.Recharge;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

public interface CardService {

    Flux<Card> findAll();
    Mono<Card> findById(Long id);
    Mono<Card> create(Card card);
    Mono<Card> recharge(Long id, java.math.BigDecimal amount);
    Mono<Card> use(Long id, int passengers);
    Flux<Recharge> getRecharges(Long id);
    Mono<RechargeReportDTO> getRechargeReportByMonth(Long cardId, int month, int year);
    Mono<UsageReportDTO> getUsageReportByMonth(Long cardId, int month, int year);
    Flux<CardHistoryDTO> getCardHistory(Long cardId); // Retorna un Flux de CardHistoryDTO que representa la historia de recargas y usos
}
