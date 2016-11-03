package com.comverse.timesheet.web.dto;

import com.comverse.timesheet.web.bean.system.Account;

public class AccountDTO extends Account{
	private String lockStatus;
	private String roleName;
	public String getLockStatus() {
		return lockStatus;
	}

	public void setLockStatus(String lockStatus) {
		this.lockStatus = lockStatus;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AccountDTO [lockStatus=");
		builder.append(lockStatus);
		builder.append(", roleName=");
		builder.append(roleName);
		builder.append("]");
		return builder.toString();
	}
}
