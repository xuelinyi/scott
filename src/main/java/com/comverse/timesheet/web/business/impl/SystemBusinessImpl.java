package com.comverse.timesheet.web.business.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.comverse.timesheet.web.bean.system.Account;
import com.comverse.timesheet.web.bean.system.AccountIp;
import com.comverse.timesheet.web.bean.system.AdminLog;
import com.comverse.timesheet.web.bean.system.Permission;
import com.comverse.timesheet.web.bean.system.Role;
import com.comverse.timesheet.web.business.ISystemBusiness;
import com.comverse.timesheet.web.dao.ISystemDao;
@Component
public class SystemBusinessImpl implements ISystemBusiness{
	private static final Logger log = Logger.getLogger(SystemBusinessImpl.class);
	@Resource
	private ISystemDao systemDao;
	public List<Account> findAccount() {
		log.debug("查询所有的账户信息");
		try {
			return systemDao.findAccount();
		} catch (Exception e) {
			log.error("查询所有的账户信息产生异常e:"+e);
		}
		return Collections.EMPTY_LIST;
	}

	public Account getAccount(int accountId) {
		log.debug("根据账户的ID查询对应的账户信息accountId:"+accountId);
		if(0!=accountId) {
			try {
				return systemDao.getAccount(accountId);
			} catch (Exception e) {
				log.error("根据账户的ID查询对应的账户信息产生异常e:"+e);
			}
		}
		return null;
	}

	public boolean addAccount(Account account) {
		log.debug("增加角色信息。account:" + account);
		if((null != account)&&(0 == account.getId())) {
			try {
				return systemDao.addAccount(account);
			} catch (Exception e) {
				log.error("增加角色信息产生异常e:"+e);
			}
		}
		return false;
	}

	public boolean updateAccount(Account account) {
		log.debug("编辑角色信息。account:"+account);
		if((null != account)&&(0 != account.getId())) {
			try {
				return systemDao.updateAccount(account);
			} catch (Exception e) {
				log.error("编辑角色信息产生异常e:"+e);
			}
		}
		return false;
	}

	public boolean deleteAccount(int accountId) {
		log.debug("根据账户ID删除账户信息。accountID:"+accountId);
		if(0!=accountId) {
			try {
				return systemDao.deleteAccount(accountId);
			} catch (Exception e) {
				log.error("根据账户ID删除账户信息产生异常e:"+e);
			}
		}
		return false;
	}

	public List<Role> findRole() {
		log.debug("查询所有的角色信息");
		try {
			return systemDao.findRole();
		} catch (Exception e) {
			log.error("查询所有的角色信息产生异常e:"+e);
		}
		return Collections.EMPTY_LIST;
	}

	public Role getRole(int roleId) {
		log.debug("根据角色ID获取对应的角色信息.roleID:"+roleId);
		if(0!= roleId) {
			try {
				return systemDao.getRole(roleId);
			} catch (Exception e) {
				log.error("根据角色ID获取对应的角色信息产生异常e:"+e);
			}
		}
		return null;
	}

	public boolean addRole(Role role) {
		log.debug("增加角色信息role:"+role);
		if((null != role)&&(0==role.getId())){
			try {
				return systemDao.addRole(role);
			} catch (Exception e) {
				log.error("增加角色信息产生异常e:"+e);
			}
		}
		return false;
	}

	public boolean updateRole(Role role) {
		log.debug("编辑角色信息。role:"+role);
		if((null != role)&&(0!=role.getId())) {
			try {
				return systemDao.updateRole(role);
			} catch (Exception e) {
				log.error("编辑角色信息产生异常e:"+e);
			}
		}
		return false;
	}

	public boolean deleteRole(int roleId) {
		log.debug("根据角色ID删除对应的角色信息。roleID:"+roleId);
		if(0 != roleId) {
			try {
				return systemDao.deleteRole(roleId);
			} catch (Exception e) {
				log.error("根据角色ID删除对应的角色信息产生异常e:"+e);
			}
		}
		return false;
	}

	public List<AccountIp> findAccountIp() {
		log.debug("查询所有的账户IP信息");
		try {
			return systemDao.findAccountIp();
		} catch (Exception e) {
			log.error("查询所有的账户信息产生异常e:"+e);
		}
		return Collections.EMPTY_LIST;
	}

	public Role getAccountIp(int accountIpId) {
		log.debug("根据账户IP的ID查找对应的IP信息.accountId:" + accountIpId);
		try {
			return systemDao.getAccountIp(accountIpId);
		} catch (Exception e) {
			log.error("根据账户IP的ID查找对应的IP信息产生异常e:"+e);
		}
		return null;
	}

	public boolean addAccountIp(AccountIp accountIp) {
		log.debug("增加账户IP信息。accountIp:"+accountIp);
		if((null != accountIp)&&(0==accountIp.getId())) {
			try {
				return systemDao.addAccountIp(accountIp);
			} catch (Exception e) {
				log.error("增加账户IP信息产生异常。e:"+e);
			}
		}
		return false;
	}

	public boolean updateAccountIp(AccountIp accountIp) {
		log.debug("编辑账户IP信息。accountIp:"+accountIp);
		if((null != accountIp)&&(0!=accountIp.getId())) {
			try {
				return systemDao.updateAccountIp(accountIp);
			} catch (Exception e) {
				log.error("编辑账户IP信息产生异常。e:"+e);
			}
		}
		return false;
	}

	public boolean deleteAccountIp(int accountIpId) {
		log.debug("删除账户IP信息。accountIpId:"+accountIpId);
		if(0 != accountIpId) {
			try {
				return systemDao.deleteAccountIp(accountIpId);
			} catch (Exception e) {
				log.error("根据账户IP的ID删除对应的角色信息产生异常e:"+e);
			}
		}
		return false;
	}

	public List<Permission> findPermission() {
		log.debug("查询所有的权限信息");
		try {
			return systemDao.findPermission();
		} catch (Exception e) {
			log.error("查询所有的权限信息产生异常e:"+e);
		}
		return Collections.EMPTY_LIST;
	}

	public void addAdminLog(AdminLog adminlog) {
		log.debug("增加操作日志adminlog : " + adminlog);
		if(null != adminlog)  {
			try {
				systemDao.addAdminLog(adminlog);
			} catch (Exception e) {
				log.error("增加日志失败。e:"+e);
			}
		}
		
	}

}
