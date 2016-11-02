package com.comverse.timesheet.web.bean.system;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.JoinColumn;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Aaccount entity. @author xuelin yi
 */
public class Account{

	private long id;				//用户ID
	private String name;			//用户名
	private String password;		//密码
	private String lockEndTime;		//锁定时间
	private long loginNumber;		//登录次数
	private String phoneNumber;		//电话号码
	private String email;			//邮箱
	private String desc;			//备注
	private String createTime;		//创建时间
	private List<Role> roleList = new ArrayList<Role>();//对应权限   一对多


	public Account() {
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


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getLockEndTime() {
		return lockEndTime;
	}


	public void setLockEndTime(String lockEndTime) {
		this.lockEndTime = lockEndTime;
	}


	public long getLoginNumber() {
		return loginNumber;
	}


	public void setLoginNumber(long loginNumber) {
		this.loginNumber = loginNumber;
	}


	public String getPhoneNumber() {
		return phoneNumber;
	}


	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
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


	public List<Role> getRoleList() {
		return roleList;
	}


	public void setRoleList(List<Role> roleList) {
		this.roleList = roleList;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Account [id=");
		builder.append(id);
		builder.append(", name=");
		builder.append(name);
		builder.append(", password=");
		builder.append(password);
		builder.append(", lockEndTime=");
		builder.append(lockEndTime);
		builder.append(", loginNumber=");
		builder.append(loginNumber);
		builder.append(", phoneNumber=");
		builder.append(phoneNumber);
		builder.append(", email=");
		builder.append(email);
		builder.append(", desc=");
		builder.append(desc);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", roleList=");
		builder.append(roleList);
		builder.append("]");
		return builder.toString();
	}


	public static Account updateAccount(Account oldAccount,Account account){
		oldAccount.setName(account.getName());
		oldAccount.setPassword(account.getPassword());
		oldAccount.setLockEndTime(account.getLockEndTime());
		oldAccount.setLoginNumber(account.getLoginNumber());
		oldAccount.setPhoneNumber(account.getPhoneNumber());
		oldAccount.setEmail(account.getEmail());
		oldAccount.setDesc(account.getDesc());
		return oldAccount;
	}
	
}