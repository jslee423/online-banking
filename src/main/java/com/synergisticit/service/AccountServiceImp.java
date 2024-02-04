package com.synergisticit.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.synergisticit.domain.Account;
import com.synergisticit.repository.AccountRepository;

@Service
public class AccountServiceImp implements AccountService {
	
	@Autowired AccountRepository accountRepository;

	@Override
	public Account saveAccount(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public List<Account> findAll() {
		return accountRepository.findAll();
	}

	@Override
	public Account findById(Long accountId) {
		Optional<Account> optAccount = accountRepository.findById(accountId);
		if (optAccount.isPresent()) {
			return optAccount.get();
		}
		return null;
	}

	@Override
	public void deleteById(Long accountId) {
		accountRepository.deleteById(accountId);

	}

	@Override
	public List<Account> findByAccountCustomer(Long customerId) {
		return accountRepository.findByAccountCustomer_CustomerId(customerId);
	}

}
