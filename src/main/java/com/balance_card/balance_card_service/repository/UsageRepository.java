package com.balance_card.balance_card_service.repository;

import com.balance_card.balance_card_service.entity.Usage;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface UsageRepository extends ReactiveCrudRepository<Usage, Long> {
}
