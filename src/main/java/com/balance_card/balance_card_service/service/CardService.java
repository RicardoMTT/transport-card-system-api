package com.balance_card.balance_card_service.service;

import com.balance_card.balance_card_service.entity.Card;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CardService {

    Flux<Card> findAll();
    Mono<Card> findById(Long id);
    Mono<Card> create(Card card);
    Mono<Card> recharge(Long id, java.math.BigDecimal amount);
    Mono<Card> use(Long id, int passengers);
}
