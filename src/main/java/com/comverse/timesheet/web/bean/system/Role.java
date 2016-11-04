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

import com.comverse.timesheet.web.dto.RoleDTO;


/**
 * Aaccount entity. @author MyEclipse Persistence Tools
 */
public class Role{


	private int id;		//角色ID
	private String name;	//角色名称
	private String desc;	//角色备注
	private int code;
	private String createTime;
	private List<Permission> permissionList = new ArrayList<Permission>();//对应权限   


	/** default constructor */
	public Role() {
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
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

	public static RoleDTO conversionRole(Role role){
		RoleDTO roleDTO = new RoleDTO();
		roleDTO.setId(role.getId());
		roleDTO.setDesc(role.getDesc());
		roleDTO.setName(role.getName());
		roleDTO.setCreateTime(role.getCreateTime());
		List<Permission> permissionList = role.getPermissionList();
		if((null != permissionList)&&(0!=permissionList.size())) {
			roleDTO.setPermissionList(permissionList);
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < permissionList.size(); i++) {
				if(i>0) {
					sb.append(",");
				}
				sb.append(permissionList.get(i).getName());
			}
			roleDTO.setPermissionListName(sb.toString());
		}
		return roleDTO;
	}
}