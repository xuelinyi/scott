package com.comverse.timesheet.web.dao.impl;

import java.util.List;

import org.apache.log4j.Logger;

import com.comverse.timesheet.web.bean.system.Account;
import com.comverse.timesheet.web.bean.system.AccountIp;
import com.comverse.timesheet.web.bean.system.AdminLog;
import com.comverse.timesheet.web.bean.system.Permission;
import com.comverse.timesheet.web.bean.system.Role;
import com.comverse.timesheet.web.dao.ISystemDao;
import com.comverse.timesheet.web.util.BasicSqlSupport;

public class SystemDaoImpl extends BasicSqlSupport implements ISystemDao{
	private static final Logger log = Logger.getLogger(SystemDaoImpl.class);

	public List<Account> findAccount() throws Exception {
		log.debug("查询所有的用户信息");
		return session.selectList("mybatis.mapper.System.selectAccountByNull");
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
			int result = session.insert("mybatis.mapper.System.addAccount", account);
			if(result > 0) {
				return true;
			}
		}
		return false;
	}

	public boolean updateAccount(Account account) throws Exception {
		log.debug("修改账户信息account:"+account);
		if((null != account)&&(0!=account.getId())) {
			int result = session.update("mybatis.mapper.System.updateAccount", account);
			if(result > 0) {
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

	public List<Role> findRole() throws Exception {
		log.debug("查询所有的角色信息");
		return session.selectList("mybatis.mapper.System.selectRoleByNull");
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

	public List<AccountIp> findAccountIp() throws Exception {
		log.debug("查询所有的用户IP信息");
		return session.selectList("mybatis.mapper.System.selectAccountIpByNull");
	}

	public Role getAccountIp(int accountIpId) throws Exception {
		log.debug("根据账户ID查询对应的用户IP信息accountIpId:"+accountIpId);
		if(0 != accountIpId) {
			return session.selectOne("mybatis.mapper.System.selectAccountIpById",accountIpId);
		}
		return null;
	}

	public boolean addAccountIp(AccountIp accountIp) throws Exception {
		log.debug("增加用户IP信息.accountIp:" + accountIp);
		if((null != accountIp)&&(0==accountIp.getId())) {
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
}
