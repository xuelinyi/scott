package com.comverse.timesheet.web.dao;

import java.util.List;

import com.comverse.timesheet.web.bean.system.Account;
import com.comverse.timesheet.web.bean.system.AccountIp;
import com.comverse.timesheet.web.bean.system.AccountRoleRelation;
import com.comverse.timesheet.web.bean.system.AdminLog;
import com.comverse.timesheet.web.bean.system.Permission;
import com.comverse.timesheet.web.bean.system.Role;
import com.comverse.timesheet.web.bean.system.RolePermissionRelation;
import com.comverse.timesheet.web.bean.system.SysConfigure;
import com.comverse.timesheet.web.dto.AccountDTO;
import com.comverse.timesheet.web.dto.AdminLogDTO;
import com.comverse.timesheet.web.dto.RoleDTO;

public interface ISystemDao {
	List<AccountDTO> findAccount() throws Exception;
	Account getAccount(int accountId) throws Exception;
	List<Account> findAccountByEmail(String email) throws Exception;
	boolean addAccount(Account account) throws Exception;
	boolean updateAccount(Account account) throws Exception;
	boolean deleteAccount(int accountId) throws Exception;
	boolean addAccountRoleRelation(List<AccountRoleRelation> accountRoleRelationList) throws Exception; 
	boolean deleteAccountRoleRelation(int accountId) throws Exception;
	
	List<RoleDTO> findRole() throws Exception;
	Role getRole(int roleId) throws Exception;
	boolean addRole(Role role) throws Exception;
	boolean updateRole(Role role) throws Exception;
	boolean deleteRole(int roleId) throws Exception;
	boolean addRolePermissionRelation(List<RolePermissionRelation> rolePermissionRelationList) throws Exception; 
	boolean deleteRolePermissionRelation(int roleId) throws Exception;
	
	List<AccountIp> findAccountIp() throws Exception;
	AccountIp getAccountIp(int accountIpId) throws Exception;
	boolean addAccountIp(AccountIp accountIp) throws Exception;
	boolean updateAccountIp(AccountIp accountIp) throws Exception;
	boolean deleteAccountIp(int accountIpId) throws Exception;
	
	List<Permission> findPermission() throws Exception;
	
	
	void addAdminLog(AdminLog adminLog) throws Exception;
	List<AdminLogDTO> findAdminLog() throws Exception;
	
	boolean updateSysconfigure(SysConfigure sysConfigure) throws Exception;
	SysConfigure getSysConfigure(String sysConfigureId)throws Exception;
	List<SysConfigure> findSysConfigureList() throws Exception;
	
}
