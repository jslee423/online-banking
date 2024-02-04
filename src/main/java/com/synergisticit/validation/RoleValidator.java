package com.synergisticit.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.synergisticit.domain.Role;

@Component
public class RoleValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return Role.class.equals(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		Role role = (Role)target;

		//ValidationUtils.rejectIfEmptyOrWhitespace(errors, "roleId", "role.roleId.empty", "must enter role id");
		ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "role.name.empty", "must enter role name");
		
		if (role.getName() != null) {
			if (role.getName().length() < 2 && role.getName().length() != 0) {
				errors.rejectValue("name", "name.minLength", "must be 2 or more characters");
			}
			if (role.getName().length() > 30) {
				errors.rejectValue("name", "name.minLength", "must not exceed 50 characters");
			}			
		}
	}

}
