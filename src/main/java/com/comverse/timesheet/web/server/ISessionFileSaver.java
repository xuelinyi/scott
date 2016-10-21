package com.comverse.timesheet.web.server;

import com.comverse.timesheet.web.server.message.SessionSegmentMessage;

public interface ISessionFileSaver {
	public void add(SessionSegmentMessage message);
}
