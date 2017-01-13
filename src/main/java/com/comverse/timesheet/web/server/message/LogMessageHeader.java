package com.comverse.timesheet.web.server.message;


public class LogMessageHeader {
	private int identifier;
	private long id;
	private int type;
	
	public LogMessageHeader() {
		super();
	}
	public LogMessageHeader(LogMessageHeader logMessage) {
		super();
		setIdentifier(logMessage.getIdentifier());
		setId(logMessage.getId());
		setType(logMessage.getType());
	}
	
	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	
}

