package com.comverse.timesheet.web.bean.system;

public class RolePermissionRelation {
	private int id;
	private Role role;
	private Permission permission;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Role getRole() {
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	public Permission getPermission() {
		return permission;
	}
	public void setPermission(Permission permission) {
		this.permission = permission;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RolePermissionRelation [id=");
		builder.append(id);
		builder.append(", role=");
		builder.append(role);
		builder.append(", permission=");
		builder.append(permission);
		builder.append("]");
		return builder.toString();
	}
	public RolePermissionRelation(){
		
	}
	public RolePermissionRelation(Role role,Permission permission){
		this.role = role;
		this.permission = permission;
	}
}
