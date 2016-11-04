package com.comverse.timesheet.web.business;

import java.util.List;

import com.comverse.timesheet.web.bean.system.Account;
import com.comverse.timesheet.web.bean.system.AccountIp;
import com.comverse.timesheet.web.bean.system.AdminLog;
import com.comverse.timesheet.web.bean.system.Permission;
import com.comverse.timesheet.web.bean.system.Role;
import com.comverse.timesheet.web.bean.system.SysConfigure;
import com.comverse.timesheet.web.dto.AccountDTO;
import com.comverse.timesheet.web.dto.AdminLogDTO;
import com.comverse.timesheet.web.dto.RoleDTO;

public interface ISystemBusiness {
	List<AccountDTO> findAccount();
	Account getAccount(int accountId);
	boolean addAccount(Account account);
	boolean updateAccount(Account account);
	boolean deleteAccount(int accountId);
	
	List<RoleDTO> findRole();
	Role getRole(int roleId);
	boolean addRole(Role role);
	boolean updateRole(Role role);
	boolean deleteRole(int roleId);
	
	List<AccountIp> findAccountIp();
	AccountIp getAccountIp(int accountIpId);
	boolean addAccountIp(AccountIp accountIp);
	boolean updateAccountIp(AccountIp accountIp);
	boolean deleteAccountIp(int accountIpId);
	
	List<Permission> findPermission();
	
	void addAdminLog(AdminLog adminlog);
	List<AdminLogDTO> findAdminLog();
	
	boolean updateSysconfigure(SysConfigure sysConfigure);
	List<SysConfigure> findSysConfigureList();
}
