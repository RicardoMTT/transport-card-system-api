package com.balance_card.balance_card_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class BalanceCardServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BalanceCardServiceApplication.class, args);
	}

}
