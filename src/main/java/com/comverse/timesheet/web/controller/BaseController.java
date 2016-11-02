package com.comverse.timesheet.web.controller;

import javax.servlet.http.HttpSession;



import org.springframework.beans.factory.annotation.Autowired;

import com.comverse.timesheet.web.SystemEnum;
import com.comverse.timesheet.web.bean.system.AdminLog;
import com.comverse.timesheet.web.business.ISystemBusiness;

public class BaseController{
	@Autowired
	private ISystemBusiness systemBusiness;
	@Autowired
	private HttpSession session;
	
	/**
	 * 从session中获取登陆的用户名 
	 * @return
	 */
	public String getAccount(){
		return (String)session.getAttribute("user");
	}
	
	/**
	 * 从session中获取登陆的IP地址 
	 * @return
	 */
	public String getClientIp(){
		return (String)session.getAttribute("clientIp");
	}	
	
	/**
	 * 添加“调试” 管理信息 
	 * @return
	 */
	public void debug(String log){
		
		systemBusiness.addAdminLog(new AdminLog(getAccount(), getClientIp(), log, SystemEnum.DEBUG.getLogFlag()));
	}
	
	/**
	 * 添加“信息” 管理信息 
	 * @return
	 */
	public void info(String log){
		systemBusiness.addAdminLog(new AdminLog(getAccount(), getClientIp(), log, SystemEnum.INFO.getLogFlag()));
	}
	
	/**
	 * 添加“告警” 管理信息 
	 * @return
	 */
	public void warn(String log){
		systemBusiness.addAdminLog(new AdminLog(getAccount(), getClientIp(), log, SystemEnum.WARN.getLogFlag()));
	}
	
	/**
	 * 添加“错误” 管理信息 
	 * @return
	 */
	public void error(String log){
		systemBusiness.addAdminLog(new AdminLog(getAccount(), getClientIp(), log, SystemEnum.ERROR.getLogFlag()));
	}
	
	/**
	 * 添加“成功” 管理信息 
	 * @return
	 */
	public void success(String log){
		systemBusiness.addAdminLog(new AdminLog(getAccount(), getClientIp(), log, SystemEnum.SUCCESS.getLogFlag()));
	}
	
	/**
	 * 添加“失败” 管理信息 
	 * @return
	 */
	public void fail(String log){
		systemBusiness.addAdminLog(new AdminLog(getAccount(), getClientIp(), log, SystemEnum.FAIL.getLogFlag()));
	}	
	
}









