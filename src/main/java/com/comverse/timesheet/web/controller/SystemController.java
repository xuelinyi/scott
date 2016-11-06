package com.comverse.timesheet.web.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comverse.timesheet.web.bean.TestTable;
import com.comverse.timesheet.web.bean.system.Account;
import com.comverse.timesheet.web.bean.system.AccountIp;
import com.comverse.timesheet.web.bean.system.Permission;
import com.comverse.timesheet.web.bean.system.Role;
import com.comverse.timesheet.web.bean.system.SysConfigure;
import com.comverse.timesheet.web.business.ISystemBusiness;
import com.comverse.timesheet.web.business.TestBusiness;
import com.comverse.timesheet.web.dto.AccountDTO;
import com.comverse.timesheet.web.dto.AdminLogDTO;
import com.comverse.timesheet.web.dto.AuthorAndBookDTO;
import com.comverse.timesheet.web.dto.RoleDTO;
import com.comverse.timesheet.web.util.PageList;

@Controller
public class SystemController extends BaseController{
	private static final Logger log = Logger.getLogger(SystemController.class);
	@Autowired
    private ISystemBusiness systemBusiness; 
	@RequestMapping("/system/accountList")
	public String jumpAccountPage(HttpServletRequest request, ModelMap modelMap) throws Exception {
		log.debug("跳转用户列表界面");
		List<AccountDTO> accountList=systemBusiness.findAccount();
	    modelMap.addAttribute("accountList", accountList);
		return "accountList";
	}
	@RequestMapping("/system/getAccount")
	@ResponseBody
	public Account getAccount(@RequestParam(value = "accountId", required = true)int accountId,ModelMap modelMap) {
		log.debug("根据账户ID查找对应的账户信息accountId:" +accountId);
		if(0!=accountId) {
			return systemBusiness.getAccount(accountId);
		}
		return null;
	}
	@RequestMapping(value="/system/addAccount", method = RequestMethod.POST, consumes = {"application/json"})
	@ResponseBody
	public boolean addAccount(@RequestBody Account account) {
		log.debug("增加账户信息。" +account);
		boolean addResult = false;
		if((null!=account)&&(0==account.getId())) {
			addResult = systemBusiness.addAccount(account);
			if(addResult) 
				success("增加账户信息成功。account:"+account);
			else
				fail("增加账户信息失败。account:"+account);
		}
		return addResult;
	}
	@RequestMapping(value="/system/updateAccount", method = RequestMethod.POST, consumes = {"application/json"})
	@ResponseBody
	public boolean updateAccount(@RequestBody Account account) {
		log.debug("编辑账户信息。" +account);
		boolean updateResult = false;
		if((null!=account)&&(0!=account.getId())) {
			updateResult = systemBusiness.updateAccount(account);
			if(updateResult) 
				success("编辑账户信息成功。account:"+account);
			else
				fail("编辑账户信息失败。account:"+account);
		}
		return updateResult;
	}
	@RequestMapping("/system/deleteAccount")
	@ResponseBody
	public boolean deleteAccount(int accountId) {
		log.debug("删除账户信息。" +accountId);
		boolean deleteResult = false;
		if(0!=accountId) {
			deleteResult = systemBusiness.deleteAccount(accountId);
			if(deleteResult) 
				success("删除账户信息成功。accountId:"+accountId);
			else
				fail("删除账户信息失败。accountId:"+accountId);
		}
		return deleteResult;
	}
	@RequestMapping("/system/roleList")
	public String jumpRolePage(ModelMap modelMap) {
		log.debug("跳转角色管理页面。");
		List<RoleDTO> roleList = systemBusiness.findRole();
		modelMap.addAttribute("roleList", roleList);
		return "roleList";
	}
	@RequestMapping("/system/findRoleList")
	@ResponseBody
	public List<RoleDTO> findRoleList() {
		log.debug("查询所有的角色信息");
		return systemBusiness.findRole();
	}
	@RequestMapping("/system/getRole")
	@ResponseBody
	public Role getRole(@RequestParam(value = "roleId", required = true)int roleId,ModelMap modelMap) {
		log.debug("根据角色ID查找对应的角色信息roleId:" +roleId);
		if(0!=roleId) {
			return systemBusiness.getRole(roleId);
		}
		return null;
	}
	@RequestMapping(value="/system/addRole", method = RequestMethod.POST, consumes = {"application/json"})
	@ResponseBody
	public boolean addRole(@RequestBody Role role) {
		log.debug("增加角色信息。" +role);
		boolean addResult = false;
		if((null!=role)&&(0==role.getId())) {
			addResult = systemBusiness.addRole(role);
			if(addResult) 
				success("增加角色信息成功。role:"+role);
			else
				fail("增加角色信息失败。role:"+role);
		}
		return addResult;
	}
	@RequestMapping(value="/system/updateRole", method = RequestMethod.POST, consumes = {"application/json"})
	@ResponseBody
	public boolean updateRole(@RequestBody Role role) {
		log.debug("编辑角色信息。" +role);
		boolean updateResult = false;
		if((null!=role)&&(0!=role.getId())) {
			updateResult = systemBusiness.updateRole(role);
			if(updateResult) 
				success("编辑角色信息成功。role:"+role);
			else
				fail("编辑角色信息失败。role:"+role);
		}
		return updateResult;
	}
	@RequestMapping("/system/deleteRole")
	@ResponseBody
	public boolean deleteRole(int roleId) {
		log.debug("删除角色信息。" +roleId);
		boolean deleteResult = false;
		if(0!=roleId) {
			deleteResult = systemBusiness.deleteRole(roleId);
			if(deleteResult) 
				success("删除角色信息成功。roleId:"+roleId);
			else
				fail("删除角色信息失败。roleId:"+roleId);
		}
		return deleteResult;
	}
	@RequestMapping("/system/accountIpList")
	public String jumpAccountIpPage(HttpServletRequest request, ModelMap modelMap) throws Exception {
		log.debug("跳转用户Ip列表界面");
		List<AccountIp> accountIpList=systemBusiness.findAccountIp();
	    modelMap.addAttribute("accountIpList", accountIpList);
		return "accountIpList";
	}
	@RequestMapping("/system/getAccountIp")
	@ResponseBody
	public AccountIp getAccountIp(@RequestParam(value = "accountIpId", required = true)int accountIpId,ModelMap modelMap) {
		log.debug("根据账户IpID查找对应的账户信息accountId:" +accountIpId);
		if(0!=accountIpId) {
			return systemBusiness.getAccountIp(accountIpId);
		}
		return null;
	}
	@RequestMapping(value="/system/addAccountIp", method = RequestMethod.POST, consumes = {"application/json"})
	@ResponseBody
	public boolean addAccountIp(@RequestBody AccountIp accountIp) {
		log.debug("增加账户Ip信息。" +accountIp);
		boolean addResult = false;
		if((null!=accountIp)&&(0==accountIp.getId())) {
			addResult = systemBusiness.addAccountIp(accountIp);
			if(addResult) 
				success("增加账户Ip信息成功。account:"+accountIp);
			else
				fail("增加账户Ip信息失败。account:"+accountIp);
		}
		return addResult;
	}
	@RequestMapping(value="/system/updateAccountIp", method = RequestMethod.POST, consumes = {"application/json"})
	@ResponseBody
	public boolean updateAccountIp(@RequestBody AccountIp accountIp) {
		log.debug("编辑账户信息。" +accountIp);
		boolean updateResult = false;
		if((null!=accountIp)&&(0!=accountIp.getId())) {
			updateResult = systemBusiness.updateAccountIp(accountIp);
			if(updateResult) 
				success("编辑账户Ip信息成功。account:"+accountIp);
			else
				fail("编辑账户Ip信息失败。account:"+accountIp);
		}
		return updateResult;
	}
	@RequestMapping("/system/deleteAccountIp")
	@ResponseBody
	public boolean deleteAccountIp(int accountIpId) {
		log.debug("删除账户Ip信息。" +accountIpId);
		boolean deleteResult = false;
		if(0!=accountIpId) {
			deleteResult = systemBusiness.deleteAccountIp(accountIpId);
			if(deleteResult) 
				success("删除账户Ip信息成功。accountId:"+accountIpId);
			else
				fail("删除账户Ip信息失败。accountId:"+accountIpId);
		}
		return deleteResult;
	}
	@RequestMapping("/system/findAccountDTO")
	@ResponseBody
	public List<AccountDTO> findAccountDTO() {
		log.debug("ajax查询所有的用户信息");
		return systemBusiness.findAccount();
	}
	
