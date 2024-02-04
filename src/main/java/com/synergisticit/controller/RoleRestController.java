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

import com.synergisticit.domain.Role;
import com.synergisticit.service.RoleService;
import com.synergisticit.validation.RoleValidator;

import jakarta.validation.Valid;

@Secured({"DBA", "Admin"})
@RestController
@RequestMapping("roles")
public class RoleRestController {

	@Autowired RoleService roleService;
	@Autowired RoleValidator roleValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(roleValidator);
	}
	
	@GetMapping(value = "findAll")
	public ResponseEntity<List<Role>> findAll() {
		System.out.println("RoleRestController.findAll()...");
		List<Role> roles = roleService.findAll();
		if (roles.isEmpty()) {
			//###
			return new ResponseEntity<List<Role>>(roles, HttpStatus.NOT_FOUND);
		} else {
			//###
			return new ResponseEntity<List<Role>>(roles, HttpStatus.FOUND);
		}
	} 
	
	@GetMapping(value = "findById", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Role> findById(@RequestParam Long roleId) {
		System.out.println("RoleRestController.findById()...");
		Role role = roleService.findById(roleId);
		if (role == null) {
			return new ResponseEntity<Role>(role, HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<Role>(role, HttpStatus.FOUND);
		}
	} 
	
	@PostMapping(value = "save", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> saveRole(@Valid @RequestBody Role role, BindingResult br) {
		System.out.println("RoleRestController.saveRole()...");
		HttpHeaders headers = new HttpHeaders();
		StringBuilder errors = new StringBuilder("");
		Role foundRole = roleService.findById(role.getRoleId());	
		
		if (foundRole != null || br.hasFieldErrors()) {
			if (br.hasFieldErrors()) {
				List<FieldError> fieldErrors = br.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					System.out.println("errors: " + fieldError.getDefaultMessage());
					errors = errors.append("\"" + fieldError.getField() + "\":" + fieldError.getDefaultMessage());
				}
				headers.add("errors", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			} else {
				errors.append("Role with id: " + role.getRoleId() + " already exists.");
				headers.add("Existing role", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			}
		} else {
			Role newRole = roleService.saveRole(role);
			headers.add("New role", role.getName());
			return new ResponseEntity<Role>(newRole, headers, HttpStatus.CREATED);
		}
	}
	
	@PutMapping(value = "update", consumes = "application/json", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> updateRole(@Valid @RequestBody Role role, BindingResult br) {
		System.out.println("RoleRestController.updateRole()...");
		HttpHeaders headers = new HttpHeaders();
		StringBuilder errors = new StringBuilder("");
		Role foundRole = roleService.findById(role.getRoleId());
		
		if (foundRole == null || br.hasFieldErrors()) {
			if (br.hasFieldErrors()) {
				List<FieldError> fieldErrors = br.getFieldErrors();
				for (FieldError fieldError : fieldErrors) {
					System.out.println("errors: " + fieldError.getDefaultMessage());
					errors = errors.append("\"" + fieldError.getField() + "\":" + fieldError.getDefaultMessage());
				}
				headers.add("errors", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.BAD_REQUEST);
			} else {
				errors.append("No role with id: " + role.getRoleId());
				headers.add("No role exists", errors.toString());
				return new ResponseEntity<StringBuilder>(errors, headers, HttpStatus.NOT_FOUND);
			}
		} else {
			roleService.saveRole(role);
			return new ResponseEntity<Role>(role, HttpStatus.OK);
		}
	} 
	
	@DeleteMapping(value = "delete")
	public ResponseEntity<Role> delete(@RequestParam Long roleId) {
		System.out.println("RoleRestController.delete()...");
		HttpHeaders headers = new HttpHeaders();
		Role role = roleService.findById(roleId);
		
		if (role == null) {
			return new ResponseEntity<Role>(HttpStatus.NOT_FOUND);
		} else {
			roleService.deleteById(roleId);
			headers.add("Role deleted", String.valueOf(roleId));
			return new ResponseEntity<Role>(role, headers, HttpStatus.ACCEPTED);
		}
	}
	
	
	
}
