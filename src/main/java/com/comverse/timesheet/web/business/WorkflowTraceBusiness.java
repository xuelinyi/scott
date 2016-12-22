package com.comverse.timesheet.web.business;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;
public interface WorkflowTraceBusiness {
	public List<Map<String, Object>> traceProcess(String processInstanceId) throws Exception;
}
