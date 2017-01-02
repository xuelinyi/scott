package com.comverse.timesheet.web.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Repository;

import com.comverse.timesheet.web.bean.leave.Leave;
import com.comverse.timesheet.web.dao.ILeaveDAO;
import com.comverse.timesheet.web.util.BasicSqlSupport;
@Repository
public class LeaveDaoImpl extends BasicSqlSupport implements ILeaveDAO{
	private static final Logger log = Logger.getLogger(LeaveDaoImpl.class);
	public int saveLeave(Leave entity) {
		log.debug("保存请假流程。entity ： " + entity);
		if(null != entity) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				entity.setApplyTime(formatter.format(new Date()));
				entity.setRealityStartTime(formatter.format(new Date()));
				entity.setRealityEndTime(formatter.format(new Date()));
				int id =  session.insert("mybatis.mapper.OaLeave.addOaLeave",entity);
				return id;
			}catch(Exception e) {
				log.error("保存请假流程失败。e:" + e);
			}
		}
		return 0;
	}
	public boolean updateLeaveProcessInstanceId(Leave leave) {
		log.debug("修改请假流程的leave："+leave);
		boolean result = false;
		if((null != leave)&&(0!=leave.getId())) {
			try {
				session.update("mybatis.mapper.OaLeave.updateOaLeaveProcessInstanceId",leave);
				result = true;
			}catch(Exception e) {
				log.error("修改processInstanceId失败"+e);
			}
		}
		return result;
	}
	public Leave getLeave(int leaveId) {
		log.debug("根据请假ID查询其请假信息。leaveId："+leaveId);
		if(0!=leaveId) {
			try {
				return session.selectOne("mybatis.mapper.OaLeave.selectOaLeaveById",leaveId);
			}catch(Exception e) {
				log.error("根据ID查询其请假信息失败。"+e);
			}
		}
		return null;
	}
	public boolean updateLeave(Leave leave) {
		log.debug("修改请假流程的leave："+leave);
		boolean result = false;
		if((null != leave)&&(0!=leave.getId())) {
			try {
				session.update("mybatis.mapper.OaLeave.updateOaLeave",leave);
				result = true;
			}catch(Exception e) {
				log.error("修改processInstanceId失败"+e);
			}
		}
		return result;
	}
}
