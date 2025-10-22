package com.balance_card.balance_card_service.service;

import com.balance_card.balance_card_service.entity.Card;
import com.balance_card.balance_card_service.repository.CardRepository;
import com.balance_card.balance_card_service.repository.RechargeRepository;
import com.balance_card.balance_card_service.repository.UsageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
public class CardServiceImplTest {


    @Mock
    private CardRepository cardRepository;

    @Mock
    private RechargeRepository rechargeRepository;

    @Mock
    private UsageRepository usageRepository;

    private CardServiceImpl cardService;

    @BeforeEach
    void setUp() {
        cardService = new CardServiceImpl(cardRepository, rechargeRepository, usageRepository);
    }

    @Test
    void findAll_ShouldReturnCardsFromRepository_WhenCacheIsEmpty() {
        // Arrange
        Card card1 = new Card();
        card1.setId(1L);
        Card card2 = new Card();
        card2.setId(2L);
        Card card3 = new Card();
        card3.setId(3L);
        card1.setType("METRO");
        card2.setType("CORREDOR");
        card3.setType("METRO");
        card1.setBalance(new BigDecimal("50.00"));
        card2.setBalance(new BigDecimal("30.00"));
        card3.setBalance(new BigDecimal("20.00"));
        card1.setFare(new BigDecimal("2.50"));
        card2.setFare(new BigDecimal("2.50"));
        card3.setFare(new BigDecimal("2.50"));
        card1.setStatus("GOOD");
        card2.setStatus("GOOD");
        card3.setStatus("GOOD");
        card1.setCreatedAt(LocalDateTime.now());
        card2.setCreatedAt(LocalDateTime.now());
        card3.setCreatedAt(LocalDateTime.now());
        card1.setUpdatedAt(LocalDateTime.now());
        card2.setUpdatedAt(LocalDateTime.now());
        card3.setUpdatedAt(LocalDateTime.now());

        when(cardRepository.findAll()).thenReturn(Flux.just(card1, card2, card3));

        // Act & Assert
        StepVerifier.create(cardService.findAll())
                .expectNext(card1)
                .expectNext(card2)
                .expectNext(card3)
                .verifyComplete();

        // Verify repository was called once
        verify(cardRepository, times(1)).findAll();
    }

    @Test
    void findAll_ShouldUseCachedData_WhenCalledMultipleTimes() {
        // Arrange
        Card card1 = new Card();
        card1.setId(1L);
        Card card2 = new Card();
        card2.setId(2L);
        card1.setType("METRO");
        card2.setType("CORREDOR");
        card1.setBalance(new BigDecimal("50.00"));
        card2.setBalance(new BigDecimal("30.00"));
        card1.setFare(new BigDecimal("2.50"));
        card2.setFare(new BigDecimal("2.50"));
        card1.setStatus("GOOD");
        card2.setStatus("GOOD");
        card1.setCreatedAt(LocalDateTime.now());
        card2.setCreatedAt(LocalDateTime.now());
        card1.setUpdatedAt(LocalDateTime.now());
        card2.setUpdatedAt(LocalDateTime.now());


        when(cardRepository.findAll()).thenReturn(Flux.just(card1, card2));

        // Stepverifier se utiliza ya que como los publishers son asíncronos, necesitamos una forma de verificar de subscripción y emisión de elementos.
        // Aquí StepVerifier simula la suscripción al Flux retornado por findAll() y verifica que los elementos emitidos son los esperados.


        // Act - Primera llamada
        StepVerifier.create(cardService.findAll())
                .expectNext(card1)
                .expectNext(card2)
                .verifyComplete(); // Verifica que el flujo se completa correctamente después de emitir los elementos esperados.

        // Act - Segunda llamada (debe usar cache)
        StepVerifier.create(cardService.findAll())
                .expectNext(card1)
                .expectNext(card2)
                .verifyComplete();

        // Act - Tercera llamada (debe usar cache)
        StepVerifier.create(cardService.findAll())
                .expectNext(card1)
                .expectNext(card2)
                .verifyComplete();

        // Assert - El repositorio solo debe haber sido llamado una vez
        verify(cardRepository, times(1)).findAll();
    }


}
