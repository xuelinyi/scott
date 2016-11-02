package com.comverse.timesheet.web.dao;

import java.util.List;

import com.comverse.timesheet.web.bean.system.Account;
import com.comverse.timesheet.web.bean.system.AccountIp;
import com.comverse.timesheet.web.bean.system.AdminLog;
import com.comverse.timesheet.web.bean.system.Permission;
import com.comverse.timesheet.web.bean.system.Role;

public interface ISystemDao {
	List<Account> findAccount() throws Exception;
	Account getAccount(int accountId) throws Exception;
	boolean addAccount(Account account) throws Exception;
	boolean updateAccount(Account account) throws Exception;
	boolean deleteAccount(int accountId) throws Exception;
	
	List<Role> findRole() throws Exception;
	Role getRole(int roleId) throws Exception;
	boolean addRole(Role role) throws Exception;
	boolean updateRole(Role role) throws Exception;
	boolean deleteRole(int roleId) throws Exception;
	
	List<AccountIp> findAccountIp() throws Exception;
	Role getAccountIp(int accountIpId) throws Exception;
	boolean addAccountIp(AccountIp accountIp) throws Exception;
	boolean updateAccountIp(AccountIp accountIp) throws Exception;
	boolean deleteAccountIp(int accountIpId) throws Exception;
	
	List<Permission> findPermission() throws Exception;
	public void addAdminLog(AdminLog adminLog) throws Exception;
}
