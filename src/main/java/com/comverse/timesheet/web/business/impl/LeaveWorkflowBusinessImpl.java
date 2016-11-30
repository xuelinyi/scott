package com.comverse.timesheet.web.business.impl;

import java.util.Map;

import javax.annotation.Resource;

import org.activiti.engine.HistoryService;
import org.activiti.engine.IdentityService;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

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

}
