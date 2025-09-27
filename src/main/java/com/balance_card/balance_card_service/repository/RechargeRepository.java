package com.balance_card.balance_card_service.repository;

import com.balance_card.balance_card_service.entity.Recharge;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface RechargeRepository extends ReactiveCrudRepository<Recharge, Long> {
}
