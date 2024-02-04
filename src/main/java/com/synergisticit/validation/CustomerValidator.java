package com.synergisticit.validation;

import java.time.LocalDate;
import java.time.Period;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.synergisticit.domain.Customer;

@Component
public class CustomerValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Customer.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Customer customer = (Customer)target;

		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "customerId", "customer.customerId.empty", "must enter customer id");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "customerName", "customer.customerName.empty", "must enter customer name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "customerPhoneNum", "customer.customerPhoneNum.empty", "must enter phone number");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "customerRealId", "customer.customerRealId.empty", "must enter real id");
		
		if (customer.getCustomerGender() == null) {
			errors.rejectValue("customerGender", "customerGender.null", "select gender");
		} 
		
		if (customer.getCustomerDOB() == null) {
			errors.rejectValue("customerDOB", "customerDOB.value", "you did not select date of birth");
		}
		
		if (customer.getCustomerDOB() != null) {
			LocalDate dobLocalDate = customer.getCustomerDOB();
			LocalDate currentLocalDate = LocalDate.now();
			Period period = Period.between(dobLocalDate, currentLocalDate);
			
			if (period.getYears() < 18) {
				errors.rejectValue("customerDOB", "customerDOB.age", "you must be 18 years or older");				
			} 
		}
		
		if (customer.getCustomerAddress() == null) {
			errors.rejectValue("customerAddress", "customerAddress.empty", "must enter customer address");
		}
	}

}
