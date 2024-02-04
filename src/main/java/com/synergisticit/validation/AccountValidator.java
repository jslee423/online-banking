package com.synergisticit.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.synergisticit.domain.Account;

@Component
public class AccountValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Account.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Account account = (Account)target;

		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "accountId", "account.accountId.empty", "must enter account id");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "accountHolder", "account.accountHolder.empty", "must enter account holder");
		
		if (account.getAccountType() == null) {
			errors.rejectValue("accountType", "accountType.null", "select account type");
		} 
		
		if (account.getAccountOpenDate() == null) {
			errors.rejectValue("accountOpenDate", "accountOpenDate.value", "you did not select an open date");
		}
		
		if (account.getAccountHolder() != null) {
			if (account.getAccountHolder().length() < 3 && account.getAccountHolder().length() != 0) {
				errors.rejectValue("accountHolder", "accountHolder.minLength", "must be 3 or more characters");
			}
			if (account.getAccountHolder().length() > 50) {
				errors.rejectValue("accountHolder", "accountHolder.minLength", "must not exceed 50 characters");
			}			
		}
		
		if (account.getAccountBranch() == null) {
			errors.rejectValue("accountBranch", "accountBranch.null", "select branch");
		} 
	}

}
