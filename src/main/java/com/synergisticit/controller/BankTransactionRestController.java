package com.synergisticit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synergisticit.domain.BankTransaction;
import com.synergisticit.service.BankTransactionService;

@RestController
@RequestMapping("bankTransactions")
public class BankTransactionRestController {

	@Autowired BankTransactionService bankTransactionService;
	
	@GetMapping(value = "findAll")
	public ResponseEntity<List<BankTransaction>> findAll() {
		System.out.println("BankTransactionRestController.findAll()...");
		List<BankTransaction> bankTransactions = bankTransactionService.findAll();
		if (bankTransactions.isEmpty()) {
			//###
			return new ResponseEntity<List<BankTransaction>>(bankTransactions, HttpStatus.NOT_FOUND);
		} else {
			//###
			return new ResponseEntity<List<BankTransaction>>(bankTransactions, HttpStatus.FOUND);
		}
	} 
	
	@GetMapping(value = "findById", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<BankTransaction> findById(@RequestParam Long bankTransactionId) {
		System.out.println("BankTransactionRestController.findById()...");
		BankTransaction bankTransaction = bankTransactionService.findById(bankTransactionId);
		if (bankTransaction == null) {
			return new ResponseEntity<BankTransaction>(bankTransaction, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<BankTransaction>(bankTransaction, HttpStatus.FOUND);
		}
	} 
}
