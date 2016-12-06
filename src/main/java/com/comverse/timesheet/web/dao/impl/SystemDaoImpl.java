package com.comverse.timesheet.web.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.comverse.timesheet.web.bean.system.Account;
import com.comverse.timesheet.web.bean.system.AccountIp;
import com.comverse.timesheet.web.bean.system.AccountRoleRelation;
import com.comverse.timesheet.web.bean.system.AdminLog;
import com.comverse.timesheet.web.bean.system.Permission;
import com.comverse.timesheet.web.bean.system.Role;
import com.comverse.timesheet.web.bean.system.RolePermissionRelation;
import com.comverse.timesheet.web.bean.system.SysConfigure;
import com.comverse.timesheet.web.dao.ISystemDao;
import com.comverse.timesheet.web.dto.AccountDTO;
import com.comverse.timesheet.web.dto.AdminLogDTO;
import com.comverse.timesheet.web.dto.RoleDTO;
import com.comverse.timesheet.web.util.BasicSqlSupport;
@Repository
public class SystemDaoImpl extends BasicSqlSupport implements ISystemDao{
	private static final Logger log = Logger.getLogger(SystemDaoImpl.class);

	public List<AccountDTO> findAccount() throws Exception {
		log.debug("查询所有的用户信息");
		List<Account> accountList = session.selectList("mybatis.mapper.System.selectAccountByNull");
		List<AccountDTO> accountDTOList = new ArrayList<AccountDTO>();
		if((null != accountList)&&(0!=accountList.size())) {
			for (Account account : accountList) {
				accountDTOList.add(Account.conversionAccount(account));
			}
		}
		return accountDTOList;
	}
	public List<Account> findAccountByEmail(String email) throws Exception {
		log.debug("根据邮箱查找对应的用户信息email:"+email);
		if(null != email) {
			return session.selectList("mybatis.mapper.System.selectAccountByEmail",email);
		}
		return Collections.EMPTY_LIST;
	}
	public Account getAccount(int accountId) throws Exception {
		log.debug("根据账户ID查询对应的账户信息accountId:"+accountId);
		if(0 != accountId) {
			return session.selectOne("mybatis.mapper.System.selectAccountById",accountId);
		}
		return null;
	}

