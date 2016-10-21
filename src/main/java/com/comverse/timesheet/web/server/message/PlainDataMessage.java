package com.comverse.timesheet.web.server.message;

import org.apache.commons.codec.binary.Hex;

public class PlainDataMessage{
	private long sessionId;
	private int dataLen;//<=0 means end of the session
	private int protocolId;
	private long time;
	private String srcMac="";
	private String dstMac="";
	private String srcIp="";
	private int srcPort;
	private String dstIp="";
	private int dstPort;
	private byte[]data=new byte[0];
	

	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public int getDataLen() {
		return dataLen;
	}
	public void setDataLen(int dataLen) {
		this.dataLen = dataLen;
	}
	public int getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(int protocolId) {
		this.protocolId = protocolId;
	}
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public String getSrcMac() {
		return srcMac;
	}
	public void setSrcMac(String srcMac) {
		this.srcMac = srcMac;
	}
	public String getDstMac() {
		return dstMac;
	}
	public void setDstMac(String dstMac) {
		this.dstMac = dstMac;
	}
	public String getSrcIp() {
		return srcIp;
	}
	public void setSrcIp(String srcIp) {
		this.srcIp = srcIp;
	}
	public int getSrcPort() {
		return srcPort;
	}
	public void setSrcPort(int srcPort) {
		this.srcPort = srcPort;
	}
	public String getDstIp() {
		return dstIp;
	}
	public void setDstIp(String dstIp) {
		this.dstIp = dstIp;
	}
	public int getDstPort() {
		return dstPort;
	}
	public void setDstPort(int dstPort) {
		this.dstPort = dstPort;
	}
	public byte[] getData() {
		return data;
	}
	public void setData(byte[] data) {
		this.data = data;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlainDataMessage [sessionId=");
		builder.append(sessionId);
		builder.append(", dataLen=");
		builder.append(dataLen);
		builder.append(", protocolId=");
		builder.append(protocolId);
		builder.append(", time=");
		builder.append(time);
		builder.append(", srcMac=");
		builder.append(srcMac);
		builder.append(", dstMac=");
		builder.append(dstMac);
		builder.append(", srcIp=");
		builder.append(srcIp);
		builder.append(", srcPort=");
		builder.append(srcPort);
		builder.append(", dstIp=");
		builder.append(dstIp);
		builder.append(", dstPort=");
		builder.append(dstPort);
		builder.append(", data=");
		builder.append(Hex.encodeHexString(data));
		builder.append("]");
		return builder.toString();
	}



}

