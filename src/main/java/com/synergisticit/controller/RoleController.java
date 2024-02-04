package com.synergisticit.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.synergisticit.domain.Role;
import com.synergisticit.service.RoleService;
import com.synergisticit.validation.RoleValidator;

import jakarta.validation.Valid;

@Secured({"DBA", "Admin"})
@Controller
public class RoleController {

	@Autowired RoleService roleService;
	@Autowired RoleValidator roleValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(roleValidator);
	}
	
	@RequestMapping("roleForm")
	public ModelAndView roleForm(Role role) {
		System.out.println("RoleController.roleForm()...");
		
		ModelAndView mav = new ModelAndView("roleForm");
		mav.addObject("roles", roleService.findAll());
		mav.addObject("activeRole", "active");
		
		return mav;
	}
	
	@RequestMapping("saveRole")
	public ModelAndView saveRole(@Valid @ModelAttribute Role role, BindingResult br) {
		System.out.println("RoleController.saveRole()...");
		ModelAndView mav = new ModelAndView("roleForm");
		
		if (br.hasErrors()) {
			mav.addObject("roles", roleService.findAll());
			mav.setViewName("roleForm");
			
			return mav;
		} else {
			roleService.saveRole(role);
			mav.addObject("roles", roleService.findAll());
			mav.setViewName("redirect:roleForm");
			
			return mav;
		}
	}
	
	@RequestMapping("updateRole")
	public ModelAndView updateRole(Role role) {
		System.out.println("RoleController.updateRole()...");
		
		role = roleService.findById(role.getRoleId());
		ModelAndView mav = new ModelAndView("roleForm");
		mav.addObject("role", role);
		mav.addObject("roles", roleService.findAll());
		mav.addObject("activeRole", "active");
		mav.addObject("updateBtn", true);
		
		return mav;
	}
	
	@RequestMapping("deleteRole")
	public ModelAndView deleteRole(Role role) {
		System.out.println("RoleController.deleteRole()...");
		
		roleService.deleteById(role.getRoleId());
		
		ModelAndView mav = new ModelAndView("redirect:roleForm");
		mav.addObject("roles", roleService.findAll());
		
		return mav;
	}
}
