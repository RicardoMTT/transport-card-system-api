package com.balance_card.balance_card_service.service;

import com.balance_card.balance_card_service.entity.Card;
import com.balance_card.balance_card_service.entity.Recharge;
import com.balance_card.balance_card_service.entity.Usage;
import com.balance_card.balance_card_service.repository.CardRepository;
import com.balance_card.balance_card_service.repository.RechargeRepository;
import com.balance_card.balance_card_service.repository.UsageRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;

@Service
@EnableCaching
public class CardServiceImpl implements CardService{

    private final CardRepository cardRepository;
    private final RechargeRepository rechargeRepository;
    private final UsageRepository usageRepository;

    private Flux<Card> cachedCards;
    private Instant lastCacheTime;
    private final Duration CACHE_DURATION = Duration.ofMinutes(15);
    public CardServiceImpl(CardRepository cardRepository, RechargeRepository rechargeRepository, UsageRepository usageRepository) {
        this.cardRepository = cardRepository;
        this.rechargeRepository = rechargeRepository;
        this.usageRepository = usageRepository;
    }


    @Override
    public Flux<Card> findAll() {
        // Si no hay cache o expiró, crear nuevo
        if (cachedCards == null || isExpired()) {
            cachedCards = cardRepository.findAll()
                    .doOnNext(card -> System.out.println("Cargando card id: " + card.getId()))
                    .cache(); // Cache hasta que se complete
            lastCacheTime = Instant.now();
        }
        return cachedCards;
    }

    private boolean isExpired() {
        return lastCacheTime == null ||
                Duration.between(lastCacheTime, Instant.now()).compareTo(CACHE_DURATION) > 0;
    }
    @Override
    public Mono<Card> findById(Long id) {
        return cardRepository.findById(id);
    }


    @Override
    public Mono<Card> create(Card card) {
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(LocalDateTime.now());
        return cardRepository.save(card);
    }



    // Usado cada vez que se hace una recarga en el card
    // Suma al balance del card el monto de la recarga
    // Crea un registro en la tabla recharge con el id del card, monto de la recarga y fecha de creacion
    // Retorna el card actualizado
    @Override
    public Mono<Card> recharge(Long id, BigDecimal amount) {
        return cardRepository.findById(id)
                .flatMap(card -> {
                    card.setBalance(card.getBalance().add(amount));
                    card.setUpdatedAt(LocalDateTime.now());
                    Recharge r = new Recharge();
                    r.setCardId(card.getId());
                    r.setAmount(amount);
                    r.setCreatedAt(LocalDateTime.now());
                    return rechargeRepository.save(r)
                            .then(cardRepository.save(card));
                });
    }



    // Usado cada vez que se usa el card para pagar un viaje
    // Resta del balance del card el total del viaje (fare * passengers)
    // Crea un registro en la tabla usage con el id del card, numero de pasajeros, total del viaje y fecha de creacion
    // Si el balance es insuficiente, lanza un error
    @Override
    public Mono<Card> use(Long id, int passengers) {
        return cardRepository.findById(id)
                .flatMap(card -> {
                    BigDecimal total = card.getFare().multiply(new BigDecimal(passengers));
                    if (card.getBalance().compareTo(total) < 0) {
                        return Mono.error(new RuntimeException("Saldo insuficiente"));
                    }
                    BigDecimal newBalance = card.getBalance().subtract(total);
                    card.setBalance(newBalance);
                    
                    // Set status based on balance
                    if (newBalance.compareTo(new BigDecimal(5)) <= 0) {
                        card.setStatus("LOW");
                    } else if (newBalance.compareTo(new BigDecimal(15)) <= 0) {
                        card.setStatus("MEDIUM");
                    } else {
                        card.setStatus("GOOD");
                    }
                    
                    card.setUpdatedAt(LocalDateTime.now());
                    Usage u = new Usage();
                    u.setCardId(card.getId());
                    u.setPassengers(passengers);
                    u.setTotalFare(total);
                    u.setCreatedAt(LocalDateTime.now());
                    return usageRepository.save(u)
                            .then(cardRepository.save(card));
                });
    }
}
