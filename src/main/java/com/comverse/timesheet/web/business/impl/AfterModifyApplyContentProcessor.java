package com.comverse.timesheet.web.business.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.activiti.engine.RuntimeService;
import org.activiti.engine.delegate.DelegateTask;
import org.activiti.engine.delegate.TaskListener;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.comverse.timesheet.web.bean.leave.Leave;
import com.comverse.timesheet.web.business.LeaveWorkflowBusiness;
/**
 * 请假内容处理器
 * @author 12440
 *
 */
@Component
@Transactional
public class AfterModifyApplyContentProcessor implements TaskListener{
	private static final long serialVersionUID = -3759995935611666490L;
	private static final Logger log = Logger.getLogger(AfterModifyApplyContentProcessor.class);
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
    protected LeaveWorkflowBusiness leaveWorkflowService;
		
	public void notify(DelegateTask task) {
		log.debug("请假内容处理器调整申请。task："+task);
		if(null != task) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String processInstanceId = task.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			Leave leave = leaveWorkflowService.getLeave(new Integer(processInstance.getBusinessKey()));
			leave.setLeaveType((String) task.getVariable("leaveType"));
			leave.setStartTime(formatter.format((Date) task.getVariable("startTime")));
			leave.setEndTime(formatter.format((Date) task.getVariable("endTime")));
			leave.setReason((String) task.getVariable("reason"));
			leaveWorkflowService.updateLeave(leave);
		}
	}

}
