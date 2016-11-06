package com.comverse.timesheet.web.business.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Component;

import com.comverse.timesheet.web.SystemEnum;
import com.comverse.timesheet.web.bean.system.Account;
import com.comverse.timesheet.web.bean.system.AccountIp;
import com.comverse.timesheet.web.bean.system.AccountRoleRelation;
import com.comverse.timesheet.web.bean.system.AdminLog;
import com.comverse.timesheet.web.bean.system.Permission;
import com.comverse.timesheet.web.bean.system.Role;
import com.comverse.timesheet.web.bean.system.RolePermissionRelation;
import com.comverse.timesheet.web.bean.system.SysConfigure;
import com.comverse.timesheet.web.business.ISystemBusiness;
import com.comverse.timesheet.web.dao.ISystemDao;
import com.comverse.timesheet.web.dto.AccountDTO;
import com.comverse.timesheet.web.dto.AdminLogDTO;
import com.comverse.timesheet.web.dto.RoleDTO;
import com.comverse.timesheet.web.util.MD5Util;
import com.comverse.timesheet.web.util.MailUtil;
@Component
public class SystemBusinessImpl implements ISystemBusiness{
	private static final Logger log = Logger.getLogger(SystemBusinessImpl.class);
	@Resource
	private ISystemDao systemDao;
	public List<AccountDTO> findAccount() {
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
		log.debug("增加账户信息。account:" + account);
		if((null != account)&&(0 == account.getId())) {
			try {
				boolean addAccountResult = systemDao.addAccount(account);
				if(addAccountResult){
					boolean addAccountRoleRelaResult = systemDao.addAccountRoleRelation(accountToAccountRoleRelation(account));
					if(addAccountRoleRelaResult) {
						return true;
					}
				}
			} catch (Exception e) {
				log.error("增加账户信息产生异常e:"+e);
			}
		}
		return false;
	}
	private List<AccountRoleRelation> accountToAccountRoleRelation(Account account) {
		log.debug("将account转化为与role的关联关系。account:"+account);
		List<AccountRoleRelation> accountRoleRelationList = new ArrayList<AccountRoleRelation>();
		if(null != account) {
			List<Role>  roleList = account.getRoleList();
			if(null != roleList) {
				for (Role role : roleList) {
					AccountRoleRelation AccountRoleRelation = new AccountRoleRelation(account, role);
					accountRoleRelationList.add(AccountRoleRelation);
				}
			}
		}
		return accountRoleRelationList;
	}
	public boolean updateAccount(Account account) {
		log.debug("编辑角色信息。account:"+account);
		if((null != account)&&(0 != account.getId())) {
			try {
				boolean updateAccountResult = systemDao.updateAccount(account);
				if(updateAccountResult) {
					boolean deleteAccountRoleRelationResult = systemDao.deleteAccountRoleRelation(account.getId());
					if(deleteAccountRoleRelationResult) {
						boolean addAccountRoleRelaResult = systemDao.addAccountRoleRelation(accountToAccountRoleRelation(account));
						if(addAccountRoleRelaResult) {
							return true;
						}
					}
				}
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
				boolean deleteAccountResult = systemDao.deleteAccount(accountId);
				if(deleteAccountResult) {
					boolean deleteAccountRoleRelationResult = systemDao.deleteAccountRoleRelation(accountId);
					if(deleteAccountRoleRelationResult) {
						return true;
					}
				}
			} catch (Exception e) {
				log.error("根据账户ID删除账户信息产生异常e:"+e);
			}
		}
		return false;
	}

