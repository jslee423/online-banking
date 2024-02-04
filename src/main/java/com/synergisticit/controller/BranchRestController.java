package com.synergisticit.controller;

import java.util.Base64;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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

import com.synergisticit.domain.Branch;
import com.synergisticit.service.BranchService;
import com.synergisticit.validation.BranchValidator;

import jakarta.validation.Valid;

@Secured({"DBA", "Admin"})
@RestController
@RequestMapping("branches")
public class BranchRestController {

	@Autowired BranchService branchService;
	@Autowired BranchValidator branchValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(branchValidator);
	}
	
	@GetMapping(value = "findAll")
	public ResponseEntity<List<Branch>> findAll() {
		//###
		String usernamePassword = Base64.getEncoder().encodeToString("jason:123123".getBytes());
		String authHeader = "Basic " + usernamePassword;
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.add("Authorization", authHeader);
		//###
		
		System.out.println("BranchRestController.findAll()...");
		List<Branch> branches = branchService.findAll();
		if (branches.isEmpty()) {
			//###
			return new ResponseEntity<List<Branch>>(branches, httpHeaders, HttpStatus.NOT_FOUND);
		} else {
			//###
			return new ResponseEntity<List<Branch>>(branches, httpHeaders, HttpStatus.FOUND);
		}
	}
	
	@GetMapping(value = "findById", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Branch> findById(@RequestParam Long branchId) {
		System.out.println("BranchRestController.findBranchById()...");
		Branch branch = branchService.findById(branchId);
		if (branch == null) {
			return new ResponseEntity<Branch>(branch, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Branch>(branch, HttpStatus.FOUND);
		}
	} 
	
	@PostMapping(value = "save", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveBranch(@Valid @RequestBody Branch branch, BindingResult br) {
		System.out.println("BranchRestController.saveBranch()...");
		HttpHeaders headers = new HttpHeaders();
		StringBuilder errors = new StringBuilder("");
		Branch foundBranch = branchService.findById(branch.getBranchId());
		
		if (foundBranch != null || br.hasFieldErrors()) {
			if (br.hasFieldErrors()) {
				List<FieldError> fieldErrors = br.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					System.out.println("errors: " + fieldError.getDefaultMessage());
					errors = errors.append("\"" + fieldError.getField() + "\":" + fieldError.getDefaultMessage());
				}
				headers.add("errors", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			} else {
				errors.append("Branch with id: " + branch.getBranchId() + " already exists.");
				headers.add("Existing branch", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			}
			
		} else {
			Branch newBranch = branchService.saveBranch(branch);
			headers.add("New Employee", newBranch.getBranchName());
			return new ResponseEntity<Branch>(newBranch, headers, HttpStatus.CREATED);
		}
		
	}
	
	@PutMapping(value = "update", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateBranch(@Valid @RequestBody Branch branch, BindingResult br) {
		System.out.println("BranchRestController.updateBranch()...");
		HttpHeaders headers = new HttpHeaders();
		StringBuilder errors = new StringBuilder("");
		Branch b = branchService.findById(branch.getBranchId());

		if (b == null || br.hasFieldErrors()) {
			if (br.hasFieldErrors()) {
				List<FieldError> fieldErrors = br.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					System.out.println("errors: " + fieldError.getDefaultMessage());
					errors = errors.append("\"" + fieldError.getField() + "\":" + fieldError.getDefaultMessage());
				}
				headers.add("errors", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			} else {
				errors.append("No branch with id: " + branch.getBranchId());
				headers.add("No branch exists", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.NOT_FOUND);
			}
		} else {
			branchService.saveBranch(branch);
			return new ResponseEntity<Branch>(branch, HttpStatus.OK);
		}
	}
	
	@DeleteMapping(value = "delete")
	public ResponseEntity<Branch> delete(@RequestParam Long branchId) {
		System.out.println("BranchRestController.delete()...");
		HttpHeaders headers = new HttpHeaders();
		
		Branch branch = branchService.findById(branchId);
		if (branch == null) {
			return new ResponseEntity<Branch>(HttpStatus.NOT_FOUND);
		} else {
			branchService.deleteById(branchId);
			headers.add("Branch deleted", String.valueOf(branchId));
			return new ResponseEntity<Branch>(branch, headers, HttpStatus.ACCEPTED);
		}
	} 
	
}
