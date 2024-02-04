package com.synergisticit.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synergisticit.domain.Account;
import com.synergisticit.domain.BankTransaction;
import com.synergisticit.domain.TransactionType;
import com.synergisticit.repository.BankTransactionRepository;

@Service
public class BankTransactionServiceImp implements BankTransactionService {

	@Autowired BankTransactionRepository bankTransactionRepository;
	@Autowired AccountService accountService;
	
	@Override
	public BankTransaction saveTransaction(BankTransaction bankTransaction) {
		if (bankTransaction.getBankTransactionType() == TransactionType.DEPOSIT) {
			Account dbAccountTo = accountService.findById(bankTransaction.getBankTransactionToAccount());
			dbAccountTo.setAccountBalance(dbAccountTo.getAccountBalance() + bankTransaction.getBankTransactionAmount());
		}
		if (bankTransaction.getBankTransactionType() == TransactionType.WITHDRAWAL) {
			Account dbAccountFrom = accountService.findById(bankTransaction.getBankTransactionFromAccount());
			dbAccountFrom.setAccountBalance(dbAccountFrom.getAccountBalance() - bankTransaction.getBankTransactionAmount());
		}
		if (bankTransaction.getBankTransactionType() == TransactionType.TRANSFER) {
			Account dbAccountFrom = accountService.findById(bankTransaction.getBankTransactionFromAccount());
			Account dbAccountTo = accountService.findById(bankTransaction.getBankTransactionToAccount());
			dbAccountFrom.setAccountBalance(dbAccountFrom.getAccountBalance() - bankTransaction.getBankTransactionAmount());
			dbAccountTo.setAccountBalance(dbAccountTo.getAccountBalance() + bankTransaction.getBankTransactionAmount());
			accountService.saveAccount(dbAccountFrom);
			accountService.saveAccount(dbAccountTo);
		}
//		if (bankTransaction.getBankTransactionType() == TransactionType.NEW_ACCOUNT) {
//			
//		}
		
		return bankTransactionRepository.save(bankTransaction);
	}

	@Override
	public List<BankTransaction> findAll() {
		return bankTransactionRepository.findAll();
	}

	@Override
	public BankTransaction findById(Long bankTransactionId) {
		return bankTransactionRepository.findById(bankTransactionId).orElse(null);
	}

	@Override
	public List<BankTransaction> findBybankTransactionByAccountId(List<Long> accountIds) {
		List<BankTransaction> transactions = bankTransactionRepository.findBybankTransactionFromAccountIn(accountIds);
		for (BankTransaction bt : bankTransactionRepository.findBybankTransactionToAccountIn(accountIds)) {
			if (!transactions.contains(bt)) {
				transactions.add(bt);
			}
		}
		return transactions;
	}

	@Override
	public List<BankTransaction> findBybankTransactionDateTimeBetween(LocalDateTime fromDate, LocalDateTime toDate) {
		return bankTransactionRepository.findBybankTransactionDateTimeBetween(fromDate, toDate);
	}

}
