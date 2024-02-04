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
import com.synergisticit.service.CustomerService;
import com.synergisticit.validation.CustomerValidator;

import jakarta.validation.Valid;

@Secured({"DBA", "Admin"})
@RestController
@RequestMapping("customers")
public class CustomerRestController {

	@Autowired CustomerService customerService;
	@Autowired CustomerValidator customerValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(customerValidator);
	}
	
	@GetMapping(value = "findAll")
	public ResponseEntity<List<Customer>> findAll() {
		System.out.println("CustomerRestController.findAll()...");
		List<Customer> customers = customerService.findAll();
		if (customers.isEmpty()) {
			//###
			return new ResponseEntity<List<Customer>>(customers, HttpStatus.NOT_FOUND);
		} else {
			//###
			return new ResponseEntity<List<Customer>>(customers, HttpStatus.FOUND);
		}
	} 
	
	@GetMapping(value = "findById", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> findById(@RequestParam Long customerId) {
		System.out.println("CustomerRestController.findById()...");
		Customer customer = customerService.findById(customerId);
		if (customer == null) {
			return new ResponseEntity<Customer>(customer, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Customer>(customer, HttpStatus.FOUND);
		}
	} 
	
	@PostMapping(value = "save", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveCustomer(@Valid @RequestBody Customer customer, BindingResult br) {
		System.out.println("CustomerRestController.saveCustomer()...");
		HttpHeaders headers = new HttpHeaders();
		StringBuilder errors = new StringBuilder("");
		Customer foundCustomer = customerService.findById(customer.getCustomerId());
		
		if (foundCustomer != null || br.hasFieldErrors()) {
			if (br.hasFieldErrors()) {
				List<FieldError> fieldErrors = br.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					System.out.println("errors: " + fieldError.getDefaultMessage());
					errors = errors.append("\"" + fieldError.getField() + "\":" + fieldError.getDefaultMessage());
				}
				headers.add("errors", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			} else {
				errors.append("Customer with id: " + customer.getCustomerId() + " already exists.");
				headers.add("Existing customer", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			}
		} else {
			Customer newCustomer = customerService.saveCustomer(customer);
			headers.add("New Customer", newCustomer.getCustomerName());
			return new ResponseEntity<Customer>(newCustomer, headers, HttpStatus.CREATED);
		}
	} 
	
	@PutMapping(value = "update", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateCustomer(@Valid @RequestBody Customer customer, BindingResult br) {
		System.out.println("CustomerRestController.updateCustomer()...");
		HttpHeaders headers = new HttpHeaders();
		StringBuilder errors = new StringBuilder("");
		Customer foundCustomer = customerService.findById(customer.getCustomerId());
		
		if (foundCustomer == null || br.hasFieldErrors()) {
			if (br.hasFieldErrors()) {
				List<FieldError> fieldErrors = br.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					System.out.println("errors: " + fieldError.getDefaultMessage());
					errors = errors.append("\"" + fieldError.getField() + "\":" + fieldError.getDefaultMessage());
				}
				headers.add("errors", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			} else {
				errors.append("No customer with id: " + customer.getCustomerId());
				headers.add("No customer exists", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.NOT_FOUND);
			}
		} else {
			customerService.saveCustomer(customer);
			return new ResponseEntity<Customer>(customer, HttpStatus.OK);
		}
	}
	
	@DeleteMapping(value = "delete")
	public ResponseEntity<Customer> delete(@RequestParam Long customerId) {
		System.out.println("CustomerRestController.delete()...");
		HttpHeaders headers = new HttpHeaders();
		Customer customer = customerService.findById(customerId);
		
		if (customer == null) {
			return new ResponseEntity<Customer>(HttpStatus.NOT_FOUND);
		} else {
			customerService.deleteById(customerId);
			headers.add("Customer deleted", String.valueOf(customerId));
			return new ResponseEntity<Customer>(customer, headers, HttpStatus.ACCEPTED);
		}
	}
}
