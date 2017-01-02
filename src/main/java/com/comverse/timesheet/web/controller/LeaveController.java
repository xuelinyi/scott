package com.comverse.timesheet.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.activiti.engine.ActivitiException;
import org.activiti.engine.RuntimeService;
import org.activiti.engine.TaskService;
import org.activiti.engine.identity.User;
import org.activiti.engine.runtime.ProcessInstance;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.comverse.timesheet.web.bean.leave.Leave;
import com.comverse.timesheet.web.business.LeaveWorkflowBusiness;


@Controller
@RequestMapping(value = "/leave")
public class LeaveController {
	@Autowired
	private HttpSession session;
	@Autowired
    protected LeaveWorkflowBusiness leaveWorkflowService;

    @Autowired
    protected RuntimeService runtimeService;
    @Autowired
    protected TaskService taskService;
	private static final Logger log = Logger.getLogger(LeaveController.class);
	@RequestMapping("apply")
	public String createForm(Model model) {
		model.addAttribute("leave", new Leave());
		return "/oa/leave/leaveApply";
	}
	/**
	   * 启动请假流程
	   * leave:1:4含义：流程key，版本号，流程定义在数据库中的id
	   * @param leave
	   */
	  @RequestMapping(value = "start", method = RequestMethod.POST, consumes = {"application/json"})
	  @ResponseBody
	  public String startWorkflow(@RequestBody Leave leave) {
		String message = "没有部署流程，请在[工作流]->[流程管理]页面点击<重新部署流程>";
	    try {
	      leave.setUserId(((User)session.getAttribute("activity_user")).getId());
	      Map<String, Object> variables = new HashMap<String, Object>();
	      ProcessInstance processInstance = leaveWorkflowService.startWorkflow(leave, variables);
	      message = "流程已启动，流程ID：" +processInstance.getId();
	    } catch (ActivitiException e) {
	      if (e.getMessage().indexOf("no processes deployed with key") != -1) {
	        log.warn("没有部署流程!", e);
	        message = "没有部署流程，请在[工作流]->[流程管理]页面点击<重新部署流程>";
	      } else {
	    	  log.error("启动请假流程失败：", e);
	    	  message = "系统内部错误！";
	      }
	    } catch (Exception e) {
	    	log.error("启动请假流程失败：", e);
	    	message = "系统内部错误！";
	    }
	    return message;
	  }
	  
	  /**
	   * 任务列表
	   * @author xuelinyi
	   * @return
	   */
	  @RequestMapping(value="task")
	  public String taskList(Model model) {
		  log.debug("查询当前登录者的任务列表");
		  String userId = ((User)session.getAttribute("activity_user")).getId();
		  List<Leave> leaveList = leaveWorkflowService.findTodoTasks(userId);
		  model.addAttribute("leaveList", leaveList);
		  return "/oa/leave/taskList";
	  }
	  /**
	   * 签收任务
	   * @author Administrator
	   */
	  @RequestMapping(value="claim/{id}")
	  public String claim(@PathVariable("id") String taskId, HttpSession session, RedirectAttributes redirectAttributes) {
		 log.debug("签收任务taskId："+taskId);
		 if(null != taskId) {
			String userId = ((User)session.getAttribute("activity_user")).getId();
			taskService.claim(taskId, userId);
		 }
		 return "redirect:/leave/task";
	  }
	  /**
	   * 读取详细数据
	   * @author xuelinyi
	   */
	  @RequestMapping(value="detail/{id}")
	  @ResponseBody
	  public Leave detail(@PathVariable("id")int id) {
		  log.debug("读取任务的详细信息。taskId"+id);
		  if(0 != id) {
			  return leaveWorkflowService.getLeave(id);
		  }
		  return new Leave();
	  }
	  /**
	   * 读取详细数据
	   * @author xuelinyi
	   */
	  @RequestMapping(value="detail-with-vars/{id}/{taskId}")
	  @ResponseBody
	  public Leave getLeaveWithVars(@PathVariable("id")int id,@PathVariable("taskId") String taskId) {
		  log.debug("读取详细数据id:"+id);
		  log.debug("taskId:"+taskId);
		  Leave leave = new Leave();
		  if((0!=id)&&(null != taskId)) {
			  leave = leaveWorkflowService.getLeave(id);
			  Map<String, Object> variables = taskService.getVariables(taskId);
			  leave.setVariables(variables);
		  }
		  return leave;
	  }
	  /**
	   * 完成任务
	   */
	  @RequestMapping(value = "complete/{id}", method = { RequestMethod.POST, RequestMethod.GET })
	  @ResponseBody
	  public String complete(@PathVariable("id") String taskId, com.comverse.timesheet.web.util.Variable var) {
		  try {
		      Map<String, Object> variables = var.getVariableMap();
		      taskService.complete(taskId, variables);
		      return "success";
		    } catch (Exception e) {
		      log.error("error on complete task {}, variables={}"+new Object[] { taskId, var.getVariableMap(), e });
		      return "error";
		    }
	  }
	  /**
	   * 读取运行中的流程
	   * @author 12440
	   */
	  @RequestMapping(value="running")
	  public String runningList(Model model) {
		  log.debug("读取运行的流程。");
		  model.addAttribute("leaveList", leaveWorkflowService.findRunningTask());
		  return "/oa/leave/running";
	  }
	  /**
	   * 读取已结束的流程
	   * @author 12440
	   */
	  @RequestMapping(value="finished")
	  public String finishedList(Model model) {
		  log.debug("读取已结束的流程。");
		  model.addAttribute("leaveList", leaveWorkflowService.findFinishedProcessInstaces());
		  return "/oa/leave/finished";
	  }
}









