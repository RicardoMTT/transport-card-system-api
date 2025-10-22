package com.balance_card.balance_card_service.repository;

import com.balance_card.balance_card_service.entity.Usage;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

public interface UsageRepository extends ReactiveCrudRepository<Usage, Long> {

    Flux<Usage> findByCardId(Long cardId);
    // Buscar usos por tarjeta y rango de fechas
    @Query("SELECT * FROM usages WHERE card_id = :cardId AND created_at BETWEEN :startDate AND :endDate")
    Flux<Usage> findByCardIdAndDateRange(Long cardId, LocalDateTime startDate, LocalDateTime endDate);

}
