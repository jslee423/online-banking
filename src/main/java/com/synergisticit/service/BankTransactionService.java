package com.synergisticit.service;

import java.time.LocalDateTime;
import java.util.List;

import com.synergisticit.domain.BankTransaction;

public interface BankTransactionService {

	public BankTransaction saveTransaction(BankTransaction bankTransaction);
	
	public List<BankTransaction> findAll();
	
	public BankTransaction findById(Long bankTransactionId);
	
	List<BankTransaction> findBybankTransactionByAccountId(List<Long> accountIds);
	
	List<BankTransaction> findBybankTransactionDateTimeBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