	public boolean addAccount(Account account) throws Exception {
		log.debug("增加账户信息.account:" + account);
		if((null != account)&&(0==account.getId())) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			account.setCreateTime(formatter.format(new Date()));
			int addAccountResult = session.insert("mybatis.mapper.System.addAccount", account);
			int addAccountActivityResult = session.insert("mybatis.mapper.System.addAccountOfActivity", account);
			if((addAccountResult > 0)&&(addAccountActivityResult>0)) {
				return true;
			}
		}
		return false;
	}

	public boolean updateAccount(Account account) throws Exception {
		log.debug("修改账户信息account:"+account);
		if((null != account)&&(0!=account.getId())) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				account.setCreateTime(formatter.format(new Date()));
				session.update("mybatis.mapper.System.updateAccount", account);
				return true;
			}catch(Exception e) {
				log.error("修改账户信息e:"+e);
				return false;
			}
		}
		return false;
	}

	public boolean deleteAccountRoleRelation(int accountId) throws Exception  {
		log.debug("根据账户ID删除对应的角色信息accountId ： " +accountId);
		if(0!=accountId) {
			try {
				session.delete("mybatis.mapper.System.deleteAccountRoleRelation",accountId);
				return true;
			}catch(Exception e) {
				log.error("根据账户ID删除对应的角色信息失败e:"+e);
				return false;
			}
		}
		return false;
	}
	
	public boolean addAccountRoleRelation(List<AccountRoleRelation> accountRoleRelationList) throws Exception  {
		log.debug("给账户增加角色信息。accountRoleRelationList ：" + accountRoleRelationList);
		if((null != accountRoleRelationList)&&(0 != accountRoleRelationList.size())) {
			int addAccountRoleRelaResult = session.insert("mybatis.mapper.System.addAccountRoleRelation", accountRoleRelationList);
			if(addAccountRoleRelaResult > 0) {
				return true;
			}
		}
		return false;
	}
	
	public boolean deleteAccount(int accountId) throws Exception {
		log.debug("根据账户ID删除对应的账户信息accountID:"+accountId);
		if(0!=accountId) {
			int result = session.delete("mybatis.mapper.System.deleteAccount", accountId);
			if(result > 0) {
				return true;
			}
		}
		return false;
	}

	public List<RoleDTO> findRole() throws Exception {
		log.debug("查询所有的角色信息");
		List<Role> roleList = session.selectList("mybatis.mapper.System.selectRoleByNull");
		List<RoleDTO> roleDTOList = new ArrayList<RoleDTO>();
		if((null != roleList)&&(0!=roleList.size())) {
			for (Role role : roleList) {
				roleDTOList.add(Role.conversionRole(role));
			}
		}
		return roleDTOList;
	}

	public Role getRole(int roleId) throws Exception {
		log.debug("根据账户ID查询对应的角色信息roleId:"+roleId);
		if(0 != roleId) {
			return session.selectOne("mybatis.mapper.System.selectRoleById",roleId);
		}
		return null;
	}

	public boolean addRole(Role role) throws Exception {
		log.debug("增加角色信息.role:" + role);
		if((null != role)&&(0==role.getId())) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			role.setCreateTime(formatter.format(new Date()));
			int result = session.insert("mybatis.mapper.System.addRole", role);
			if(result > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean updateRole(Role role) throws Exception {
		log.debug("修改角色信息role:"+role);
		if((null != role)&&(0!=role.getId())) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			role.setCreateTime(formatter.format(new Date()));
			int result = session.update("mybatis.mapper.System.updateRole", role);
			if(result > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean deleteRole(int roleId) throws Exception {
		log.debug("根据账户ID删除对应的角色信息roleId:"+roleId);
		if(0!=roleId) {
			int result = session.delete("mybatis.mapper.System.deleteRole", roleId);
			if(result > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean deleteRolePermissionRelation(int roleId) throws Exception  {
		log.debug("根据账户ID删除对应的权限关联信息roleId ： " +roleId);
		if(0!=roleId) {
			try{
				session.delete("mybatis.mapper.System.deleteRolePermissionRelation",roleId);
				return true;
			}catch(Exception e) {
				log.error("删除权限信息失败。e:"+e);
				return false;
			}
		}
		return false;
	}
	
	public boolean addRolePermissionRelation(List<RolePermissionRelation> rolePermissionRelationList) throws Exception  {
		log.debug("给角色增加权限信息。rolePermissionRelationList ：" + rolePermissionRelationList);
		if((null != rolePermissionRelationList)&&(0 != rolePermissionRelationList.size())) {
			int addAccountRoleRelaResult = session.insert("mybatis.mapper.System.addRolePermissionRelation", rolePermissionRelationList);
			if(addAccountRoleRelaResult > 0) {
				return true;
			}
		}
		return false;
	}
	
	public List<AccountIp> findAccountIp() throws Exception {
		log.debug("查询所有的用户IP信息");
		return session.selectList("mybatis.mapper.System.selectAccountIpByNull");
	}

	public AccountIp getAccountIp(int accountIpId) throws Exception {
		log.debug("根据账户ID查询对应的用户IP信息accountIpId:"+accountIpId);
		if(0 != accountIpId) {
			return session.selectOne("mybatis.mapper.System.selectAccountIpById",accountIpId);
		}
		return null;
	}

	public boolean addAccountIp(AccountIp accountIp) throws Exception {
		log.debug("增加用户IP信息.accountIp:" + accountIp);
		if((null != accountIp)&&(0==accountIp.getId())) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			accountIp.setCreateTime(formatter.format(new Date()));
			int result = session.insert("mybatis.mapper.System.addAccountIp", accountIp);
			if(result > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean updateAccountIp(AccountIp accountIp) throws Exception {
		log.debug("修改用户IP信息accountIp:"+accountIp);
		if((null != accountIp)&&(0!=accountIp.getId())) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			accountIp.setCreateTime(formatter.format(new Date()));
			int result = session.update("mybatis.mapper.System.updateAccountIp", accountIp);
			if(result > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean deleteAccountIp(int accountIpId) throws Exception {
		log.debug("根据账户ID删除对应的用户IP信息accountIpId:"+accountIpId);
		if(0!=accountIpId) {
			int result = session.delete("mybatis.mapper.System.deleteAccountIp", accountIpId);
			if(result > 0) {
				return true;
			}
		}
		return false;
	}

	public List<Permission> findPermission() throws Exception {
		log.debug("查询所有的权限信息");
		return session.selectList("mybatis.mapper.System.selectPermissionByNull");
	}
	
	public void addAdminLog(AdminLog adminLog)throws Exception{
		log.debug("查询所有的权限信息");
		if(null != adminLog) {
			session.insert("mybatis.mapper.System.addAdminLog",adminLog);
		}
	}

	public List<AdminLogDTO> findAdminLog() throws Exception {
		log.debug("查询所有的日志信息");
		List<AdminLog> adminLogList = session.selectList("mybatis.mapper.System.selectAdminLogByNull");
		List<AdminLogDTO> adminLogDTOList = new ArrayList<AdminLogDTO>();
		if((null != adminLogList)&&(0 != adminLogList.size())){
			for (AdminLog adminLog : adminLogList) {
				adminLogDTOList.add(AdminLog.conversionAdminLog(adminLog));
			}
		}
		return adminLogDTOList;
	}

	public boolean updateSysconfigure(SysConfigure sysConfigure)
			throws Exception {
		log.debug("修改系统配置信息。sysConfigure：" +sysConfigure);
		if((null != sysConfigure)&&(null!=sysConfigure.getId())) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			sysConfigure.setUpdateTime(formatter.format(new Date()));
			int count = session.update("mybatis.mapper.System.updateSysconfig", sysConfigure);
			if(count>0) {
				return true;
			}
		}
		return false;
	}

	public List<SysConfigure> findSysConfigureList() throws Exception {
		log.debug("查询所有的系统配置信息。");
		return session.selectList("mybatis.mapper.System.selectSysconfigByNull");
	}
	public SysConfigure getSysConfigure(String sysConfigureId) throws Exception {
		log.debug("根据ID获取系统参数信息sysConfigureId : " + sysConfigureId);
		if(null!=sysConfigureId) {
			return session.selectOne("mybatis.mapper.System.selectSysconfigById", sysConfigureId);
		}
		return null;
	}
}
