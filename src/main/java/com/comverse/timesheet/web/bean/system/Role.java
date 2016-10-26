package com.comverse.timesheet.web.bean.system;

import java.util.ArrayList;
import java.util.List;


public class Role {
	private long id;					//标识
	private String name;				//名称
	private long type;					//类型	Int	组织角色 1020,业务角色 1030,安全角色 1040,其他角色 1050
	private String desc;				//备注
	private String createTime;
	
	private List<Menus> menusList = new ArrayList<Menus>(); //角色和菜单关联实体（中间实体）

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

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public List<Menus> getMenusList() {
		return menusList;
	}

	public void setMenusList(List<Menus> menusList) {
		this.menusList = menusList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RoleBean [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", type=");
		builder.append(type);
		builder.append(", desc=");
		builder.append(desc);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", menusList=");
		builder.append(menusList);
		builder.append("]");
		return builder.toString();
	}

	
}
