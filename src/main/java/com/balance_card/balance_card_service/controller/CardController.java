package com.balance_card.balance_card_service.controller;

import com.balance_card.balance_card_service.entity.Card;
import com.balance_card.balance_card_service.entity.CardHistoryDTO;
import com.balance_card.balance_card_service.entity.Recharge;
import com.balance_card.balance_card_service.service.CardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/cards")
public class CardController {

    private final CardService cardService;


    public CardController(CardService cardService) {
        this.cardService = cardService;
    }


    @GetMapping
    public Flux<Card> all() {
        return cardService.findAll();
    }


    @GetMapping("/{id}")
    public Mono<ResponseEntity<Card>> getOne(@PathVariable Long id) {
        return cardService.findById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }


    @PostMapping
    public Mono<Card> create(@RequestBody Card card) {
        return cardService.create(card);
    }

    // Api usada para registrar una recarga en el card
    @PostMapping("/{id}/recharges")
    public Mono<Card> recharge(@PathVariable Long id, @RequestBody java.util.Map<String, Object> body) {
        BigDecimal amount = new BigDecimal(body.get("amount").toString());
        return cardService.recharge(id, amount);
    }


    // Api usada para registrar una cantidad de usos del card
    // El body debe contener un campo "passengers" con el numero de pasajeros que usan
    @PostMapping("/{id}/usages")
    public Mono<Card> use(@PathVariable Long id, @RequestBody java.util.Map<String, Object> body) {
        int passengers = Integer.parseInt(body.get("passengers").toString());
        return cardService.use(id, passengers);
    }

    // Obtener el historial de recargas de un card
    @GetMapping("/{id}/recharges")
    public Flux<Recharge> getRecharges(@PathVariable Long id) {
        return cardService.getRecharges(id);
    }

    // Obtener el historial de recargas de un card
    @GetMapping("/{id}/history")
    public Flux<CardHistoryDTO> getHistory(@PathVariable Long id) {
        return cardService.getCardHistory(id);
    }

}
