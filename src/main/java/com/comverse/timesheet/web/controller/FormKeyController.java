package com.comverse.timesheet.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;






import org.activiti.engine.FormService;
import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.ManagementService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.history.HistoricProcessInstanceQuery;
import org.activiti.engine.identity.User;
import org.activiti.engine.impl.persistence.entity.SuspensionState;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.runtime.ProcessInstanceQuery;
import org.activiti.engine.task.NativeTaskQuery;
import org.activiti.engine.task.Task;
import org.apache.commons.lang.StringUtils;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/formFormkey")
public class FormKeyController {
	private static final Logger log = LoggerFactory.getLogger(FormKeyController.class);

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
	  private ManagementService managementService;
	  @Autowired
	  private HttpSession session;
	  /**
	   * 动态form流程刘表
	   * @author 12440
	   */
	  @RequestMapping(value="process-list")
	  public String processDefinitionList(Model model) {
		  log.debug("动态form流程列表");
		  /**
		   * 只读取动态表单
		   */
		  ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery().processDefinitionKey("leave-formkey").active().orderByDeploymentId().desc();
		  List<ProcessDefinition> processDefinitionList = query.list();
		  model.addAttribute("processDefinitionList", processDefinitionList);
		  return "/form/formkey/formkey-process-list";
	  }
	  /**
	   * 读取启动流程的表单字段
	   */
	  @RequestMapping(value="get-form/start/{processDefinitionId}")
	  @ResponseBody
	  public Object findStartForm(@PathVariable(value="processDefinitionId")String processDefinitionId){
		  log.debug("读取启动流程的表单字段。processDefinitionId："+processDefinitionId);
		  Object startForm = formService.getRenderedStartForm(processDefinitionId);
		  return startForm;
	  }
	  /**
	   * 读取Task表单
	   */
	  @RequestMapping(value="get-form/task/{taskId}")
	  @ResponseBody
	  public Object findTaskForm(@PathVariable(value="taskId")String taskId) {
		  log.debug("读取Task表单。taskId"+taskId);
		  Object renderedTaskForm = formService.getRenderedTaskForm(taskId);
		  return renderedTaskForm;
	  }
	  /**
	   * 提交Task的并保存form
	   *@author 12440
	   */
	  @RequestMapping(value="task/complete/{taskId}")
	  public String completeTask(@PathVariable(value="taskId")String taskId,HttpServletRequest request) {
		  log.debug("提交task并保存form。taskId:"+taskId);
		  Map<String, String> formProperties = new HashMap<String, String>();
		  Map<String, String[]> parameterMap = request.getParameterMap();
		  Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		  for (Entry<String, String[]> entry : entrySet) {
			String key = entry.getKey();
			if(StringUtils.defaultString(key).startsWith("fp_")) { 
				String[] paramSplit = key.split("_");
				formProperties.put(paramSplit[1], entry.getValue()[0]);
			}
		  }
		  String activitiUserId = ((org.activiti.engine.identity.User)session.getAttribute("activity_user")).getId();
		  identityService.setAuthenticatedUserId(activitiUserId);
		  formService.submitTaskFormData(taskId, formProperties);
		  return "redirect:/formFormkey/taskList";
	  }
	  /**
	   * 读取启动流程的表单数据
	   */
	  @RequestMapping(value="start-process/{processDefinitionId}")
	  public String submitStartFormAndStartProcessInstance(@PathVariable(value="processDefinitionId")String processDefinitionId,HttpServletRequest request) {
		  log.debug("读取启动流程的表单数据。processDefinitionId："+processDefinitionId);
		  Map<String, String> formProperties = new HashMap<String, String>();

		    // 从request中读取参数然后转换
		    Map<String, String[]> parameterMap = request.getParameterMap();
		    Set<Entry<String, String[]>> entrySet = parameterMap.entrySet();
		    for (Entry<String, String[]> entry : entrySet) {
		      String key = entry.getKey();

		      // fp_的意思是form paremeter
		      if (StringUtils.defaultString(key).startsWith("fp_")) {
		        formProperties.put(key.split("_")[1], entry.getValue()[0]);
		      }
		    }

		    log.debug("start form parameters: {}"+formProperties);

		    String activitiUserId = ((org.activiti.engine.identity.User)session.getAttribute("activity_user")).getId();
		    identityService.setAuthenticatedUserId(activitiUserId);

		    ProcessInstance processInstance = formService.submitStartFormData(processDefinitionId, formProperties);
		    log.debug("start a processinstance: {}"+processInstance);

		    return "redirect:/formFormkey/process-list";
	  }
	  /**
	   * Task列表
	   * @author 12440
	   */
	  @RequestMapping(value="taskList")
	  public String taskList(Model model) {
		  log.debug("查询Task列表");
		  String asigneeSql = "select distinct RES.* from ACT_RU_TASK RES inner join ACT_RE_PROCDEF D on RES.PROC_DEF_ID_ = D.ID_ WHERE RES.ASSIGNEE_ = #{userId}"
		            + " and D.KEY_ = #{processDefinitionKey} and RES.SUSPENSION_STATE_ = #{suspensionState}";

		    // 当前人在候选人或者候选组范围之内
		    String needClaimSql = "select distinct RES1.* from ACT_RU_TASK RES1 inner join ACT_RU_IDENTITYLINK I on I.TASK_ID_ = RES1.ID_ inner join ACT_RE_PROCDEF D1 on RES1.PROC_DEF_ID_ = D1.ID_ WHERE"
		            + " D1.KEY_ = #{processDefinitionKey} and RES1.ASSIGNEE_ is null and I.TYPE_ = 'candidate'"
		            + " and ( I.USER_ID_ = #{userId} or I.GROUP_ID_ IN (select g.GROUP_ID_ from ACT_ID_MEMBERSHIP g where g.USER_ID_ = #{userId} ) )"
		            + " and RES1.SUSPENSION_STATE_ = #{suspensionState}";
		    String sql = asigneeSql + " union all " + needClaimSql;
		    String activitiUserId = ((org.activiti.engine.identity.User)session.getAttribute("activity_user")).getId();
		    NativeTaskQuery query = taskService.createNativeTaskQuery().sql(sql)
		            .parameter("processDefinitionKey", "leave-formkey").parameter("suspensionState", SuspensionState.ACTIVE.getStateCode())
		            .parameter("userId", activitiUserId);
		    List<Task> taskList = query.list();
		    model.addAttribute("taskList", taskList);
		  return "/form/formkey/formkey-task-list";
	  }
	  /**
	   * 签收任务
	   * @author 12440
	   */
	  @RequestMapping(value="claim/{id}")
	  public String claim(@PathVariable(value="id")String taskId) {
		  log.debug("签收任务。taskId:"+taskId);
		  String activitiUserId = ((org.activiti.engine.identity.User)session.getAttribute("activity_user")).getId();
		  taskService.claim(taskId, activitiUserId);
		  return "redirect:/formFormkey/taskList";
	  }
	  /**|
	   * 运行中的流程实例
	   * @author 12440
	   */
	  @RequestMapping(value="runningList")
	  public String runningList(Model model) {
		  log.debug("运行中的实例流程");
		  ProcessInstanceQuery processInstanceQuery = runtimeService.createProcessInstanceQuery().processDefinitionKey("leave-formkey").active().orderByProcessInstanceId().desc();
		  List<ProcessInstance> processInstanceList = processInstanceQuery.list();
		  model.addAttribute("runtimeInstanceList", processInstanceList);
		  return "/form/running-list";
	  }
	  /**
	   * 查询一结束的实例流程
	   * @author 12440
	   */
	  @RequestMapping(value="finishedList")
	  public String finishedList(Model model) {
		  log.debug("查询已结束的实例流程");
		  HistoricProcessInstanceQuery historicProcessInstanceQuery = historyService.createHistoricProcessInstanceQuery().processDefinitionKey("leave-formkey").orderByProcessInstanceId().desc().finished();
		  model.addAttribute("historicProcessInstanceList", historicProcessInstanceQuery.list());
		  return "/form/finished-list";
	  }
}















