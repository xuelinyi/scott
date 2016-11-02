package com.comverse.timesheet.web.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comverse.timesheet.web.bean.TestTable;
import com.comverse.timesheet.web.bean.system.Account;
import com.comverse.timesheet.web.business.ISystemBusiness;
import com.comverse.timesheet.web.business.TestBusiness;
import com.comverse.timesheet.web.dto.AuthorAndBookDTO;
import com.comverse.timesheet.web.util.PageList;

@Controller
public class SystemController extends BaseController{
	private static final Logger log = Logger.getLogger(SystemController.class);
	@Autowired
    private ISystemBusiness systemBusiness; 
	@RequestMapping("/system/accountList")
	public String jumpAccountPage(HttpServletRequest request, ModelMap modelMap) throws Exception {
		log.debug("跳转用户列表界面");
		List<Account> accountList=systemBusiness.findAccount();
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
	@RequestMapping("/system/addAccount")
	@ResponseBody
	public boolean addAccount(Account account) {
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
}
