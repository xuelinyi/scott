package com.comverse.timesheet.web.business;

import java.util.List;
import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;

import com.comverse.timesheet.web.bean.leave.Leave;


public interface LeaveWorkflowBusiness {
	public ProcessInstance startWorkflow(Leave entity, Map<String, Object> variables);
	public List<Leave> findTodoTasks(String userId);
	public Leave getLeave(int id);
	public boolean updateLeave(Leave entity);
	public List<Leave> findRunningTask();
	public List<Leave> findFinishedProcessInstaces();
}
