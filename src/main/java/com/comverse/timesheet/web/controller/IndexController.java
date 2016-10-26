package com.comverse.timesheet.web.controller;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;


@Controller
public class IndexController{
	private static Logger log = Logger.getLogger(IndexController.class);
	@Autowired
	private HttpSession session;
	
	@RequestMapping(value = {"/"})
	public ModelAndView root() {
		return new ModelAndView("redirect:/login");
	}
	
	@RequestMapping(value = {"/*"})
	public ModelAndView error() {
		return new ModelAndView("redirect:/login");
	}
	
	@RequestMapping(value = "/PermissionDenied")
	public String PermissionDenied() {
		log.debug("#PermissionDenied");
		return "error";
	}
	
	@RequestMapping(value = "/login")
	public String login() {
		log.debug("#login");
		return "login";
	}
	
    @RequestMapping(params={"authenticationFailure=true"}, value ="/login")
    public String handleInvalidLogin(HttpServletRequest request) {
    	{

    	Object username = request.getAttribute("SPRING_SECURITY_LAST_USERNAME");
       username = session.getAttribute("SPRING_SECURITY_LAST_USERNAME");
    	}
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (authentication != null) {
			Object principal = authentication.getPrincipal();
			log.debug("principal = " + principal);
			String username="anonymousUser";
			if (principal instanceof UserDetails) {
				username = ((UserDetails)principal).getUsername();
	        } else {
	        	username = principal.toString();
	        }
		}
       return "login";
    }
    @RequestMapping(value = "/index")
	public String index(Model model,HttpServletRequest request) {
		log.debug("#index");
		
		return "index";
	}
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null){
			session.setAttribute("user", null);
			new SecurityContextLogoutHandler().logout(request, response, auth);
		}
		return "redirect:/login?logout";
	}
	
	
}
