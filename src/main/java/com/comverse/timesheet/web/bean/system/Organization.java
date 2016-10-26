package com.comverse.timesheet.web.bean.system;

import java.util.ArrayList;
import java.util.List;


public class Organization {
	private long id;					//标识
	private String code;				//组织编码
	private String name;				//名称
	private String certificateType;		//证件类型
	private String certificateNumber;	//证件号
	private String linkman;				//联系人
	private String phone;				//电话
	private String fax;					//传真号
	private String email;				//邮件
	private String zip;					//邮编
	private String address;				//地址
	private long type;					//类型
	private String state;				//状态
	private String remark;				//备注	
	private long parent;				//上级组织
	private String password;			//密码
	
	private List<Role> roleList = new ArrayList<Role>();//对应角色   一对多

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCertificateType() {
		return certificateType;
	}

	public void setCertificateType(String certificateType) {
		this.certificateType = certificateType;
	}

	public String getCertificateNumber() {
		return certificateNumber;
	}

	public void setCertificateNumber(String certificateNumber) {
		this.certificateNumber = certificateNumber;
	}

	public String getLinkman() {
		return linkman;
	}

	public void setLinkman(String linkman) {
		this.linkman = linkman;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getFax() {
		return fax;
	}

	public void setFax(String fax) {
		this.fax = fax;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public long getType() {
		return type;
	}

	public void setType(long type) {
		this.type = type;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

	public long getParent() {
		return parent;
	}

	public void setParent(long parent) {
		this.parent = parent;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Role> getRoleList() {
		return roleList;
	}

	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Organization [id=");
		builder.append(id);
		builder.append(", code=");
		builder.append(code);
		builder.append(", name=");
		builder.append(name);
		builder.append(", certificateType=");
		builder.append(certificateType);
		builder.append(", certificateNumber=");
		builder.append(certificateNumber);
		builder.append(", linkman=");
		builder.append(linkman);
		builder.append(", phone=");
		builder.append(phone);
		builder.append(", fax=");
		builder.append(fax);
		builder.append(", email=");
		builder.append(email);
		builder.append(", zip=");
		builder.append(zip);
		builder.append(", address=");
		builder.append(address);
		builder.append(", type=");
		builder.append(type);
		builder.append(", state=");
		builder.append(state);
		builder.append(", remark=");
		builder.append(remark);
		builder.append(", parent=");
		builder.append(parent);
		builder.append(", password=");
		builder.append(password);
		builder.append(", roleList=");
		builder.append(roleList);
		builder.append("]");
		return builder.toString();
	}

	
}
