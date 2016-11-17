package com.comverse.timesheet.web.controller;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.comverse.timesheet.web.bean.leave.Leave;

@Controller
public class LeaveController {
	private static final Logger log = Logger.getLogger(LeaveController.class);
	@RequestMapping("/leave/apply")
	public String createForm(Model model) {
		model.addAttribute("leave", new Leave());
		return "leaveApply";
	}
}
