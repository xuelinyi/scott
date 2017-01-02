package com.comverse.timesheet.web.util;

import java.util.List;
import java.util.Map;


import org.activiti.bpmn.model.Activity;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.impl.RepositoryServiceImpl;
import org.activiti.engine.impl.persistence.entity.ProcessDefinitionEntity;
import org.activiti.engine.impl.pvm.process.ActivityImpl;
import org.activiti.engine.repository.ProcessDefinition;
import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.common.collect.Maps;

public class ProcessDefinitionCache {
	private static final Logger log = Logger.getLogger(ProcessDefinitionCache.class);
	private static Map<String, ProcessDefinition> map = Maps.newHashMap();
	private static Map<String, List<ActivityImpl>> activities = Maps.newHashMap();

	private static Map<String, ActivityImpl> singleActivity = Maps.newHashMap();
	@Autowired
	private static RepositoryService repositoryService;
	public static String getActivityName(String processDefinitionId,String activitiId) {
		log.debug("从流程缓存中读取名称。processDefinitionId:"+processDefinitionId);
		log.debug("activitiId :"+activitiId);
		ActivityImpl activityImpl = getActivity(processDefinitionId, activitiId);
		if(null != activityImpl) {
			return ObjectUtils.toString(activityImpl.getProperty("name"));
		}
		return null;
	}
	public static ActivityImpl getActivity(String processDefinitionId,String activitiId) {
		log.debug("从流程缓存中读取名称。processDefinitionId:"+processDefinitionId);
		log.debug("activitiId :"+activitiId);
		ProcessDefinition ProcessDefinition = get(processDefinitionId);
		if (ProcessDefinition != null) {
	      ActivityImpl activityImpl = singleActivity.get(processDefinitionId + "_" + activitiId);
	      if (activityImpl != null) {
	        return activityImpl;
	      }
	    }
		return null;
	}
	public static ProcessDefinition get(String processDefinitionId) {
		ProcessDefinition processDefinition = map.get(processDefinitionId);
		if(null == processDefinition ) {
			processDefinition = (ProcessDefinitionEntity) ((RepositoryServiceImpl)repositoryService).getProcessDefinition(processDefinitionId);
			if(null != processDefinition) {
				put(processDefinitionId, processDefinition);
			}
		}
		return processDefinition;
	}
	public static void put(String processDefinitionId,ProcessDefinition processDefinition) {
		map.put(processDefinitionId, processDefinition);
		ProcessDefinitionEntity pde = (ProcessDefinitionEntity)processDefinition;
		activities.put(processDefinitionId, pde.getActivities());
		for (ActivityImpl activityImpl : pde.getActivities()) {
			 singleActivity.put(processDefinitionId + "_" + activityImpl.getId(), activityImpl);
		}
	}
	public static void setRepositoryService(RepositoryService repositoryService) {
	    ProcessDefinitionCache.repositoryService = repositoryService;
	}
}
