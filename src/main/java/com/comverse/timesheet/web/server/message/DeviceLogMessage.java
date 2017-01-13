package com.comverse.timesheet.web.server.message;

import com.comverse.timesheet.web.server.impl.Constants;
import com.comverse.timesheet.web.server.util.BinaryHelper;

public class DeviceLogMessage extends LogMessageHeader{
	private long time;
	private int level;
	private String deviceName="";
	private String ip="";
	private String desc="";
	public DeviceLogMessage() {
		super();
		setType(Constants.SYSLOG_TYPE_DEVICE);
	}
	public DeviceLogMessage(LogMessageHeader logMessage) {
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
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) {
		this.deviceName = deviceName;
	}
	public String getIp() {
		return ip;
	}
	public byte[] getIpBinary() {
		return BinaryHelper.ipv4ToBinary(ip);
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public String toString() {
		return "DeviceLogMessage [getId()=" + getId() + ", getType()=" + getType() + ", time=" + time + ", level="
				+ level + ", deviceName=" + deviceName + ", ip=" + ip + ", desc=" + desc + "]";
	}

}