package com.comverse.timesheet.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.activiti.engine.ManagementService;
import org.activiti.engine.runtime.Job;
import org.activiti.engine.runtime.JobQuery;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 作业控制器
 * @author 12440
 *
 */
@Controller
@RequestMapping(value="management")
public class JobController {
	private static final Logger log = Logger.getLogger(JobController.class);
	@Autowired
	private ManagementService managementService;
	private static Map<String, String> JOB_TYPES = new HashMap<String, String>();
	static {
        JOB_TYPES.put("activate-processdefinition", "激活流程定义");
        JOB_TYPES.put("timer-intermediate-transition", "中间定时");
        JOB_TYPES.put("timer-transition", "边界定时");
        JOB_TYPES.put("timer-start-event", "定时启动流程");
        JOB_TYPES.put("suspend-processdefinition", "挂起流程定义");
        JOB_TYPES.put("async-continuation", "异步锁");
   }
	
	/**
	 * Job 列表
	 */
	@RequestMapping(value="jobList")
	public String jobList(Model model) {
		log.debug("查询JOB列表");
		JobQuery jobQuery = managementService.createJobQuery();
		List<Job> jobList = jobQuery.list();
		model.addAttribute("jobList", jobList);
		Map<String, String> exceptionStacktraces = new HashMap<String, String>();
		for (Job job : jobList) {
			if(StringUtils.isNotBlank(job.getExceptionMessage())) {
				exceptionStacktraces.put(job.getId(), managementService.getJobExceptionStacktrace(job.getId()));
			}
		}
		model.addAttribute("JOB_TYPES", JOB_TYPES);
		model.addAttribute("exceptionStacktraces", exceptionStacktraces);
		return "management/job-list";
	}
	/**
	 * 删除作业
	 */
	@RequestMapping(value="deleteJob/{jobId}")
	public String deleteJob(@PathVariable("jobId") String jobId) {
		log.debug("删除作业信息jobId:"+jobId);
		managementService.deleteJob(jobId);
		return "redirect:/management/jobList";
	}
	/**
	 * 执行作业
	 */
	@RequestMapping(value ="executeJob/{jobId}")
	public String executeJob(@PathVariable("jobId") String jobId) {
		log.debug("执行作业。jobId："+jobId);
		managementService.executeJob(jobId);
		return "redirect:/management/jobList";
	}
	/**
	 * 更改作业可重试次数
	 */
	@RequestMapping(value="changeJobRetries/{jobId}")
	public String changeRetries(@PathVariable("jobId") String jobId, @RequestParam("retries") int retries) {
		log.debug("更改作业可重试次数。jobId;"+jobId);
		log.debug("retries:"+retries);
		managementService.setJobRetries(jobId, retries);
		return "redirect:/management/jobList";
	}
	/**
	 * 读取作业的异常信息
	 */
	@RequestMapping(value="stacktraceJob/{jobId}")
	@ResponseBody
	public String getJobExceptionStacktrace(@PathVariable("jobId") String jobId) {
        return managementService.getJobExceptionStacktrace(jobId);
	}
}
