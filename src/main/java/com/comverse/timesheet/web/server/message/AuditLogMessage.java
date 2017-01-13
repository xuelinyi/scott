package com.comverse.timesheet.web.server.message;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.comverse.timesheet.web.server.impl.Constants;
import com.comverse.timesheet.web.server.util.BinaryHelper;

public class AuditLogMessage extends LogMessageHeader{
	private long time;
	private int level;
	private long sessionId;
	private long applicationId;
	private int protocolId;	
	private String sIp="";
	private int sPort;
	private String dIp="";
	private int dPort;
	
	private String desc="";
	public AuditLogMessage() {
		super();
		setType(Constants.SYSLOG_TYPE_AUDIT);
	}	
	public AuditLogMessage(LogMessageHeader logMessage) {
		super(logMessage);
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	public int getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(int protocolId) {
		this.protocolId = protocolId;
	}
	public String getsIp() {
		return sIp;
	}
	public byte[] getSIpBinary() {
		return BinaryHelper.ipv4ToBinary(sIp);
	}
	public void setsIp(String sIp) {
		this.sIp = sIp;
	}
	public String getdIp() {
		return dIp;
	}
	public byte[] getDIpBinary() {
		return BinaryHelper.ipv4ToBinary(dIp);
	}
	
	public void setdIp(String dIp) {
		this.dIp = dIp;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	
	public int getsPort() {
		return sPort;
	}
	public void setsPort(int sPort) {
		this.sPort = sPort;
	}
	public int getdPort() {
		return dPort;
	}
	public void setdPort(int dPort) {
		this.dPort = dPort;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuditLogMessage [time=");
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		builder.append(format.format(new Date(time)));
		builder.append(", level=");
		builder.append(level);
		builder.append(", sessionId=");
		builder.append(sessionId);
		builder.append(", applicationId=");
		builder.append(applicationId);
		builder.append(", protocolId=");
		builder.append(protocolId);
		builder.append(", sIp=");
		builder.append(sIp);
		builder.append(", sPort=");
		builder.append(sPort);
		builder.append(", dIp=");
		builder.append(dIp);
		builder.append(", dPort=");
		builder.append(dPort);
		builder.append(", desc=");
		builder.append(desc);
		builder.append("]");
		return builder.toString();
	}


	
}
