package com.comverse.timesheet.web.server;

import com.comverse.timesheet.web.server.dto.CacheSession;

public interface ISessionSaver {
	public void add(CacheSession cacheSession,int sessionKilledFlag);
	public void update(CacheSession cacheSession,int sessionKilledFlag);
}
