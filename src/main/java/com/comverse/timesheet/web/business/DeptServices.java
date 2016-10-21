package com.comverse.timesheet.web.business;

import java.util.List;

import com.comverse.timesheet.web.bean.Dept;

public interface DeptServices {
	public List findDept();
	public void deleteDept(Long id);
	public List getDeptsForPo();
	public void saveDept(List<Dept> depts);
	public List getDepts();
}
