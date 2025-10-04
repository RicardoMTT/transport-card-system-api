package com.balance_card.balance_card_service.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MontoInvalidoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Mono<ResponseEntity<Map<String, Object>>> handleMontoInvalidoException(MontoInvalidoException ex) {
        return createErrorResponse(HttpStatus.BAD_REQUEST, ex.getMessage());
    }


    private Mono<ResponseEntity<Map<String, Object>>> createErrorResponse(
            HttpStatus status,
            String message,
            Object... details) {

        Map<String, Object> response = new HashMap<>();
        response.put("status", status.value());
        response.put("error", status.getReasonPhrase());
        response.put("message", message);

        if (details.length > 0) {
            response.put("details", details[0]);
        }

        return Mono.just(ResponseEntity.status(status).body(response));
    }
}
