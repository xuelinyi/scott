package com.comverse.timesheet.web.server.dto;

import com.comverse.timesheet.web.server.message.SessionSegmentMessage;

public class CacheKey {
	private long sessionId;
	private long applicationId;
	private int protocolId;	
	private int type;

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

	public CacheKey(long sessionId, long applicationId, int protocolId, int type) {
		super();
		this.sessionId = sessionId;
		this.applicationId = applicationId;
		this.protocolId = protocolId;
		this.type = type;
	}
	public CacheKey(SessionSegmentMessage message) {
		super();
		this.sessionId = message.getSessionId();
		this.applicationId = message.getApplicationId();
		this.protocolId = message.getProtocolId();
		this.type = message.getType();
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (applicationId ^ (applicationId >>> 32));
		result = prime * result + protocolId;
		result = prime * result + (int) (sessionId ^ (sessionId >>> 32));
		result = prime * result + type;
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		CacheKey other = (CacheKey) obj;
		if (applicationId != other.applicationId)
			return false;
		if (protocolId != other.protocolId)
			return false;
		if (sessionId != other.sessionId)
			return false;
		if (type != other.type)
			return false;
		return true;
	}
}
