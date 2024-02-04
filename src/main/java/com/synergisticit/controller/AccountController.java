package com.synergisticit.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.synergisticit.domain.Account;
import com.synergisticit.domain.AccountType;
import com.synergisticit.domain.BankTransaction;
import com.synergisticit.service.AccountService;
import com.synergisticit.service.BankTransactionService;
import com.synergisticit.service.BranchService;
import com.synergisticit.service.CustomerService;
import com.synergisticit.service.UserService;
import com.synergisticit.validation.AccountValidator;

import jakarta.validation.Valid;

@Controller
public class AccountController {
	
	@Autowired AccountService accountService;
	@Autowired BranchService branchService;
	@Autowired CustomerService customerService;
	@Autowired UserService userService;
	@Autowired BankTransactionService bankTransactionService;
	@Autowired AccountValidator accountValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(accountValidator);
	}
	
	@RequestMapping("accountForm")
	public ModelAndView accountForm(Account account, Principal principal) {
		System.out.println("AccountController.accountForm()...");
		
		ModelAndView mav = new ModelAndView("accountForm");
		mav.addObject("accounts", accountService.findAll());
		mav.addObject("accountTypes", AccountType.values());
		mav.addObject("branches", branchService.findAll());
		mav.addObject("customers", customerService.findAll());
		mav.addObject("loggedInUser", userService.findUserByUsername(principal.getName()).getUsername());
		mav.addObject("loggedInUserRoles", userService.findUserByUsername(principal.getName()).getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()));
		mav.addObject("activeAccount", "active");
		
		List<Long> userAccountIds = accountService
				.findByAccountCustomer(userService.findUserByUsername(principal.getName()).getUserId())
				.stream().map(acc -> acc.getAccountId()).collect(Collectors.toList());
		List<BankTransaction> userTransactions = bankTransactionService.findBybankTransactionByAccountId(userAccountIds);
		mav.addObject("userTransactions", userTransactions);
		
		return mav;
	}
	
	@RequestMapping("saveAccount")
	public ModelAndView saveAccount(@Valid @ModelAttribute Account account, BindingResult br) {
		System.out.println("AccountController.saveAccount()...");
		
		ModelAndView mav = new ModelAndView("accountForm");
		
		if (br.hasErrors()) {
			mav.addObject("accounts", accountService.findAll());
			mav.addObject("accountTypes", AccountType.values());
			mav.addObject("branches", branchService.findAll());
			mav.addObject("customers", customerService.findAll());
			
			return mav;
		} else {
			accountService.saveAccount(account);
			mav.addObject("accounts", accountService.findAll());
			mav.addObject("accountTypes", AccountType.values());
			mav.addObject("branches", branchService.findAll());
			mav.addObject("customers", customerService.findAll());
			mav.setViewName("redirect:accountForm");
			
			return mav;
		}
	}
	
	@RequestMapping("updateAccount")
	public ModelAndView updateAccount(Account account, Principal principal) {
		System.out.println("AccountController.updateAccount()...");
		
		account = accountService.findById(account.getAccountId());
		ModelAndView mav = new ModelAndView("accountForm");
		mav.addObject("accounts", accountService.findAll());
		mav.addObject("accountTypes", AccountType.values());
		mav.addObject("branches", branchService.findAll());
		mav.addObject("customers", customerService.findAll());
		mav.addObject("account", account);
		mav.addObject("currentAccountType", account.getAccountType());
		//mav.addObject("accountBranch", account.getAccountBranch());
		mav.addObject("loggedInUser", userService.findUserByUsername(principal.getName()).getUsername());
		mav.addObject("loggedInUserRoles", userService.findUserByUsername(principal.getName()).getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()));
		mav.addObject("activeAccount", "active");
		mav.addObject("updateBtn", true);
		
		return mav;
	}
	
	@RequestMapping("deleteAccount")
	public String deleteAccount(Account account) {
		System.out.println("AccountController.deleteAccount()...");
		
		accountService.deleteById(account.getAccountId());
		
		return "redirect:accountForm";
	}
}
