package com.comverse.timesheet.web.server.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import com.comverse.timesheet.web.server.ISqlExecuter;
import com.comverse.timesheet.web.server.ISyslogSaver;
import com.comverse.timesheet.web.server.message.AdminLogMessage;
import com.comverse.timesheet.web.server.message.AuditLogMessage;
import com.comverse.timesheet.web.server.message.DeviceLogMessage;
import com.comverse.timesheet.web.server.message.LogMessageHeader;


public class SyslogSaver implements ISyslogSaver{
	private static Logger log = Logger.getLogger(SyslogSaver.class);
	private ISqlExecuter sqlExecuter=null;

	public void setSqlExecuter(ISqlExecuter sqlExecuter) {
		this.sqlExecuter = sqlExecuter;
	}

	public void add(LogMessageHeader message) {
		SimpleDateFormat sdf=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		StringBuffer sb=new StringBuffer();
		if(message instanceof AdminLogMessage){
			AdminLogMessage msg=(AdminLogMessage)message;
			sb.append("INSERT INTO AADMIN_LOG(TTIME,LLEVEL,AACCOUNT,IIP,DDESC) VALUES('");
			sb.append(sdf.format(new Date(msg.getTime())));
			sb.append("',");
			sb.append(msg.getLevel());
			sb.append(",'");
			sb.append(msg.getAccount());
			sb.append("','");
			sb.append(msg.getIp());
			sb.append("','");
			sb.append(msg.getDesc());
			sb.append("')");
		}else if(message instanceof AuditLogMessage){
			AuditLogMessage msg=(AuditLogMessage)message;
			sb.append("INSERT INTO AUDIT_LOG(TTIME,LLEVEL,SSESSION_ID,AAPPLICATION_ID,PPROTOCOL_ID,SSIP,SSPORT,DDIP,DDPORT,DDESC) VALUES('");
			sb.append(sdf.format(new Date(msg.getTime())));
			sb.append("',");
			sb.append(msg.getLevel());
			sb.append(",");
			sb.append(msg.getSessionId());
			sb.append(",");
			sb.append(msg.getApplicationId());
			sb.append(",");
			sb.append(msg.getProtocolId());
			sb.append(",'");
			sb.append(msg.getsIp());
			sb.append("',");
			sb.append(msg.getsPort());
			sb.append(",'");			
			sb.append(msg.getdIp());
			sb.append("',");
			sb.append(msg.getdPort());			
			sb.append(",'");
			sb.append(msg.getDesc());
			sb.append("')");			
		}else if(message instanceof DeviceLogMessage){
			DeviceLogMessage msg=(DeviceLogMessage)message;
			sb.append("INSERT INTO DEVICE_LOG(TTIME,LLEVEL,DDEVICE_NAME,IIP,DDESC) VALUES('");
			sb.append(sdf.format(new Date(msg.getTime())));
			sb.append("',");
			sb.append(msg.getLevel());
			sb.append(",'");
			sb.append(msg.getDeviceName());
			sb.append("','");
			sb.append(msg.getIp());
			sb.append("','");
			sb.append(msg.getDesc());
			sb.append("')");			
		}
		String sql=sb.toString();		
		if(sqlExecuter!=null && sql.length()>0){
			sqlExecuter.addSql(0,sql);
		}
		
	}
}