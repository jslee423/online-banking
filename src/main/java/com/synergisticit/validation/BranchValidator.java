package com.synergisticit.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.synergisticit.domain.Branch;

@Component
public class BranchValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Branch.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Branch branch = (Branch)target;
		System.out.println("validator name: " + branch.getBranchName());
		
		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "branchId", "branch.branchId.empty", "must enter branch id");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "branchName", "branch.branchName.empty", "must enter branch name");
		
		if (branch.getBranchName() != null) {
			if (branch.getBranchName().length() < 3 && branch.getBranchName().length() != 0) {
				errors.rejectValue("branchName", "branchName.minLength", "must be 3 or more characters");
			}
			if (branch.getBranchName().length() > 50) {
				errors.rejectValue("branchName", "branchName.minLength", "must not exceed 50 characters");
			}		
		}
		
		if (branch.getBranchAddress() == null) {
			errors.rejectValue("branchAddress", "branchAddress", "must enter branch address");
		}
//		if (branch.getBranchAddress().getAddressLine1() == null) {
//			errors.rejectValue("branchAddress", "branchAddress.addressLine1", "must enter address line 1");
//		}
//		if (branch.getBranchAddress().getCity() == null) {
//			errors.rejectValue("branchAddress", "branchAddress.city", "must enter city");
//		}
//		if (branch.getBranchAddress().getState() == null) {
//			errors.rejectValue("branchAddress", "branchAddress.state", "must enter state");
//		}
//		if (branch.getBranchAddress().getCountry() == null) {
//			errors.rejectValue("branchAddress", "branchAddress.country", "must enter country");
//		}
//		if (branch.getBranchAddress().getZipCode() == null) {
//			errors.rejectValue("branchAddress", "branchAddress.zipCode", "must enter zipcode");
//		}
		
	}

}
