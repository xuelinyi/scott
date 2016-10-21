package com.comverse.timesheet.web.server;

import com.comverse.timesheet.web.server.message.LogMessageHeader;

public interface ISyslogSaver {
	public void add(LogMessageHeader message);
}
