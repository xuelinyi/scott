package com.comverse.timesheet.web.controller;


import java.awt.image.BufferedImage;  
 
import java.io.IOException;  
 
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;  
 
import java.util.List;  
 
import java.util.Map;  
 
  
 
import javax.annotation.Resource;  
 
import javax.imageio.ImageIO;  
 
import javax.servlet.http.HttpServletResponse;

import org.activiti.bpmn.model.BpmnModel;
import org.activiti.engine.ProcessEngine;  
 
import org.activiti.engine.RepositoryService;  
 
import org.activiti.engine.RuntimeService;  
 
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDiagramCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;  
 
import org.activiti.engine.repository.ProcessDefinition;  
 
import org.activiti.engine.runtime.ProcessInstance;  
 
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;  
 
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.comverse.timesheet.web.business.WorkflowTraceBusiness;
import com.comverse.timesheet.web.util.ProcessInstanceDiagramCmd;
import com.comverse.timesheet.web.util.Util;
import com.sun.org.apache.xalan.internal.xsltc.compiler.sym;

@Controller  
public class ActivitiController {
	private static final Logger log = Logger.getLogger(ActivitiController.class);
	@Autowired
	protected RepositoryService repositoryService;
	@Autowired
	protected RuntimeService runtimeService;
	@Autowired
	protected TaskService taskService;

	protected static Map<String, ProcessDefinition> PROCESS_DEFINITION_CACHE = new HashMap<String, ProcessDefinition>();

	@Autowired
	ProcessEngineFactoryBean processEngine;
	@Autowired
	protected WorkflowTraceBusiness traceService;
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
			InputStream resourceAsStream = null;
			try {
				resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(),
						resourceName);
				byte[] b = new byte[1024];
				int len = -1;
				while((len = resourceAsStream.read(b, 0, 1024))!= -1) {
					response.getOutputStream().write(b,0,len);
				}
			}catch(Exception e) {
				log.error("生成图片失败。e:"+e);
			}
		}
		
	}
	@RequestMapping(value="/resource/process-instance")
	public void loadByProcessInstance(@RequestParam("type") String resourceType,
			@RequestParam("pid") String processInstanceId,HttpServletResponse resp) {
		log.debug("读取资源，根据资源类型resourceType:"+resourceType+"和资源ID:"+processInstanceId);
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().
				processInstanceId(processInstanceId).singleResult();
		ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery()
				.processDefinitionId(processInstance.getProcessDefinitionId()).singleResult();
		String resourceName = "";
		if("image".equals(resourceType)) {
			resourceName = processDefinition.getDiagramResourceName();
		}else if("xml".equals(resourceType)) {
			resourceName = processDefinition.getResourceName();
		}
		InputStream resourceAsStream = null;
		try {
			resourceAsStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), resourceName);
			byte[] b = new byte[1024];
			int len = -1;
			while((len = resourceAsStream.read(b, 0, 1024))!= -1) {
				resp.getOutputStream().write(b,0,len);
			}
		}catch(Exception e) {
			log.error("读取资源失败。"+e);
		}
	}
	/**
	 * 输出跟踪流程信息
	 *
	 * @param processInstanceId
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/process/trace")
	@ResponseBody
	public List<Map<String, Object>> traceProcess(@RequestParam("pid") String processInstanceId) throws Exception {
		List<Map<String, Object>> activityInfos = traceService.traceProcess(processInstanceId);
		return activityInfos;
	}

	/**
	 * 读取带跟踪的图片
	 */
	@RequestMapping(value = "/trace/auto")
	public void readResource(@RequestParam("executionId") String executionId, HttpServletResponse response)
			throws Exception {
		ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(executionId)
				.singleResult();
		BpmnModel bpmnModel = repositoryService.getBpmnModel(processInstance.getProcessDefinitionId());
		List<String> activeActivityIds = runtimeService.getActiveActivityIds(executionId);
		// 不使用spring请使用下面的两行代码
		// ProcessEngineImpl defaultProcessEngine = (ProcessEngineImpl)
		// ProcessEngines.getDefaultProcessEngine();
		// Context.setProcessEngineConfiguration(defaultProcessEngine.getProcessEngineConfiguration());

		// 使用spring注入引擎请使用下面的这行代码
		Context.setProcessEngineConfiguration(processEngine.getProcessEngineConfiguration());

		InputStream imageStream = ProcessDiagramGenerator.generateDiagram(bpmnModel, "png", activeActivityIds);

		// 输出资源内容到相应对象
		byte[] b = new byte[1024];
		int len;
		while ((len = imageStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}
	}
}  

