package com.comverse.timesheet.web.bean.leave;

import java.io.Serializable;
import java.util.Date;
import java.util.Map;

import org.activiti.bpmn.model.Task;
import org.activiti.engine.history.HistoricProcessInstance;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.runtime.ProcessInstance;

public class Leave implements Serializable {

	private static final long serialVersionUID = 3639928826696478170L;
	private int id;
	private String processInstanceId;
	private String userId;
	private Date startTime;
	private Date endTime;
	private Date realityStartTime;
	private Date realityEndTime;
	private Date applyTime;
	private String leaveType;
	private String reason;
	
	private Task task;
	private Map<String, Object> variables;
	private ProcessInstance processInstance;
	private HistoricProcessInstance historicProcessInstance;
	
	private ProcessDefinition processDefinition;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getProcessInstanceId() {
		return processInstanceId;
	}

	public void setProcessInstanceId(String processInstanceId) {
		this.processInstanceId = processInstanceId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getRealityStartTime() {
		return realityStartTime;
	}

	public void setRealityStartTime(Date realityStartTime) {
		this.realityStartTime = realityStartTime;
	}

	public Date getRealityEndTime() {
		return realityEndTime;
	}

	public void setRealityEndTime(Date realityEndTime) {
		this.realityEndTime = realityEndTime;
	}

	public Date getApplyTime() {
		return applyTime;
	}

	public void setApplyTime(Date applyTime) {
		this.applyTime = applyTime;
	}

	public String getLeaveType() {
		return leaveType;
	}

	public void setLeaveType(String leaveType) {
		this.leaveType = leaveType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public Task getTask() {
		return task;
	}

	public void setTask(Task task) {
		this.task = task;
	}

	public Map<String, Object> getVariables() {
		return variables;
	}

	public void setVariables(Map<String, Object> variables) {
		this.variables = variables;
	}

	public ProcessInstance getProcessInstance() {
		return processInstance;
	}

	public void setProcessInstance(ProcessInstance processInstance) {
		this.processInstance = processInstance;
	}

	public HistoricProcessInstance getHistoricProcessInstance() {
		return historicProcessInstance;
	}

	public void setHistoricProcessInstance(HistoricProcessInstance historicProcessInstance) {
		this.historicProcessInstance = historicProcessInstance;
	}

	public ProcessDefinition getProcessDefinition() {
		return processDefinition;
	}

	public void setProcessDefinition(ProcessDefinition processDefinition) {
		this.processDefinition = processDefinition;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Leave [id=");
		builder.append(id);
		builder.append(", processInstanceId=");
		builder.append(processInstanceId);
		builder.append(", userId=");
		builder.append(userId);
		builder.append(", startTime=");
		builder.append(startTime);
		builder.append(", endTime=");
		builder.append(endTime);
		builder.append(", realityStartTime=");
		builder.append(realityStartTime);
		builder.append(", realityEndTime=");
		builder.append(realityEndTime);
		builder.append(", applyTime=");
		builder.append(applyTime);
		builder.append(", leaveType=");
		builder.append(leaveType);
		builder.append(", reason=");
		builder.append(reason);
		builder.append(", task=");
		builder.append(task);
		builder.append(", variables=");
		builder.append(variables);
		builder.append(", processInstance=");
		builder.append(processInstance);
		builder.append(", historicProcessInstance=");
		builder.append(historicProcessInstance);
		builder.append(", processDefinition=");
		builder.append(processDefinition);
		builder.append("]");
		return builder.toString();
	}
	
	
}
