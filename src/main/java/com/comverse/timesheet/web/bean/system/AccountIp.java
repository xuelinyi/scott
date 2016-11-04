package com.comverse.timesheet.web.bean.system;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;


/**
 * Aaccount entity. @author xuelin yi
 */
public class AccountIp{
	private long id;				//用户ID
	private String ip;				//IP地址
	private String netmask;			//掩码
	private String accountName;		//用户名 
	private String createTime;

	public AccountIp() {
	}


	public long getId() {
		return id;
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getIp() {
		return ip;
	}


	public void setIp(String ip) {
		this.ip = ip;
	}


	public String getNetmask() {
		return netmask;
	}


	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}


	public String getAccountName() {
		return accountName;
	}


	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getCreateTime() {
		return createTime;
	}


	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}


	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AccountIp [id=");
		builder.append(id);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", netmask=");
		builder.append(netmask);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", accountName=");
		builder.append(accountName);
		builder.append("]");
		return builder.toString();
	}

	
}