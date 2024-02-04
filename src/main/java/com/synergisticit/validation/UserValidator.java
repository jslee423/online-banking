package com.synergisticit.validation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.synergisticit.domain.Customer;
import com.synergisticit.domain.User;
import com.synergisticit.service.CustomerService;
import com.synergisticit.service.UserService;

@Component
public class UserValidator implements Validator {
	
	@Autowired CustomerService customerService;
	@Autowired UserService userService;

	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		User user = (User)target;
		
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "userId", "user.userId.empty", "must enter user id");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "user.username.empty", "must enter user name");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "user.password.empty", "must enter password");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "user.email.empty", "must enter email");

		if (user.getUsername() != null) {
			if (user.getUsername().length() < 3 && user.getUsername().length() != 0) {
				errors.rejectValue("username", "username.minLength", "must be 3 or more characters");
			}
			if (user.getUsername().length() > 50) {
				errors.rejectValue("username", "username.minLength", "must not exceed 50 characters");
			}			
		}
		
		if (user.getPassword() != null) {
			if (user.getPassword().length() < 6 && user.getPassword().length() != 0) {
				errors.rejectValue("password", "password.minLength", "must be 6 or more characters");
			}
			if (user.getPassword().length() > 50) {
				errors.rejectValue("password", "password.minLength", "must not exceed 50 characters");
			}
		}
		
		if (user.getRoles().isEmpty()) {
			errors.rejectValue("roles", "user.roles.empty", "you must select a role");
		}
		
		if (user.getUserId() != null) {
			Customer customer = customerService.findById(user.getUserId());
			if (customer == null) {
				errors.rejectValue("userId", "user.userId.notFound", "customer with this id does not exist");
			}
		}
		
		if (userService.findUserByUsername(user.getUsername()) != null && userService.findById(user.getUserId()) == null) {
			errors.rejectValue("username", "username.exists", "username already exists");
		}
	}

}
