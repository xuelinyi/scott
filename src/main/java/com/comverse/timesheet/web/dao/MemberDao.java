package com.comverse.timesheet.web.dao;

import com.comverse.timesheet.web.bean.Member;
import com.comverse.timesheet.web.bean.User;

public interface MemberDao {
	
	public boolean add(Member member);
	public void addMongo(User user);
	public User findUserWithMongo(String name);
}
