package com.comverse.timesheet.web.server.message;

import java.util.ArrayList;
import java.util.List;

public class SessionSegmentMessage {
	private int identifier;
	private long sessionId;
	private long applicationId;
	private int protocolId;	
	private int type;
	private long recvTime;
	private long collectorId;
	private String collectorIp;
	private List<TLVMessage>tlvList=new ArrayList<TLVMessage>();
	
	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	public SessionSegmentMessage() {
		super();
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
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public List<TLVMessage> getTlvList() {
		return tlvList;
	}
	public void setTlvList(List<TLVMessage> tlvList) {
		this.tlvList = tlvList;
	}


	public long getRecvTime() {
		return recvTime;
	}
	public void setRecvTime(long recvTime) {
		this.recvTime = recvTime;
	}
	
	public long getCollectorId() {
		return collectorId;
	}
	public void setCollectorId(long collectorId) {
		this.collectorId = collectorId;
	}
	public String getCollectorIp() {
		return collectorIp;
	}
	public void setCollectorIp(String collectorIp) {
		this.collectorIp = collectorIp;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SessionSegmentMessage [sessionId=");
		builder.append(sessionId);
		builder.append(", applicationId=");
		builder.append(applicationId);
		builder.append(", protocolId=");
		builder.append(protocolId);
		builder.append(", type=");
		builder.append(type);
		builder.append(", recvTime=");
		builder.append(recvTime);
		builder.append(", collectorId=");
		builder.append(collectorId);
		builder.append(", collectorIp=");
		builder.append(collectorIp);
		builder.append(", tlvList=");
		builder.append(tlvList);
		builder.append("]");
		return builder.toString();
	}

	
}
