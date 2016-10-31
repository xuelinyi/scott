package com.comverse.timesheet.web.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comverse.timesheet.web.bean.TestTable;
import com.comverse.timesheet.web.business.TestBusiness;
import com.comverse.timesheet.web.util.PageList;

@Controller
public class SystemController {
	private static final Logger log = Logger.getLogger(SystemController.class);
	@Autowired
    private TestBusiness testBusiness; 
	@RequestMapping("/system/userList")
	public String jumpUserPage(HttpServletRequest request, ModelMap modelMap) throws Exception {
		log.debug("跳转用户列表界面");
		List<TestTable> list=testBusiness.find();
	    modelMap.addAttribute("resultList", list);
	    System.out.println("----------->total:" + list.size());  
		return "system_user";
	}
}
