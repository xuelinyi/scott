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
 * 销假后处理器
 * <p>设置销假时间</p>
 * <p>使用Spring代理，可以注入Bean，管理事物</p>
 *
 * @author HenryYan
 */
@Component
@Transactional
public class ReportBackEndProcessor implements TaskListener{
	private static final Logger log = Logger.getLogger(ReportBackEndProcessor.class);
	@Autowired
	private RuntimeService runtimeService;
	@Autowired
	private LeaveWorkflowBusiness leaveWorkflowService;
	/**
	 * 
	 */
	private static final long serialVersionUID = 8154372222973783328L;
	public void notify(DelegateTask delegateTask) {
		log.debug("销假后设置实际开始时间。delegateTask："+delegateTask);
		if(null != delegateTask) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			String processInstanceId = delegateTask.getProcessInstanceId();
			ProcessInstance processInstance = runtimeService.createProcessInstanceQuery().processInstanceId(processInstanceId).singleResult();
			Leave leave = leaveWorkflowService.getLeave(Integer.parseInt(processInstance.getBusinessKey()));
			Object realityStartTime = delegateTask.getVariable("realityStartTime");
			leave.setRealityStartTime(formatter.format((Date) realityStartTime));
			Object realityEndTime = delegateTask.getVariable("realityEndTime");
			leave.setRealityEndTime(formatter.format((Date) realityEndTime));
			leaveWorkflowService.updateLeave(leave);
		}
		
	}

}
