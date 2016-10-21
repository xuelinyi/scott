package com.comverse.timesheet.web.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.comverse.timesheet.web.business.IRule;
import com.comverse.timesheet.web.business.Memberbusiness;
import com.comverse.timesheet.web.business.TestBusiness;
import com.comverse.timesheet.web.business.impl.RuleImpl;

@Controller
public class MemberController {
	@Autowired
	private Memberbusiness testBusiness; 
	@Autowired
	private IRule ruleImpl; 
	/**
	 * ����redis demo
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/testRedis") 
	 public String add(HttpServletRequest request, ModelMap modelMap) throws Exception{
		 Map<String,Object> map = new HashMap<String, Object>();
		 testBusiness.add();
		 return "result";
	  }
	/**
	 * ����mongoDB demo
	 * @param request
	 * @param modelMap
	 * @return
	 * @throws Exception
	 */
	 @RequestMapping("/testMongo") 
	 public String testMongo(HttpServletRequest request, ModelMap modelMap) throws Exception{
		 Map<String,Object> map = new HashMap<String, Object>();
		 testBusiness.addAnfFind();
		 return "result";
	  }
	 @RequestMapping("/testDrool") 
	 public String testDrool(HttpServletRequest request, ModelMap modelMap) throws Exception{
		 Map<String,Object> map = new HashMap<String, Object>();
		 System.out.println(":::=============================>>"+ruleImpl.addService());
		 return "result";
	  }
	 @RequestMapping("/testDwr") 
	 public String testDwr(HttpServletRequest request, ModelMap modelMap) throws Exception{
		 return "test";
	  }
}
