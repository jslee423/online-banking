package com.synergisticit.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.synergisticit.domain.Account;
import com.synergisticit.domain.BankTransaction;
import com.synergisticit.domain.TransactionType;
import com.synergisticit.service.AccountService;
import com.synergisticit.service.BankTransactionService;
import com.synergisticit.service.CustomerService;
import com.synergisticit.service.UserService;
import com.synergisticit.validation.BankTransactionValidation;

import jakarta.validation.Valid;

@Controller
public class BankTransactionController {

	@Autowired BankTransactionService bankTransactionService;
	@Autowired AccountService accountService;
	@Autowired CustomerService customerService;
	@Autowired UserService userService;
	@Autowired BankTransactionValidation bankTransactionValidation;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(bankTransactionValidation);
	}
	
	@RequestMapping("bankTransferForm")
	public ModelAndView bankTransactionForm(BankTransaction bankTransaction, Principal principal) {
		System.out.println("BankTransactionController.bankTransactionForm()...");
		
		ModelAndView mav = new ModelAndView("bankTransferForm");
		getData(mav, principal);
		mav.addObject("activeTransaction", "active");
		
		return mav;
	}
	
	@RequestMapping("saveBankTransfer")
	public ModelAndView saveBankTransaction(@Valid @ModelAttribute BankTransaction bankTransaction, BindingResult br, Principal principal) {
		System.out.println("BankTransactionController.saveBankTransaction()...");
		
		ModelAndView mav = new ModelAndView("bankTransferForm");
		
		if (br.hasErrors()) {
			getData(mav, principal);
			mav.addObject("activeTransaction", "active");
			
			return mav;
		} else {
			BankTransaction savedTransaction = bankTransactionService.saveTransaction(bankTransaction);
			getData(mav, principal);
			//mav.setViewName("redirect:bankTransferForm");
			mav.addObject("savedTransaction", savedTransaction);
			if (savedTransaction.getBankTransactionFromAccount() != null) {
				Account fromAccount = accountService.findById(savedTransaction.getBankTransactionFromAccount());				
				mav.addObject("savedFrom", fromAccount.getAccountType());
			}
			if (savedTransaction.getBankTransactionToAccount() != null) {
				Account toAccount = accountService.findById(savedTransaction.getBankTransactionToAccount());
				mav.addObject("toFrom", toAccount.getAccountType());
				
			}
			mav.addObject("activeTransaction", "active");
			mav.setViewName("transactionSuccess");
			
			return mav;
		}
	}
	
//	@RequestMapping("transactionSuccess")
//	public ModelAndView transactionSuccess() {
//		System.out.println("BankTransactionController.transactionSuccess()...");
//		
//		ModelAndView mav = new ModelAndView("transactionSuccess");
//	}
	
	@RequestMapping("searchTransferForm")
	public ModelAndView searchTransferForm(Principal principal) {
		System.out.println("BankTransactionController.searchTransferForm()...");
		
		ModelAndView mav = new ModelAndView("searchTransferForm");
		getData(mav, principal);

		List<Long> userAccountIds = accountService
				.findByAccountCustomer(userService.findUserByUsername(principal.getName()).getUserId())
				.stream().map(account -> account.getAccountId()).collect(Collectors.toList());
		
		List<BankTransaction> userTransactions = bankTransactionService.findBybankTransactionByAccountId(userAccountIds);
		mav.addObject("userTransactions", userTransactions);
		mav.addObject("activeSearch", "active");
		
		return mav;
	}
	
	@RequestMapping(value = "searchTransaction", method = RequestMethod.GET)
	public ModelAndView searchTransaction(
			@RequestParam(value = "bankTransactionType", required = false) TransactionType transactionType,
			@RequestParam(value = "selectedAccount", required = false) Integer selectedAccount,
			@RequestParam("fromDate") String fromDate,
			@RequestParam("toDate") String toDate,
			Principal principal
	) {
		System.out.println("BankTransactionController.searchTransferForm()...");
		
		ModelAndView mav = new ModelAndView("searchTransferForm");
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		LocalDate ldFromDate = LocalDate.now();
		LocalDate ldToDate = LocalDate.now();
		
		if (!fromDate.isBlank() && !fromDate.isEmpty()) {
			ldFromDate = LocalDate.parse(fromDate, formatter);			
		}
		if (!toDate.isBlank() && !toDate.isEmpty()) {
			ldToDate = LocalDate.parse(toDate, formatter);
		}

		List<Long> userAccountIds = accountService
				.findByAccountCustomer(userService.findUserByUsername(principal.getName()).getUserId())
				.stream().map(account -> account.getAccountId()).collect(Collectors.toList());
		
		List<BankTransaction> userTransactions = bankTransactionService.findBybankTransactionByAccountId(userAccountIds);
		
		if (ldToDate.isEqual(ldFromDate) || ldToDate.isAfter(ldFromDate)) {
			List<BankTransaction> transactionsByTime = bankTransactionService
					.findBybankTransactionDateTimeBetween((LocalDateTime)ldFromDate.atTime(00,00,00), (LocalDateTime)ldToDate
							.atTime(23,59,59));
			List<BankTransaction> finalResults = new ArrayList<>();;
			for (BankTransaction bt : userTransactions) {
				if (transactionsByTime.contains(bt)) {
					finalResults.add(bt);
				}
			}
			
			if (transactionType != null) {
				finalResults = finalResults.stream().filter(transaction -> transaction.getBankTransactionType() == transactionType)
						.collect(Collectors.toList());
			} 
			if (selectedAccount != null) {
				finalResults = finalResults.stream().filter(transaction -> transaction.getBankTransactionFromAccount() == Long.valueOf(selectedAccount) || transaction.getBankTransactionToAccount() == Long.valueOf(selectedAccount))
						.collect(Collectors.toList());
			}
		
			mav.addObject("userTransactions", finalResults);
		} else {
			mav.addObject("dateInputError", "end date must be equal or greater than start date");
			mav.addObject("userTransactions", userTransactions);
		}
		
		
		mav.addObject("selectedType", transactionType);
		mav.addObject("selectedFromDate", fromDate);
		mav.addObject("selectedToDate", toDate);
		mav.addObject("selectedAccount", selectedAccount);
		getData(mav, principal);
		
		return mav;
	}
	
	public void getData(ModelAndView modelAndView, Principal principal) {
		modelAndView.addObject("accounts", accountService.findAll());
		modelAndView.addObject("bankTransfers", bankTransactionService.findAll());
		modelAndView.addObject("transactionTypes", TransactionType.values());
		modelAndView.addObject("loggedInUser", userService.findUserByUsername(principal.getName()).getUsername());
		List<Account> userAccounts = accountService
				.findByAccountCustomer(userService.findUserByUsername(principal.getName()).getUserId());
		modelAndView.addObject("userAccounts", userAccounts
				.stream().map(account -> account.getAccountId()).collect(Collectors.toList()));
	}
}