	public List<RoleDTO> findRole() {
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
				boolean addRoleResult = systemDao.addRole(role);
				if(addRoleResult) {
					boolean addRoleRoleRelaResult = systemDao.addRolePermissionRelation(roleToRolePermissionRelation(role));
					if(addRoleRoleRelaResult) {
						return true;
					}
				}
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
				boolean updateRoleResult = systemDao.updateRole(role);
				if(updateRoleResult) {
					boolean deleteRoleRoleRelationResult = systemDao.deleteRolePermissionRelation(role.getId());
					if(deleteRoleRoleRelationResult) {
						if(systemDao.addRolePermissionRelation(roleToRolePermissionRelation(role))){
							return true;
						}
					}
				}
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
	private List<RolePermissionRelation> roleToRolePermissionRelation(Role role) {
		log.debug("将role转化为与Permission的关联关系。role:"+role);
		List<RolePermissionRelation> rolePermissionRelationList = new ArrayList<RolePermissionRelation>();
		if(null != role) {
			List<Permission>  permissionList = role.getPermissionList();
			if(null != permissionList) {
				for (Permission permission : permissionList) {
					RolePermissionRelation rolePermissionRelation = new RolePermissionRelation(role, permission);
					rolePermissionRelationList.add(rolePermissionRelation);
				}
			}
		}
		return rolePermissionRelationList;
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

	public AccountIp getAccountIp(int accountIpId) {
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

	public List<AdminLogDTO> findAdminLog() {
		log.debug("查询所有的日志信息");
		try {
			return systemDao.findAdminLog();
		}catch(Exception e) {
			log.error("查询日志信息失败e:"+e);	
		}
		return Collections.EMPTY_LIST;
	}

	public boolean updateSysconfigure(SysConfigure sysConfigure) {
		log.debug("修改系统配置信息。sysConfigure：" +sysConfigure);
		if(null != sysConfigure){
			try {
				return systemDao.updateSysconfigure(sysConfigure);
			}catch(Exception e) {
				log.error("修改配置信息失败e:"+e);
			}
		}
		return false;
	}

	public List<SysConfigure> findSysConfigureList() {
		log.debug("查询所有的系统配置信息。");
		try {
			return systemDao.findSysConfigureList();
		}catch(Exception e) {
			log.error("查询所有的系统配置信息失败e:"+e);
		}
		return Collections.EMPTY_LIST;
	}
	
	public SysConfigure getSysConfigure(String sysConfigureId){
		log.debug("根据ID获取系统参数信息sysConfigureId : " + sysConfigureId);
		if(null!=sysConfigureId) {
			try{
				return systemDao.getSysConfigure(sysConfigureId);
			}catch(Exception e) {
				log.error("根据ID获取系统参数信息产生异常", e);
			}
		}
		return null;
	}
	private String RandomUsage() {
		log.debug("生成随机数");
		StringBuffer sb = new StringBuffer();
        for(int i = 0;i < 6;i++){
            sb.append((int)(Math.random()*10));
        }
        return sb.toString();
	}
	/**
	 * 10001：邮箱不存在。
	 * 10002：邮箱错误
	 * 10003：重置成功
	 */
	public int forgotPassword(String email) {
		log.debug("忘记密码，密码重置。email:" + email);
		int result = 10002;
		if((null != email)&&(MailUtil.checkEmail(email))) {
			try {
				List<Account> accountList = systemDao.findAccountByEmail(email);
				if(null != accountList) {
					if(1 == accountList.size()) {
						Account account = accountList.get(0);
						String newPassword = RandomUsage();
						account.setPassword(MD5Util.getMD5(newPassword));
						boolean updateAccount = systemDao.updateAccount(account);
						if(updateAccount) {
							result = 10003;
						}
						String mailHost = systemDao.getSysConfigure("MAIL_SERVER_HOST").getValue();
						String userName = systemDao.getSysConfigure("MAIL_SERVER_ACCOUNT").getValue();
						String password = systemDao.getSysConfigure("MAIL_SERVER_PASSWORD").getValue();
						MailUtil.sendMail(mailHost, userName, password, userName, email, userName, SystemEnum.MAIL_SUBJECT.getValue(), SystemEnum.MAIL_CONTENT.getValue()+newPassword);
					}else if(0 == accountList.size()){
						result = 10001;  
					}
				}
			}catch(Exception e) {
				log.error("重置账户异常e:" + e);
			}
		}
		return result;
	}
}
