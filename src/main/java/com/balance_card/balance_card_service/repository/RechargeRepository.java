package com.balance_card.balance_card_service.repository;

import com.balance_card.balance_card_service.entity.Recharge;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface RechargeRepository extends ReactiveCrudRepository<Recharge, Long> {
    Flux<Recharge> findByCardId(Long cardId);

    // Buscar recargas por tarjeta y rango de fechas
    @Query("SELECT * FROM recharges WHERE card_id = :cardId AND created_at BETWEEN :startDate AND :endDate")
    Flux<Recharge> findByCardIdAndDateRange(Long cardId, LocalDateTime startDate, LocalDateTime endDate);

}
