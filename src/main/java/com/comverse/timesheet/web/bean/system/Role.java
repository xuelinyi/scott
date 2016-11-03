package com.comverse.timesheet.web.bean.system;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;
import javax.persistence.Table;


/**
 * Aaccount entity. @author MyEclipse Persistence Tools
 */
public class Role{


	private long id;		//角色ID
	private String name;	//角色名称
	private String desc;	//角色备注
	private int code;
	private String createTime;
	private List<Permission> permissionList = new ArrayList<Permission>();//对应权限   


	/** default constructor */
	public Role() {
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public List<Permission> getPermissionList() {
		return permissionList;
	}
	public void setPermissionList(List<Permission> permissionList) {
		this.permissionList = permissionList;
	}

	public static Role updateRole(Role oldRole,Role role){
		oldRole.setName(role.getName());
		oldRole.setDesc(role.getDesc());
		return oldRole;
	}
}