package com.comverse.timesheet.web.server.message;


import java.text.SimpleDateFormat;
import java.util.Date;

import com.comverse.timesheet.web.server.Constants;
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
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("DeviceLogMessage [time=");
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		builder.append(format.format(new Date(time)));
		builder.append(", level=");
		builder.append(level);
		builder.append(", deviceName=");
		builder.append(deviceName);
		builder.append(", ip=");
		builder.append(ip);
		builder.append(", desc=");
		builder.append(desc);
		builder.append("]");
		return builder.toString();
	}


}
