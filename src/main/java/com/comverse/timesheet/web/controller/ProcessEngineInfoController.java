package com.comverse.timesheet.web.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.activiti.engine.ManagementService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/management/engine")
public class ProcessEngineInfoController {
	private static final Logger log = Logger.getLogger(ProcessEngineInfoController.class);
	@Autowired
	private ManagementService manamentService;
	@RequestMapping("")
	public String info(Model model) {
		log.debug("引擎属性查询");
		Map<String, String> engineProperties = manamentService.getProperties();
		model.addAttribute("engineProperties", engineProperties);
		Map<String, String> systemPropertiesMap = new HashMap<String, String>();
		Properties systemProperties = System.getProperties();
		Set<Object> Objects = systemProperties.keySet();
		for (Object object : Objects) {
			systemPropertiesMap.put(object.toString(), systemProperties.get(object).toString());
		}
		model.addAttribute("systemProperties", systemPropertiesMap);
		return "management/engine-info";
	}
}
