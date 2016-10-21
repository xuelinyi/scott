package com.comverse.timesheet.web.server;

import com.comverse.timesheet.web.server.message.SessionSegmentMessage;

public interface ISessionCache {
	public void addSessionSegmentMessage(SessionSegmentMessage message);
	public void timeout();
}
