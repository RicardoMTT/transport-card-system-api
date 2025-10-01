package com.balance_card.balance_card_service.repository;

import com.balance_card.balance_card_service.entity.Usage;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface UsageRepository extends ReactiveCrudRepository<Usage, Long> {

    Flux<Usage> findByCardId(Long cardId);
}
