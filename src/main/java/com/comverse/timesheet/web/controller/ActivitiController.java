package com.comverse.timesheet.web.controller;


import java.awt.image.BufferedImage;  
 
import java.io.IOException;  
 
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;  
 
import java.util.List;  
 
import java.util.Map;  
 
  
 








import java.util.zip.ZipInputStream;

import javax.annotation.Resource;  
 
import javax.imageio.ImageIO;  
 
import javax.servlet.http.HttpServletResponse;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamReader;

import org.activiti.bpmn.converter.BpmnXMLConverter;
import org.activiti.bpmn.model.BpmnModel;
import org.activiti.editor.constants.ModelDataJsonConstants;
import org.activiti.editor.language.json.converter.BpmnJsonConverter;
import org.activiti.engine.ProcessEngine;  
 
import org.activiti.engine.RepositoryService;  
 
import org.activiti.engine.RuntimeService;  
 
import org.activiti.engine.TaskService;
import org.activiti.engine.impl.bpmn.diagram.ProcessDiagramGenerator;
import org.activiti.engine.impl.cmd.GetDeploymentProcessDiagramCmd;
import org.activiti.engine.impl.context.Context;
import org.activiti.engine.impl.interceptor.Command;  
 
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;  
 
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;  
 
import org.activiti.engine.task.Task;
import org.activiti.spring.ProcessEngineFactoryBean;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.node.ObjectNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;  
 
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.comverse.timesheet.web.business.WorkflowTraceBusiness;
import com.comverse.timesheet.web.util.ProcessInstanceDiagramCmd;
import com.comverse.timesheet.web.util.Util;
import com.comverse.timesheet.web.util.WorkflowUtils;
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
	/**
	 * 流程定义列表
	 * @author 12440
	 */
	@RequestMapping(value="/workflow/process-list")
	public String processList(Model model) {
		log.debug("查询流程定义列表");
		/**
		 * 保存对象，一个是ProcessDefinition流程定义，一个是Deployment流程部署
		 */
		List<Object[]> objects = new ArrayList<Object[]>();
		ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery().orderByDeploymentId().desc();
		List<ProcessDefinition> processDefinitionList= processDefinitionQuery.list();
		for (ProcessDefinition processDefinition : processDefinitionList) {
			String deploymentId = processDefinition.getDeploymentId();
			Deployment deployment = repositoryService.createDeploymentQuery().deploymentId(deploymentId).singleResult();
			objects.add(new Object[]{processDefinition,deployment});
		}
		model.addAttribute("objectList", objects);
		return "workflow/process-list";
	}
	/**
	 * 挂起或激活流程实例
	 */
	@RequestMapping(value="processdefinition/update/{state}/{processDefinitionId}")
	public String updateState(@PathVariable(value="state")String state,@PathVariable(value="processDefinitionId")String processDefinitionId) {
		log.debug("挂起或激活流程实例state"+state);
		log.debug("processDefinitionId:"+processDefinitionId);
		if(state.equals("active")) {
			repositoryService.activateProcessDefinitionById(processDefinitionId);
		}else if(state.equals("suspend")) {
			repositoryService.suspendProcessDefinitionById(processDefinitionId);
		}
		return "redirect:/workflow/process-list";
	}
	/**
	 * 删除部署的流程，级联删除流程实例
	 * @author 12440
	 */
	@RequestMapping(value="/process/delete")
	public String delete(@RequestParam("deploymentId") String deploymentId) {
		log.debug("删除部署的流程，级联删除流程实例。deploymentId："+deploymentId);
		repositoryService.deleteDeployment(deploymentId);
		return "redirect:/workflow/process-list";
	}
	
	/**
	 * 部署流程
	 * @author 12440
	 */
	@RequestMapping(value="/workflow/deploy")
	public String deploy(@Value("#{APP_PROPERTIES['export.diagram.path']}")String exportDir,@RequestParam(value="file",required=false)MultipartFile file) {
		log.debug("部署流程。exportDir："+exportDir);
		log.debug("file:"+file.getName());
		String fileName = file.getOriginalFilename();
		InputStream in = null;
		Deployment deployment = null;
		ZipInputStream zipIn = null;
		try {
			in = file.getInputStream();
			String extension = FilenameUtils.getExtension(fileName);
			if(extension.equals("zip")||extension.equals("bar")) {
				zipIn = new ZipInputStream(in);
				deployment = repositoryService.createDeployment().addZipInputStream(zipIn).deploy();
			}else{
				deployment = repositoryService.createDeployment().addInputStream(fileName, in).deploy();
			}
			List<ProcessDefinition> processDefinitionList = repositoryService.createProcessDefinitionQuery().deploymentId(deployment.getId()).list();
			for (ProcessDefinition processDefinition : processDefinitionList) {
				WorkflowUtils.exportDiagramToFile(repositoryService, processDefinition, exportDir);
			}
		}catch(Exception e) {
			log.error("部署流程失败。"+e);
		}finally {
			if(null!=zipIn) {
				try {
					zipIn.close();
					zipIn = null;
				} catch (IOException e) {
				}
			}
			if(null!=in) {
				try {
					in.close();
					in = null;
				} catch (IOException e) {
				}
			}
		}
		return "redirect:/workflow/process-list";
	}
	/**
	 * 转化为model
	 * @author 12440
	 */
	@RequestMapping(value="/process/convert-to-model/{processDefinitionId}")
	public String convertToModel(@PathVariable(value="processDefinitionId")String processDefinitionId) {
		log.debug("转化为model");
		if(null != processDefinitionId) {
			ProcessDefinition processDefinition = repositoryService.createProcessDefinitionQuery().
					processDefinitionId(processDefinitionId).singleResult();
			InputStream bpmnStream = repositoryService.getResourceAsStream(processDefinition.getDeploymentId(), 
					processDefinition.getResourceName());
			XMLInputFactory xif = XMLInputFactory.newInstance();
			InputStreamReader in = null;
			XMLStreamReader xtr = null;
			try {
				in = new InputStreamReader(bpmnStream,"UTF-8");
				xtr = xif.createXMLStreamReader(in);
				BpmnModel bpmnModel = new BpmnXMLConverter().convertToBpmnModel(xtr);
				BpmnJsonConverter converter = new BpmnJsonConverter();
				ObjectNode modelNode = converter.convertToJson(bpmnModel);
				org.activiti.engine.repository.Model modelData = repositoryService.newModel();
				modelData.setKey(processDefinition.getKey());
				modelData.setName(processDefinition.getResourceName());
				modelData.setCategory(processDefinition.getDeploymentId());
				
				ObjectNode modelObjectNode = new ObjectMapper().createObjectNode();
				modelObjectNode.put(ModelDataJsonConstants.MODEL_NAME, processDefinition.getName());
				modelObjectNode.put(ModelDataJsonConstants.MODEL_REVISION, 1);
				modelObjectNode.put(ModelDataJsonConstants.MODEL_DESCRIPTION, processDefinition.getDescription());
				modelData.setMetaInfo(modelObjectNode.toString());
				repositoryService.saveModel(modelData);
				repositoryService.addModelEditorSource(modelData.getId(), modelNode.toString().getBytes("utf-8"));
			}catch(Exception e) {
				log.error("转化为model失败");
			}
		}
		return "redirect:/model/list";
	}
}  





