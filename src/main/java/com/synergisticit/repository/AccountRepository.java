package com.synergisticit.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.synergisticit.domain.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {

	public List<Account> findByAccountCustomer_CustomerId(Long customerId);
}
