package com.comverse.timesheet.web.dao;

import com.comverse.timesheet.web.bean.leave.Leave;

public interface ILeaveDAO {
	int saveLeave(Leave entity);
	boolean updateLeaveProcessInstanceId(Leave leave);
}
