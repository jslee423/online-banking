package com.synergisticit.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.synergisticit.domain.BankTransaction;
import com.synergisticit.domain.TransactionType;
import com.synergisticit.service.AccountService;

@Component
public class BankTransactionValidation implements Validator {
	
	@Autowired AccountService accountService;

	@Override
	public boolean supports(Class<?> clazz) {
		return BankTransaction.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		BankTransaction bankTransaction = (BankTransaction)target;
		System.out.println("date: " + bankTransaction.getBankTransactionDateTime());
		System.out.println("from: " + bankTransaction.getBankTransactionFromAccount());
		System.out.println("to: " + bankTransaction.getBankTransactionToAccount());
		
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bankTransactionId", null);
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "bankTransactionAmount", "must enter amount");
		
		if (bankTransaction.getBankTransactionType() == null) {
			errors.rejectValue("bankTransactionType", "bankTransactionType.null", "select transaction type");
		}
		
		if (bankTransaction.getBankTransactionFromAccount() == null && bankTransaction.getBankTransactionType() != TransactionType.DEPOSIT) {
			errors.rejectValue("bankTransactionFromAccount", "bankTransactionFromAccount.null", "select from account");
		} 
		if (bankTransaction.getBankTransactionToAccount() == null && bankTransaction.getBankTransactionType() != TransactionType.WITHDRAWAL) {
			errors.rejectValue("bankTransactionToAccount", "bankTransactionToAccount.null", "select to account");
		} 
		if (bankTransaction.getBankTransactionFromAccount() != null && bankTransaction.getBankTransactionFromAccount() == bankTransaction.getBankTransactionToAccount() && bankTransaction.getBankTransactionType() != TransactionType.DEPOSIT && bankTransaction.getBankTransactionType() != TransactionType.WITHDRAWAL) {
			errors.rejectValue("bankTransactionToAccount", "bankTransactionToAccount.null", "cannot be same as account transfering from");
		} 
		if (bankTransaction.getBankTransactionDateTime() == null) {
			errors.rejectValue("bankTransactionDateTime", "bankTransactionDateTime.value", "select transfer date");
		}
		
		if (bankTransaction.getBankTransactionFromAccount() != null && bankTransaction.getBankTransactionAmount() > accountService.findById(bankTransaction.getBankTransactionFromAccount()).getAccountBalance()) {
			errors.rejectValue("bankTransactionAmount", "bankTransactionAmount.maxValue", "transfer amount cannot exceed source account balance");
		}
		
		if (bankTransaction.getBankTransactionAmount() == 0) {
			errors.rejectValue("bankTransactionAmount", "bankTransactionAmount.zero", "amount can not be 0");;
		}
		if (bankTransaction.getBankTransactionAmount() < 0) {
			errors.rejectValue("bankTransactionAmount", "bankTransactionAmount.zero", "amount can not be negative");;
		}
		
		
	}

}
