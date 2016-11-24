package com.comverse.timesheet.web.business;

import java.util.Map;

import org.activiti.engine.runtime.ProcessInstance;

import com.comverse.timesheet.web.bean.leave.Leave;


public interface LeaveWorkflowBusiness {
	public ProcessInstance startWorkflow(Leave entity, Map<String, Object> variables);
}
