package com.comverse.timesheet.web.bean.system;

public class AdminLog {
	
	private int id;		//标识
	private String time;	//发生时间
	private int level;		//级别
	private String account;	//用户
	private String ip;		//ip地址
	private String descrition;	//描述
	public long getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public long getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDescrition() {
		return descrition;
	}
	public void setDescrition(String descrition) {
		this.descrition = descrition;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdminLog [id=");
		builder.append(id);
		builder.append(", time=");
		builder.append(time);
		builder.append(", level=");
		builder.append(level);
		builder.append(", account=");
		builder.append(account);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", descrition=");
		builder.append(descrition);
		builder.append("]");
		return builder.toString();
	}
	public AdminLog() {
		
	}
	public AdminLog(String account,String ip,String descrition,int level) {
		this.account = account;
		this.ip = ip;
		this.descrition = descrition;
		this.level = level;
	}
}
