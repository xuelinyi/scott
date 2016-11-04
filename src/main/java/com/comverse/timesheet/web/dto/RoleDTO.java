package com.comverse.timesheet.web.dto;

import com.comverse.timesheet.web.bean.system.Role;

public class RoleDTO extends Role{
	private String permissionListName;

	public String getPermissionListName() {
		return permissionListName;
	}

	public void setPermissionListName(String permissionListName) {
		this.permissionListName = permissionListName;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RoleDTO [permissionListName=");
		builder.append(permissionListName);
		builder.append("]");
		return builder.toString();
	}
	
}
