package com.comverse.timesheet.web.business.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;
import org.activiti.engine.task.Task;
import org.activiti.engine.task.TaskQuery;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.comverse.timesheet.web.bean.leave.Leave;
import com.comverse.timesheet.web.business.LeaveWorkflowBusiness;
import com.comverse.timesheet.web.dao.ILeaveDAO;
@Component
public class LeaveWorkflowBusinessImpl implements LeaveWorkflowBusiness{
	private static final Logger log = Logger.getLogger(LeaveWorkflowBusinessImpl.class);
	@Resource
	private ILeaveDAO leaveDAO;
	@Resource
	private RuntimeService runtimeService;
	@Resource
	protected TaskService taskService;
	@Resource
	protected HistoryService historyService;
	@Resource
	protected RepositoryService repositoryService;
	@Autowired
	private IdentityService identityService;
	public ProcessInstance startWorkflow(Leave entity, Map<String, Object> variables) {
		//保存请假实体
		leaveDAO.saveLeave(entity);
	    log.debug("save entity: {}"+entity);
	    log.debug("======================businessKey:"+entity.getId());
	    // 用来设置启动流程的人员ID，引擎会自动把用户ID保存到activiti:initiator中
	    identityService.setAuthenticatedUserId(entity.getUserId());
	    //根据名称、id获得流程实例
	    ProcessInstance processInstance = runtimeService.startProcessInstanceByKey("leave", entity.getId()+"", variables);
	    String processInstanceId = processInstance.getId();
	    entity.setProcessInstanceId(processInstanceId);
	    leaveDAO.updateLeaveProcessInstanceId(entity);
	    return processInstance;
	}
	
	/**
	 * 查询待办任务
	 */
	@Transactional(readOnly = true)
	public List<Leave> findTodoTasks(String userId){
		log.debug("根据userId查询其待办任务。userId:"+userId);
		List<Leave> results = new ArrayList<Leave>();
		if(null != userId) {
			List<Task> tasks = new ArrayList<Task>();
			try {
				//根据当前人的ID查询
				TaskQuery todoQuery = taskService.createTaskQuery().processDefinitionKey("leave").taskAssignee(userId).active().orderByTaskId().desc()
						.orderByTaskCreateTime().desc();
				List<Task> todoList = todoQuery.list();
				//根据当前人查询未签收的任务
				TaskQuery claimQuery = taskService.createTaskQuery().processDefinitionKey("leave").taskCandidateUser(userId).active().orderByTaskId().desc()
						.orderByTaskCreateTime().desc();
				List<Task> unsignedTasks = claimQuery.list();
				tasks.addAll(todoList);
				tasks.addAll(unsignedTasks);
				for (Task task : tasks) {
					String processInstanceId = task.getProcessInstanceId();
					ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).active().singleResult();
					String businessKey = processInstance.getBusinessKey();
					Leave leave = leaveDAO.getLeave(Integer.parseInt(businessKey));
					leave.setTask(task);
					leave.setProcessInstance(processInstance);
					leave.setProcessDefinition(getProcessDefinition(processInstanceId));
					results.add(leave);
				}
			}catch(Exception e) {
				log.error("查询待办任务失败e:"+e);
			}
		}
		return results;
	}
	
	/**
	 * 查询流程定义对象
	 * @author xuelinyi
	 */
	public ProcessDefinition getProcessDefinition(String processDefinitionId) {
		log.debug("根据ProcessDefinitionId查询流程定义对象："+processDefinitionId);
		if(null != processDefinitionId) {
			try {
				return repositoryService.createProcessDefinitionQuery().processDefinitionId(processDefinitionId).singleResult();
			}catch(Exception e) {
				log.error("查询流程定义对象失败！"+e);
			}
		}
		return null;
	}

	public Leave getLeave() {
		return null;
	}
}







