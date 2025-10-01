package com.balance_card.balance_card_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class BalanceCardServiceApplication {

	public static void main(String[] args) {

		/*
		Dada una lista L de temperaturas en K días, devuelva otra lista M
		con el número de días que uno debe esperar hasta que venga un día más cálido.
		Si no hay más días cálidos, devolver -1

		Ejemplo
		k = 8
		L = 27 - 29 - 26 - 30 - 30 - 25 - 33 - 21
		M = 1  - 2  - 1  - 3  - 2  - 1  - (-1) - (-1)
		* */
//		List<Integer> L = List.of(27,29,26,30,30,25,33,21);
//		List<Integer> M = new ArrayList<>();
//
//		for (var i=0; i< L.size() ; i++){ // 6
//			Integer temperature = L.get(i); // 33
//			var count = 0;
//
//			for (var j=i+1 ; j< L.size() ; j++){
//				Integer temperature2 = L.get(j); // 21
//				if (j == L.size() -1 ){
//					M.add(-1);
//				}
//				if (temperature<temperature2){
//					count = count + 1; // 1 2 1 3 2
//					M.add(count);
//					break;
//				}else{
//					count = count + 1; // 1
//				}
//				if (j == L.size() - 1){
//					M.add(-1);
//				}
//			}
//		}
//
//		M.forEach(System.out::println);
		SpringApplication.run(BalanceCardServiceApplication.class, args);

	}

}
