package com.balance_card.balance_card_service.repository;

import com.balance_card.balance_card_service.entity.Card;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface CardRepository extends ReactiveCrudRepository<Card, Long> {
}
