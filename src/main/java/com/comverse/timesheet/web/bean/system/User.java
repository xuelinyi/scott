package com.comverse.timesheet.web.bean.system;

import java.util.ArrayList;
import java.util.List;


public class User {
	private long id;					//标识
	private String loginName;			//登录名（保证唯一）
	private String password;			//密码
	private String name;				//姓名
	private String company;				//公司
	private String phone;				//电话
	private String email;				//邮箱
	private String fax;					//传真号
	private String address;				//通信地址
	private String zip;					//邮编
	private String remark;				//备注
	private String language;
	private List<Role> roleList = new ArrayList<Role>();//对应角色   一对多
	private Organization organization;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getLoginName() {
		return loginName;
	}
	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getCompany() {
		return company;
	}
	public void setCompany(String company) {
		this.company = company;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this.fax = fax;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getZip() {
		return zip;
	}
	public void setZip(String zip) {
		this.zip = zip;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	public String getLanguage() {
		return language;
	}
	public void setLanguage(String language) {
		this.language = language;
	}
	public List<Role> getRoleList() {
		return roleList;
	}
	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}
	public Organization getOrganization() {
		return organization;
	}
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("User [id=");
		builder.append(id);
		builder.append(", loginName=");
		builder.append(loginName);
		builder.append(", password=");
		builder.append(password);
		builder.append(", name=");
		builder.append(name);
		builder.append(", company=");
		builder.append(company);
		builder.append(", phone=");
		builder.append(phone);
		builder.append(", email=");
		builder.append(email);
		builder.append(", fax=");
		builder.append(fax);
		builder.append(", address=");
		builder.append(address);
		builder.append(", zip=");
		builder.append(zip);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", language=");
		builder.append(language);
		builder.append(", roleList=");
		builder.append(roleList);
		builder.append(", organization=");
		builder.append(organization);
		builder.append("]");
		return builder.toString();
	}
	
}
