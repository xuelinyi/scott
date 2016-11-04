package com.comverse.timesheet.web.dto;

import com.comverse.timesheet.web.bean.system.AdminLog;

public class AdminLogDTO extends AdminLog{
	private String levelStr;

	public String getLevelStr() {
		return levelStr;
	}

	public void setLevelStr(String levelStr) {
		this.levelStr = levelStr;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AdminLogDTO [levelStr=");
		builder.append(levelStr);
		builder.append("]");
		return builder.toString();
	}
	
}
