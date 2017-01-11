package com.comverse.timesheet.web.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.activiti.engine.ManagementService;
import org.activiti.engine.management.TableMetaData;
import org.activiti.engine.management.TablePage;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/management/database")
public class DataBaseController {
	private static final Logger log = Logger.getLogger(DataBaseController.class);
	@Autowired
	private ManagementService managementService;
	@RequestMapping("")
	public String index(@RequestParam(value = "tableName", required = false) String tableName, HttpServletRequest request,Model model){
		log.debug("查询某个表的信息。tableName："+tableName);
		Map<String, Long> tableCount = managementService.getTableCount();
		
		List<String> keys = new ArrayList<String>();
		keys.addAll(tableCount.keySet());
		Collections.sort(keys);
		Map<String, Long> sortedTableCount = new TreeMap<String, Long>();
		for (String key : keys) {
			sortedTableCount.put(key, tableCount.get(key));
		}
		model.addAttribute("tableCount", sortedTableCount);
		if(StringUtils.isNotBlank(tableName)) {
			TableMetaData tableMetadata = managementService.getTableMetaData(tableName);
			model.addAttribute("tableMetadata", tableMetadata);
			TablePage tablePages = managementService.createTablePageQuery().tableName(tableName).listPage(0, 100);
			model.addAttribute("tablePages", tablePages.getRows());
			Long pageTotalCount = tableCount.get(tableName);
			model.addAttribute("pageTotalCount", pageTotalCount);
		}
		return "management/database";
	}
}
