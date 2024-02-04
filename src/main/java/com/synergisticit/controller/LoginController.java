package com.synergisticit.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class LoginController {
	
	@RequestMapping("accessDenied")
	public String accessDenied() {
		return "accessDenied";
	} 
	
	@RequestMapping("login")
	public String login(
			@RequestParam(required = false) String logout,
			@RequestParam(value = "error", required = false) String error,
			HttpServletRequest req, HttpServletResponse res, Model model
	) {
		System.out.println("LoginController.login()...");
		String message = null;
		
		if (logout != null) {
			Authentication auth = SecurityContextHolder.getContext().getAuthentication();
			if (auth != null) {
				new SecurityContextLogoutHandler().logout(req, res, auth);
				message = "you are logged out";
				model.addAttribute("message", message);
				//return "home";
			}
		}
		if (error != null) {
			message = "either username or passwored is incorrect";
		}
		model.addAttribute("message", message);
		
		return "loginForm";
	}
}
