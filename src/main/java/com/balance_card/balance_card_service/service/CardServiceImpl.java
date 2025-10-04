package com.balance_card.balance_card_service.service;

import com.balance_card.balance_card_service.dto.RechargeReportDTO;
import com.balance_card.balance_card_service.dto.UsageReportDTO;
import com.balance_card.balance_card_service.entity.Card;
import com.balance_card.balance_card_service.entity.CardHistoryDTO;
import com.balance_card.balance_card_service.entity.Recharge;
import com.balance_card.balance_card_service.entity.Usage;
import com.balance_card.balance_card_service.exception.MontoInvalidoException;
import com.balance_card.balance_card_service.repository.CardRepository;
import com.balance_card.balance_card_service.repository.RechargeRepository;
import com.balance_card.balance_card_service.repository.UsageRepository;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Comparator;

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

    // Valida si la cache ha expirado , comparando el tiempo actual con el tiempo de la ultima cache
    private boolean isExpired() {
        return lastCacheTime == null ||
                Duration.between(lastCacheTime, Instant.now()).compareTo(CACHE_DURATION) > 0;
    }
    
    private void invalidateCache() {
        this.cachedCards = null;
        this.lastCacheTime = null;
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


    /*
    * Cada vez que se realice una recarga o un uso de la tarjeta,
    * la caché se invalidará automáticamente. La próxima llamada a
    * findAll() obtendrá los datos más recientes de la base de datos.
    * */

    // Usado cada vez que se hace una recarga en el card
    // Suma al balance del card el monto de la recarga
    // Crea un registro en la tabla recharge con el id del card, monto de la recarga y fecha de creacion
    // Retorna el card actualizado
    @Override
    public Mono<Card> recharge(Long id, BigDecimal amount) {
        // Si el monto es menor o igual a cero, lanza una excepción
        if (amount.compareTo(BigDecimal.ZERO) <= 0){
            return Mono.error(new MontoInvalidoException("El monto debe ser mayor a cero"));
        }
        return cardRepository.findById(id)
                .flatMap(card -> {
                    card.setBalance(card.getBalance().add(amount));
                    card.setUpdatedAt(LocalDateTime.now());
                    Recharge r = new Recharge();
                    r.setCardId(card.getId());
                    r.setAmount(amount);
                    r.setCreatedAt(LocalDateTime.now());
                    return rechargeRepository.save(r)
                            .then(cardRepository.save(card))
                            .doOnSuccess(c -> invalidateCache()); // Invalida la caché después de guardar
                });
    }

    public void leerArchivo() throws FileNotFoundException {
        FileReader file = new FileReader("archivo.txt");
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
                            .then(cardRepository.save(card))
                            .doOnSuccess(c -> invalidateCache()); // Invalida la caché después de guardar
                });
    }

    @Override
    public Flux<Recharge> getRecharges(Long id) {
        return rechargeRepository.findByCardId(id);
    }


    @Override
    public Flux<CardHistoryDTO> getCardHistory(Long cardId) {
        // Obtener el card para acceder al tipo de transporte
        Mono<Card> cardMono = cardRepository.findById(cardId);

        // Obtener recargas
        Flux<CardHistoryDTO> recharges = rechargeRepository.findByCardId(cardId)
                .flatMap(recharge -> cardMono.map(card ->
                        CardHistoryDTO.fromRecharge(recharge, card.getType())
                ));

        // Obtener usos
        Flux<CardHistoryDTO> usages = usageRepository.findByCardId(cardId)
                .flatMap(usage -> cardMono.map(card ->
                                CardHistoryDTO.fromUsage(usage, card.getType())));
        // Combinar y ordenar por fecha descendente
        return Flux.merge(recharges, usages)
                .sort(Comparator.comparing(CardHistoryDTO::getDate).reversed());
    }



    @Override
    public Mono<RechargeReportDTO> getRechargeReport(Long cardId, LocalDateTime startDate, LocalDateTime endDate) {
        return rechargeRepository.findByCardIdAndDateRange(cardId, startDate, endDate)
                .collectList()
                .map(recharges -> {
                    BigDecimal totalAmount = recharges.stream()
                            .map(Recharge::getAmount)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new RechargeReportDTO(
                            cardId,
                            totalAmount,
                            (long) recharges.size(),
                            startDate,
                            endDate
                    );
                });
    }

    @Override
    public Mono<UsageReportDTO> getUsageReportByMonth(Long cardId, int month, int year) {
        // Calcular el rango de fechas del mes
        LocalDateTime startDate = LocalDateTime.of(year, month, 1, 0, 0);
        LocalDateTime endDate = startDate.plusMonths(1).minusSeconds(1); 

        return usageRepository.findByCardIdAndDateRange(cardId, startDate, endDate)
                .collectList()
                .map(usages -> {
                    BigDecimal totalSpent = usages.stream()
                            .map(Usage::getTotalFare)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);

                    return new UsageReportDTO(
                            cardId,
                            month,
                            year,
                            (long) usages.size(),
                            totalSpent
                    );
                });
    }
}
