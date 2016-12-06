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
	      leave.setUserId(session.getAttribute("user").toString());
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
		  String userId = session.getAttribute("user").toString();
		  List<Leave> leaveList = leaveWorkflowService.findTodoTasks(userId);
		  model.addAttribute("leaveList", leaveList);
		  return "/oa/leave/taskList";
	  }
}









