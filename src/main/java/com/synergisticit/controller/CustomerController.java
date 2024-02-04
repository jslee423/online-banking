package com.synergisticit.controller;

import java.security.Principal;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.synergisticit.domain.Customer;
import com.synergisticit.domain.Gender;
import com.synergisticit.service.CustomerService;
import com.synergisticit.service.UserService;
import com.synergisticit.validation.CustomerValidator;

import jakarta.validation.Valid;

@Controller
public class CustomerController {

	@Autowired CustomerService customerService;
	@Autowired UserService userService;
	@Autowired CustomerValidator customerValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(customerValidator);
	}
	
	@RequestMapping("customerForm")
	public ModelAndView customerForm(Customer customer, Principal principal) {
		System.out.println("CustomerController.customerForm()...");
		
		ModelAndView mav = new ModelAndView("customerForm");
		mav.addObject("customers", customerService.findAll());
		mav.addObject("users", userService.findAll());
		mav.addObject("genders", Gender.values());
		mav.addObject("loggedInUser", userService.findUserByUsername(principal.getName()).getUsername());
		mav.addObject("loggedInUserRoles", userService.findUserByUsername(principal.getName()).getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()));
		mav.addObject("activeCustomer", "active");
		
		return mav;
	}
	
	@RequestMapping("saveCustomer")
	public ModelAndView saveCustomer(@Valid @ModelAttribute Customer customer, BindingResult br) {
		System.out.println("CustomerController.saveCustomer()...");
		
		ModelAndView mav = new ModelAndView("customerForm");
		System.out.println("has errors: " + br.hasErrors());
		
		if (br.hasErrors()) {
			mav.addObject("customers", customerService.findAll());
			mav.addObject("users", userService.findAll());
			mav.addObject("genders", Gender.values());
			
			return mav;
		} else {
			customerService.saveCustomer(customer);
			mav.addObject("customers", customerService.findAll());
			mav.addObject("users", userService.findAll());
			mav.addObject("genders", Gender.values());
			mav.setViewName("redirect:customerForm");
			
			return mav;
		}
	}
	
	@RequestMapping("updateCustomer")
	public ModelAndView updateCustomer(Customer customer, Principal principal) {
		System.out.println("CustomerController.updateCustomer()...");
		
		customer = customerService.findById(customer.getCustomerId());
		ModelAndView mav = new ModelAndView("customerForm");
		mav.addObject("customers", customerService.findAll());
		mav.addObject("users", userService.findAll());
		mav.addObject("genders", Gender.values());
		mav.addObject("customer", customer);
		mav.addObject("customerGender", customer.getCustomerGender());
		mav.addObject("loggedInUser", userService.findUserByUsername(principal.getName()).getUsername());
		mav.addObject("loggedInUserRoles", userService.findUserByUsername(principal.getName()).getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()));
		mav.addObject("activeCustomer", "active");
		mav.addObject("updateBtn", true);
		
		return mav;
	}
	
	@RequestMapping("deleteCustomer")
	public ModelAndView deleteCustomer(Customer customer) {
		System.out.println("CustomerController.deleteCustomer()...");
		
		customerService.deleteById(customer.getCustomerId());
		
		ModelAndView mav = new ModelAndView("redirect:customerForm");
		mav.addObject("customers", customerService.findAll());
		mav.addObject("users", userService.findAll());
		mav.addObject("genders", Gender.values());
		
		return mav;
	}
}
