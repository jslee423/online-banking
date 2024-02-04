package com.synergisticit.controller;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.synergisticit.domain.BankTransaction;
import com.synergisticit.service.AccountService;
import com.synergisticit.service.BankTransactionService;
import com.synergisticit.service.UserService;

@Controller
public class HomeController {
	
	@Autowired AccountService accountService;
	@Autowired UserService userService;
	@Autowired BankTransactionService bankTransactionService;
	
	@RequestMapping("/")
	public ModelAndView home(Principal principal) {
		System.out.println("HomeController.home()...");
		
		ModelAndView mav = new ModelAndView("home");
		mav.addObject("accounts", accountService.findAll());
		if (principal != null) {
			mav.addObject("loggedInUser", userService.findUserByUsername(principal.getName()).getUsername());
			List<Long> userAccountIds = accountService
					.findByAccountCustomer(userService.findUserByUsername(principal.getName()).getUserId())
					.stream().map(account -> account.getAccountId()).collect(Collectors.toList());
			List<BankTransaction> userTransactions = bankTransactionService.findBybankTransactionByAccountId(userAccountIds);
			mav.addObject("userTransactions", userTransactions);
		}
		
		return mav;
	}
}