	@RequestMapping("/system/findPermissionList")
	@ResponseBody
	public List<Permission> findPermissionList() {
		log.debug("查询所有的权限信息");
		return systemBusiness.findPermission();
	}
	@RequestMapping("/system/findAdminLogList")
	public String findAdminLogList(HttpServletRequest request, ModelMap modelMap) throws Exception {
		log.debug("跳转系统日志界面");
		List<AdminLogDTO> adminLogList=systemBusiness.findAdminLog();
	    modelMap.addAttribute("adminLogList", adminLogList);
		return "adminLogList";
	}
	
	@RequestMapping("/system/findSysConfigureList")
	public String findSysConfigureList(HttpServletRequest request, ModelMap modelMap) throws Exception {
		log.debug("跳转系统参数配置界面");
		List<SysConfigure> sysConfigureList=systemBusiness.findSysConfigureList();
	    modelMap.addAttribute("sysConfigureList", sysConfigureList);
		return "sysConfigureList";
	}
	@RequestMapping(value="/system/updateSysConfigure", method = RequestMethod.POST, consumes = {"application/json"})
	@ResponseBody
	public boolean updateSysConfigure(@RequestBody SysConfigure sysConfigure) throws Exception {
		log.debug("编辑系统参数配置");
		boolean result = false;
		if(null != sysConfigure) {
			result = systemBusiness.updateSysconfigure(sysConfigure);
		}
		return result;
	}
	@RequestMapping(value="/system/getSysConfigure")
	@ResponseBody
	public SysConfigure getSysConfigure(String sysConfigureId) throws Exception {
		log.debug("根据ID获取系统参数信息sysConfigureId : " + sysConfigureId);
		if(null != sysConfigureId) {
			return systemBusiness.getSysConfigure(sysConfigureId);
		}
		return null;
	}
    @RequestMapping(value = "/system/forgotPassword")
    @ResponseBody
	public int forgotPassword(String email) {
		log.debug("#index");
		int result = 10001;
		if(null != email) {
			result = systemBusiness.forgotPassword(email);
		}
		return result;
	}
}
