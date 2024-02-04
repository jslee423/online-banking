package com.synergisticit.service;

import java.util.List;

import com.synergisticit.domain.Account;

public interface AccountService {
	
	public Account saveAccount(Account account);
	
	public List<Account> findAll();
	
	public Account findById(Long accountId);
	
	public void deleteById(Long accountId);
	
	public List<Account> findByAccountCustomer(Long customerId);
}
