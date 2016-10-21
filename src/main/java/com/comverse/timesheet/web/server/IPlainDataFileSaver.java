package com.comverse.timesheet.web.server;

import com.comverse.timesheet.web.server.message.PlainDataSegmentMessage;

public interface IPlainDataFileSaver {
	public void add(PlainDataSegmentMessage message);
}
