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
import com.synergisticit.domain.User;
import com.synergisticit.service.CustomerService;
import com.synergisticit.service.RoleService;
import com.synergisticit.service.UserService;
import com.synergisticit.validation.UserValidator;

import jakarta.validation.Valid;

@Controller
public class UserController {

	@Autowired UserService userService;
	@Autowired RoleService roleService;
	@Autowired CustomerService customerService;
	@Autowired UserValidator userValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(userValidator);
	}
	
	@RequestMapping("userForm")
	public ModelAndView userForm(User user, Principal principal) {
		System.out.println("UserController.userForm()...");
		
		ModelAndView mav = new ModelAndView("userForm");
		mav.addObject("users", userService.findAll());
		mav.addObject("roles", roleService.findAll());
		mav.addObject("loggedInUser", userService.findUserByUsername(principal.getName()).getUsername());
		mav.addObject("loggedInUserRoles", userService.findUserByUsername(principal.getName()).getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()));
		mav.addObject("activeUser", "active");
		
		return mav;
	}
	
	@RequestMapping("saveUser")
	public ModelAndView saveUser(@Valid @ModelAttribute User user, BindingResult br, Principal principal) {
		System.out.println("UserController.saveUser()...");
		ModelAndView mav = new ModelAndView("userForm");
		
		if (br.hasErrors()) {
			mav.addObject("users", userService.findAll());
			mav.addObject("roles", roleService.findAll());
			mav.addObject("loggedInUser", userService.findUserByUsername(principal.getName()).getUsername());
			
			return mav;
		} else {
			Customer customer = customerService.findById(user.getUserId());
			customer.setUser(user);
			user.setCustomer(customer);
			userService.saveUser(user);
			mav.addObject("users", userService.findAll());
			mav.addObject("roles", roleService.findAll());
			mav.addObject("loggedInUser", userService.findUserByUsername(principal.getName()).getUsername());
			mav.setViewName("redirect:userForm");
			
			return mav;
		}
	}
	
	@RequestMapping("updateUser")
	public ModelAndView updateUser(User user, Principal principal) {
		System.out.println("UserController.updateUser()...");
		
		user = userService.findById(user.getUserId());
		ModelAndView mav = new ModelAndView("userForm");
		mav.addObject("user", user);
		mav.addObject("userRoles", user.getRoles());
		mav.addObject("users", userService.findAll());
		mav.addObject("roles", roleService.findAll());
		mav.addObject("loggedInUser", userService.findUserByUsername(principal.getName()).getUsername());
		mav.addObject("loggedInUserRoles", userService.findUserByUsername(principal.getName()).getRoles().stream().map(role -> role.getName()).collect(Collectors.toList()));
		mav.addObject("activeUser", "active");
		mav.addObject("updateBtn", true);
		
		return mav;
	} 
	
	@RequestMapping("deleteUser")
	public String deleteUser(User user) {
		System.out.println("UserController.updateUser()...");
		
		userService.deleteById(user.getUserId());
		
		return "redirect:userForm";
	}
}
