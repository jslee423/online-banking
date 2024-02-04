package com.synergisticit.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.synergisticit.domain.Account;
import com.synergisticit.service.AccountService;
import com.synergisticit.validation.AccountValidator;

import jakarta.validation.Valid;

@Secured({"DBA", "Admin"})
@RestController
@RequestMapping("accounts")
public class AccountRestController {

	@Autowired AccountService accountService;
	@Autowired AccountValidator accountValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(accountValidator);
	}
	
	@GetMapping(value = "findAll")
	public ResponseEntity<List<Account>> findAll() {
		System.out.println("AccountRestController.findAll()...");
		List<Account> accounts = accountService.findAll();
		if (accounts.isEmpty()) {
			//###
			return new ResponseEntity<List<Account>>(accounts, HttpStatus.NOT_FOUND);
		} else {
			//###
			return new ResponseEntity<List<Account>>(accounts, HttpStatus.FOUND);
		}
	} 
	
	@GetMapping(value = "findById", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Account> findById(@RequestParam Long accountId) {
		System.out.println("AccountRestController.findById()...");
		Account account = accountService.findById(accountId);
		if (account == null) {
			return new ResponseEntity<Account>(account, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Account>(account, HttpStatus.FOUND);
		}
	} 
	
	@PostMapping(value = "save", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveCustomer(@Valid @RequestBody Account account, BindingResult br) {
		System.out.println("AccountRestController.saveCustomer()...");
		HttpHeaders headers = new HttpHeaders();
		StringBuilder errors = new StringBuilder("");
		Account foundAccount = accountService.findById(account.getAccountId());
		
		if (foundAccount != null || br.hasFieldErrors()) {
			if (br.hasFieldErrors()) {
				List<FieldError> fieldErrors = br.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					System.out.println("errors: " + fieldError.getDefaultMessage());
					errors = errors.append("\"" + fieldError.getField() + "\":" + fieldError.getDefaultMessage());
				}
				headers.add("errors", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			} else {
				errors.append("Account with id: " + account.getAccountId() + " already exists.");
				headers.add("Existing account", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			}
		} else {
			Account newAccount = accountService.saveAccount(account);
			headers.add("New Account", newAccount.getAccountType().toString());
			return new ResponseEntity<Account>(newAccount, headers, HttpStatus.CREATED);
		}
	}
	
	@PutMapping(value = "update", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCustomer(@Valid @RequestBody Account account, BindingResult br) {
		System.out.println("AccountRestController.updateCustomer()...");
		HttpHeaders headers = new HttpHeaders();
		StringBuilder errors = new StringBuilder("");
		Account foundAccount = accountService.findById(account.getAccountId());
		
		if (foundAccount == null || br.hasFieldErrors()) {
			if (br.hasFieldErrors()) {
				List<FieldError> fieldErrors = br.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					System.out.println("errors: " + fieldError.getDefaultMessage());
					errors = errors.append("\"" + fieldError.getField() + "\":" + fieldError.getDefaultMessage());
				}
				headers.add("errors", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			} else {
				errors.append("No account with id: " + account.getAccountId());
				headers.add("No account exists", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.NOT_FOUND);
			}
		} else {
			accountService.saveAccount(account);
			return new ResponseEntity<Account>(account, HttpStatus.OK);
		}
	}
	
	@DeleteMapping(value = "delete")
	public ResponseEntity<Account> delete(@RequestParam Long accountId) {
		System.out.println("AccountRestController.delete()...");
		HttpHeaders headers = new HttpHeaders();
		Account account = accountService.findById(accountId);
		
		if (account == null) {
			return new ResponseEntity<Account>(HttpStatus.NOT_FOUND);
		} else {
			accountService.deleteById(accountId);
			headers.add("Account deleted", String.valueOf(accountId));
			return new ResponseEntity<Account>(account, headers, HttpStatus.ACCEPTED);
		}
	}
}
