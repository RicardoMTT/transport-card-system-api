package com.balance_card.balance_card_service.controller;

import com.balance_card.balance_card_service.entity.Card;
import com.balance_card.balance_card_service.service.CardService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import static org.mockito.Mockito.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class) // Indica que se utilizará Mockito para las pruebas
public class CardControllerTest {

    // Usado para dependencias que el componente bajo prueba necesita
    @Mock
    private CardService cardService;

    // Usado para crear una instancia real del componente que estas probando
    // pero con sus dependencias inyectadas como mocks
    @InjectMocks
    private CardController cardController;

    private Card testCard;

    @BeforeEach
    void setUp() {
        testCard = new Card();
        testCard.setId(1L);
        testCard.setType("METRO");
        testCard.setBalance(new BigDecimal("50.00"));
        testCard.setFare(new BigDecimal("2.50"));
        testCard.setStatus("GOOD");
        testCard.setCreatedAt(LocalDateTime.now());
        testCard.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void all_ShouldReturnAllCards() {
        // Given
        Card card = new Card();
        card.setId(2L);
        card.setType("CORREDOR");
        card.setBalance(new BigDecimal("30.00"));

        // Realizamos la configuracion del mock para que devuelva un Flux con las tarjetas ( en este caso 2 )
        when(cardService.findAll()).thenReturn(Flux.just(testCard, card));

        // When
        // Solo crea la definición del flujo, pero no lo ejecuta aún
        // Al llamar a verifyComplete(), se dispara la ejecución del Flux y se verifican las expectativas definidas anteriormente.
        Flux<Card> result = cardController.all();

        // Then
        StepVerifier.create(result) // Crea un StepVerifier para verificar el resultado
                // 3. Al llamar a verifyComplete(), se dispara la ejecución del Flux, el flux comienza a emitir elementos y se verifican las expectativas definidas anteriormente.
                .expectNextCount(2)
                .verifyComplete();

        verify(cardService, times(1)).findAll();

    }
    @Test
    void all_ShouldReturnEmptyFlux_WhenNoCardsAreFound() {
        // Given
        when(cardService.findAll()).thenReturn(Flux.empty());
        // When
        Flux<Card> result = cardController.all();
        // Then
        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();

        verify(cardService, times(1)).findAll();
    }

}
