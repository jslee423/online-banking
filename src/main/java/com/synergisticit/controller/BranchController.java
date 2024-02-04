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

import com.synergisticit.domain.Branch;
import com.synergisticit.service.BranchService;
import com.synergisticit.validation.BranchValidator;

import jakarta.validation.Valid;

@Secured({"DBA", "Admin"})
@Controller
public class BranchController {
	
	@Autowired BranchService branchService;
	@Autowired BranchValidator branchValidator;
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.addValidators(branchValidator);
	}

	@RequestMapping("branchForm")
	public ModelAndView branchForm(Branch branch) {
		System.out.println("BranchController.branchForm()...");
		
		ModelAndView mav = new ModelAndView("branchForm");
		mav.addObject("branches", branchService.findAll());
		mav.addObject("activeBranch", "active");
		
		return mav;
	}
	
	@RequestMapping("saveBranch")
	public ModelAndView saveBranch(@Valid @ModelAttribute Branch branch, BindingResult br) {
		System.out.println("BranchController.saveBranch()...");
		
		ModelAndView mav = new ModelAndView("branchForm");
		
		if (br.hasErrors()) {
			mav.addObject("branches", branchService.findAll());
			mav.setViewName("branchForm");
			
			return mav;
		} else {
			branchService.saveBranch(branch);
			mav.addObject("branches", branchService.findAll());
			mav.setViewName("redirect:branchForm");
			
			return mav;
		}
	}
	
	@RequestMapping("updateBranch")
	public ModelAndView updateBranch(Branch branch) {
		System.out.println("BranchController.saveBranch()...");
		
		branch = branchService.findById(branch.getBranchId());
		
		ModelAndView mav = new ModelAndView("branchForm");
		mav.addObject("branch", branch);
		mav.addObject("branches", branchService.findAll());
		mav.addObject("activeBranch", "active");
		mav.addObject("updateBtn", true);
		
		return mav;
	} 
	
	@RequestMapping("deleteBranch")
	public ModelAndView deleteBranch(Branch branch) {
		System.out.println("BranchController.deleteBranch()...");
		
		branchService.deleteById(branch.getBranchId());
		
		ModelAndView mav = new ModelAndView("redirect:branchForm");
		mav.addObject("branches", branchService.findAll());
		
		return mav;
	} 
}
