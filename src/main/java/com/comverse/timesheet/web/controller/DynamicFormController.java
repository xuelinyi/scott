package com.comverse.timesheet.web.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.form.FormProperty;
import org.activiti.engine.form.StartFormData;
import org.activiti.engine.form.TaskFormData;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.impl.form.StartFormDataImpl;
import org.activiti.engine.impl.form.TaskFormDataImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comverse.timesheet.web.bean.User;

@Controller
@RequestMapping(value="dynamic")
public class DynamicFormController {
	private static final Logger log = Logger.getLogger(DynamicFormController.class);
	@Autowired
	private RepositoryService repositoryService;
	@Autowired
	private FormService formService;
	@Autowired
	private TaskService taskService;
	@Autowired
	private IdentityService identityService;
	@Autowired
	private HistoryService historyService;
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private HttpSession session;
	/**
	 * 动态form流程列表
	 * @author 12440
	 */
	@RequestMapping(value="process-list")
	public String processDefinitionList(Model model) {
		log.debug("查询动态form流程列表");
		/**
		 * 只读取动态表单 leave-dynamic-from
		 */
		List<ProcessDefinition> processDefinitionList = new ArrayList<ProcessDefinition>();
		ProcessDefinitionQuery processDefinitionQuery1 = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave-dynamic-from").active().orderByDeploymentId().desc();
		processDefinitionList = processDefinitionQuery1.list();
		ProcessDefinitionQuery processDefinitionQuery2 = repositoryService.createProcessDefinitionQuery().processDefinitionKey("dispatch").active().orderByDeploymentId().desc();
		processDefinitionList.addAll(processDefinitionQuery2.list());
		model.addAttribute("processDefinitionList", processDefinitionList);
		return "/form/dynamic/dynamic-form-process-list";
	}
	/**
	 * 读取启动流程的表单字段
	 */
	@RequestMapping(value="start-process/{processDefinitionId}")
	public String submitStartFormAndStartProcessInstance(@PathVariable("processDefinitionId") String processDefinitionId,
			HttpServletRequest request) {
		log.debug("读取启动流程的表单字段.processDefinitionId:"+processDefinitionId);
		Map<String, String> formProperties = new HashMap<String,String>();
		//从request中获取参数
		Map<String, String[]> parameterMap = request.getParameterMap();
		Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();
			if(StringUtils.defaultString(key).startsWith("fp_")) {
				formProperties.put(key.split("_")[1], entry.getValue()[0]);
			}
		}
		log.debug("start form parameters: {}"+formProperties);
		String activitiUserId = ((org.activiti.engine.identity.User)session.getAttribute("activity_user")).getId();
		identityService.setAuthenticatedUserId(activitiUserId+"");
		ProcessInstance processInstance = formService.submitStartFormData(processDefinitionId, formProperties);
		log.debug("start a processinstance: {}" + processInstance);
		return "redirect:/dynamic/process-list";
	}
	@RequestMapping(value="get-form/start/{processDefinitionId}")
	@ResponseBody
	public Map<String, Object> findStartForm(@PathVariable("processDefinitionId") String processDefinitionId) {
		Map<String, Object> result = new HashMap<String, Object>();
		StartFormDataImpl startFormDataImpl = (StartFormDataImpl)formService.getStartFormData(processDefinitionId);
		startFormDataImpl.setProcessDefinition(null);
		
		/**
		 * 读取enum类型数据，用于下拉框
		 */
		List<FormProperty> formProperties = startFormDataImpl.getFormProperties();
		for (FormProperty formProperty : formProperties) {
			Map<String, String> values = (Map<String, String>)formProperty.getType().getInformation("values");
			if(null != values) {
				for (Entry<String, String> enumEntry : values.entrySet()) {
					log.debug("enum, key: {}, value: {}"+enumEntry.getKey()+enumEntry.getValue());
				}
				result.put("enum_"+formProperty.getId(), values);
			}
		}
		result.put("form", startFormDataImpl);
		return result;
	}
	/**
	 * taskList 列表
	 * @author 12440
	 */
	@RequestMapping(value="taskList")
	public String taskList(Model model) {
		log.debug("查询外置的taskList列表");
		String activitiUserId = ((org.activiti.engine.identity.User)session.getAttribute("activity_user")).getId();
		List<Task> taskList = new ArrayList<Task>();
		//分配到当前登陆人的任务
		List<Task> userTaskList = taskService.createTaskQuery().processDefinitionKey("leave-dynamic-from").taskAssignee(activitiUserId).active().orderByTaskId().desc().list();
		//未签收的任务
		taskList.addAll(taskService.createTaskQuery().processDefinitionKey("leave-dynamic-from").taskCandidateUser(activitiUserId).active().orderByTaskId().desc().list());
		taskList.addAll(userTaskList);
		
		//分配到当前登陆人的任务
		List<Task> userDispatchTaskList = taskService.createTaskQuery().processDefinitionKey("dispatch").taskAssignee(activitiUserId).active().orderByTaskId().desc().list();
		//未签收的任务
		taskList.addAll(taskService.createTaskQuery().processDefinitionKey("dispatch").taskCandidateUser(activitiUserId).active().orderByTaskId().desc().list());
		taskList.addAll(userDispatchTaskList);
		model.addAttribute("taskList", taskList);
		return "/form/dynamic/dynamic-form-task-list";
	}
	/**
	 * 签收任务
	 * @author 12440
	 */
	@RequestMapping(value="task/claim/{id}")
	public String claim(@PathVariable(value="id")String taskId) {
		log.debug("签收任务id为"+taskId);
		String activitiUserId = ((org.activiti.engine.identity.User)session.getAttribute("activity_user")).getId();
		taskService.claim(taskId, activitiUserId);
		return "redirect:/dynamic/taskList";
	}
	/**
	 * 读取Task表单数据
	 * @author 12440
	 */
	@RequestMapping(value="get-form/task/{taskId}")
	@ResponseBody
	public Map<String, Object> findTaskForm(@PathVariable(value="taskId") String taskId) {
		log.debug("读取Task表单数据。taskId:"+taskId);
		Map<String, Object> result = new HashMap<String, Object>();
		TaskFormDataImpl taskFormData = (TaskFormDataImpl)formService.getTaskFormData(taskId);
		taskFormData.setTask(null);
		result.put("taskFormData", taskFormData);
		List<FormProperty> formPropertyList = taskFormData.getFormProperties();
		for (FormProperty formProperty : formPropertyList) {
			Map<String, String> values = (Map<String, String>)formProperty.getType().getInformation("values");
			if(null != values) {
				for (Entry<String,String> enumEntry : values.entrySet()) {
					
				}
				result.put(formProperty.getId(), values);
			}
		}
		return result;
	}
	/**
	 * 提交Task的并保存form
	 * @author 12440
	 */
	@RequestMapping(value="task/complete/{taskId}")
	public String completeTask(@PathVariable(value="taskId")String taskId,HttpServletRequest req) {
		log.debug("提交Task并保存form。taskId"+taskId);
		Map<String, String> formProperties = new HashMap<String, String>();
		Map<String, String[]> paraterMap = req.getParameterMap();
		Set<Entry<String, String[]>> entrySet = paraterMap.entrySet();
		for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();
			if(StringUtils.defaultString(key).startsWith("fp_")) {
				formProperties.put(key.split("_")[1], entry.getValue()[0]);
			}
		}
		String activitiUserId = ((org.activiti.engine.identity.User)session.getAttribute("activity_user")).getId();
		identityService.setAuthenticatedUserId(activitiUserId);
		formService.submitTaskFormData(taskId, formProperties);
		return "redirect:/dynamic/taskList";
	}
	/**
	 * 运行中的实例流程
	 * @author 12440
	 */
	@RequestMapping(value="runningList")
	public String processInstanceRunning(Model model) {
		log.debug("查询运行中的实例流程。");
		List<ProcessInstance> runtimeInstanceList = new ArrayList<ProcessInstance>();
		runtimeInstanceList.addAll(runtimeService.createProcessInstanceQuery().processDefinitionKey("leave-dynamic-from").orderByProcessInstanceId().desc().active().list());
		runtimeInstanceList.addAll(runtimeService.createProcessInstanceQuery().processDefinitionKey("dispatch").orderByProcessInstanceId().desc().active().list());
		model.addAttribute("runtimeInstanceList", runtimeInstanceList);
		return "/form/running-list";
	}
	/**
	 * 已结束的实例流程
	 * @author 12440
	 */
	@RequestMapping(value="finishedList")
	public String processInstanceFinished(Model model) {
		log.debug("查询运行中的实例流程。");
		List<HistoricProcessInstance> historicProcessInstanceList = new ArrayList<HistoricProcessInstance>();
		historicProcessInstanceList.addAll(historyService.createHistoricProcessInstanceQuery().processDefinitionKey("leave-dynamic-from").orderByProcessInstanceId().desc().finished().list());
		historicProcessInstanceList.addAll(historyService.createHistoricProcessInstanceQuery().processDefinitionKey("dispatch").orderByProcessInstanceId().desc().finished().list());
		model.addAttribute("historicProcessInstanceList", historicProcessInstanceList);
		return "/form/finished-list";
	}
}














