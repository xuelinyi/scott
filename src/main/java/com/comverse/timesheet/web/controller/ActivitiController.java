package com.comverse.timesheet.web.controller;


import java.awt.image.BufferedImage;  
 
import java.io.IOException;  
 
import java.io.InputStream;  
 
import java.util.HashMap;  
 
import java.util.List;  
 
import java.util.Map;  
 
  
 
import javax.annotation.Resource;  
 
import javax.imageio.ImageIO;  
 
import javax.servlet.http.HttpServletResponse;  
 
  
 
import org.activiti.engine.ProcessEngine;  
 
import org.activiti.engine.RepositoryService;  
 
import org.activiti.engine.RuntimeService;  
 
import org.activiti.engine.TaskService;  
 
import org.activiti.engine.impl.cmd.GetDeploymentProcessDiagramCmd;  
 
import org.activiti.engine.impl.interceptor.Command;  
 
import org.activiti.engine.repository.ProcessDefinition;  
 
import org.activiti.engine.runtime.ProcessInstance;  
 
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;  
 
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.comverse.timesheet.web.util.ProcessInstanceDiagramCmd;
import com.comverse.timesheet.web.util.Util;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

@Controller  
public class ActivitiController {
	private static final Logger log = Logger.getLogger(ActivitiController.class);

	protected RepositoryService repositoryService;

	protected RuntimeService runtimeService;

	protected TaskService taskService;

	protected static Map<String, ProcessDefinition> PROCESS_DEFINITION_CACHE = new HashMap<String, ProcessDefinition>();

	@Autowired
	ProcessEngineFactoryBean processEngine;
	/**
	 * 读取资源，通过部署ID
	 * 
	 */
	@RequestMapping(value="/resource/read")
	public void loadByDeployment(@RequestParam("processDefinitionId") String processDefinitionId,
			@RequestParam("resourceType") String resourceType, HttpServletResponse response) throws Exception {
		log.debug("读取资源，通过部署ID。processDefinitionId："+processDefinitionId);
		log.debug("resourceType:"+resourceType);
		if((null != processDefinitionId)&&(null != resourceType)) {
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
					processDefinitionId(processDefinitionId).singleResult();
			String resourceName = "";
			if(resourceType.equals("image")) {
				resourceName = processDefinition.getDiagramResourceName();
			}else if(resourceType.equals("xml")) {
				resourceName = processDefinition.getResourceName();
			}
		}
		
	}
}  

