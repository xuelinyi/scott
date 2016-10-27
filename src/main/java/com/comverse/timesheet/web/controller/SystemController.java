package com.comverse.timesheet.web.controller;


import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class SystemController {
	private static final Logger log = Logger.getLogger(SystemController.class);
	@RequestMapping("/system/userList")
	public String jumpUserPage() {
		log.debug("跳转用户列表界面");
		return "system_user";
	}
}
