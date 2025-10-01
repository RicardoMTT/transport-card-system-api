package com.balance_card.balance_card_service.repository;

import com.balance_card.balance_card_service.entity.Recharge;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface RechargeRepository extends ReactiveCrudRepository<Recharge, Long> {
    Flux<Recharge> findByCardId(Long cardId);
}
