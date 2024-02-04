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

import com.synergisticit.domain.Customer;
import com.synergisticit.domain.User;
import com.synergisticit.service.CustomerService;
import com.synergisticit.service.UserService;
import com.synergisticit.validation.UserValidator;

import jakarta.validation.Valid;

@Secured({"DBA", "Admin"})
@RestController
@RequestMapping("users")
public class UserRestController {

	@Autowired UserService userService;
	@Autowired CustomerService customerService;
	@Autowired UserValidator userValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(userValidator);
	}
	
	@GetMapping(value = "findAll")
	public ResponseEntity<List<User>> findAll() {
		System.out.println("UserRestController.findAll()...");
		List<User> users = userService.findAll();
		if (users.isEmpty()) {
			//###
			return new ResponseEntity<List<User>>(users, HttpStatus.NOT_FOUND);
		} else {
			//###
			return new ResponseEntity<List<User>>(users, HttpStatus.FOUND);
		}
	} 
	
	@GetMapping(value = "findById", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<User> findById(@RequestParam Long userId) {
		System.out.println("UserRestController.findById()...");
		User user = userService.findById(userId);
		if (user == null) {
			return new ResponseEntity<User>(user, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<User>(user, HttpStatus.FOUND);
		}
	} 
	
	@PostMapping(value = "save", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveUser(@Valid @RequestBody User user, BindingResult br) {
		System.out.println("UserRestController.findById()...");
		HttpHeaders headers = new HttpHeaders();
		StringBuilder errors = new StringBuilder("");
		User foundUser = userService.findById(user.getUserId());
		
		if (foundUser != null || br.hasFieldErrors()) {
			if (br.hasFieldErrors()) {
				List<FieldError> fieldErrors = br.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					System.out.println("errors: " + fieldError.getDefaultMessage());
					errors = errors.append("\"" + fieldError.getField() + "\":" + fieldError.getDefaultMessage());
				}
				headers.add("errors", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			} else {
				errors.append("User with id: " + user.getUserId() + " already exists.");
				headers.add("Existing user", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			}
		} else {
			Customer customer = customerService.findById(user.getUserId());
			customer.setUser(user);
			user.setCustomer(customer);
			User newUser = userService.saveUser(user);
			headers.add("New User", newUser.getUsername());
			return new ResponseEntity<User>(newUser, headers, HttpStatus.CREATED);
		}
	}
	
	@PutMapping(value = "update", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateUser(@Valid @RequestBody User user, BindingResult br) {
		System.out.println("UserRestController.findById()...");
		HttpHeaders headers = new HttpHeaders();
		StringBuilder errors = new StringBuilder("");
		User foundUser = userService.findById(user.getUserId());
		
		if (foundUser == null || br.hasFieldErrors()) {
			if (br.hasFieldErrors()) {
				List<FieldError> fieldErrors = br.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					System.out.println("errors: " + fieldError.getDefaultMessage());
					errors = errors.append("\"" + fieldError.getField() + "\":" + fieldError.getDefaultMessage());
				}
				headers.add("errors", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			} else {
				errors.append("No user with id: " + user.getUserId());
				headers.add("No user exists", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.NOT_FOUND);
			}
		} else {
			Customer customer = customerService.findById(user.getUserId());
			customer.setUser(user);
			user.setCustomer(customer);
			userService.saveUser(user);
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}
	}
	
	@DeleteMapping(value = "delete")
	public ResponseEntity<User> delete(@RequestParam Long userId) {
		System.out.println("UserRestController.delete()...");
		HttpHeaders headers = new HttpHeaders();
		User user = userService.findById(userId);
		
		if (user == null) {
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		} else {
			userService.deleteById(userId);
			headers.add("User deleted", String.valueOf(userId));
			return new ResponseEntity<User>(user, headers, HttpStatus.ACCEPTED);
		}
	} 
}
