package com.synergisticit.repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synergisticit.domain.BankTransaction;

public interface BankTransactionRepository extends JpaRepository<BankTransaction, Long> {

	List<BankTransaction> findBybankTransactionFromAccountIn(List<Long> accountIds);
	List<BankTransaction> findBybankTransactionToAccountIn(List<Long> accountIds);
	List<BankTransaction> findBybankTransactionDateTimeBetween(LocalDateTime fromDate, LocalDateTime toDate);
}
