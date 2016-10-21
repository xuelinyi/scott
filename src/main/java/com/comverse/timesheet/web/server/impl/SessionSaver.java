package com.comverse.timesheet.web.server.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.ISessionSaver;
import com.comverse.timesheet.web.server.ISqlExecuter;
import com.comverse.timesheet.web.server.dto.CacheSession;
import com.comverse.timesheet.web.server.dto.TLVKey;
import com.comverse.timesheet.web.server.util.BinaryHelper;
import com.comverse.timesheet.web.server.util.FilenameHelper;

public class SessionSaver implements ISessionSaver {
	private static Logger log = Logger.getLogger(SessionSaver.class);

	
	public void add(CacheSession cacheSession,int sessionKilledFlag){
		log.debug("cacheSession = " + cacheSession);
		switch (cacheSession.getType()) {
		case Constants.SESSION_LOG_TYPE_SESSION:
			addSession(cacheSession,sessionKilledFlag);
			break;
		case Constants.SESSION_LOG_TYPE_OPERATION:
			addOperation(cacheSession,sessionKilledFlag);
			break;
		case Constants.SESSION_LOG_TYPE_FILE:
			addFile(cacheSession,sessionKilledFlag);
			break;
		default:
			log.error("error message type : " + cacheSession);
			break;
		}
	}
	public void update(CacheSession cacheSession,int sessionKilledFlag){
		log.debug("update cacheSession = " + cacheSession);
		switch (cacheSession.getType()) {
		case Constants.SESSION_LOG_TYPE_SESSION:
			updateSession(cacheSession,sessionKilledFlag);
			break;
		case Constants.SESSION_LOG_TYPE_OPERATION:
			updateOperation(cacheSession,sessionKilledFlag);
			break;
		case Constants.SESSION_LOG_TYPE_FILE:
			updateFile(cacheSession,sessionKilledFlag);
			break;
		default:
			log.error("update error message type : " + cacheSession);
			break;
		}
	}
	/**
	 * 增加截断功能，防止个别字段多长，插入数据库失败
	 * @param cacheSession
	 * @param tag
	 * @param limit
	 * @return
	 */
	private String getStringValue(CacheSession cacheSession,int tag,int limit){
		TLVKey key=new TLVKey(tag);
		IoBuffer buf=cacheSession.getTlvSegments().get(key);
		if(buf==null){
			return "";
		}
		buf.flip();
		if(buf.limit()==0){
			return "";
		}
		byte[] b = null;
		if(buf.limit()>limit){
			b=new byte[limit];	
		}else{
			b=new byte[buf.limit()];
		}
		buf.get(b);
		String str=new String(b);
		return StringEscapeUtils.escapeSql(str);
	}
	
	private String getBytesValue(CacheSession cacheSession,int tag,int limit){
		TLVKey key=new TLVKey(tag);
		IoBuffer buf=cacheSession.getTlvSegments().get(key);
		if(buf==null){
			return "";
		}
		buf.flip();
		if(buf.limit()==0){
			return "";
		}
		byte[] b = null;
		if(buf.limit()>limit/2){
			b=new byte[limit/2];	
		}else{
			b=new byte[buf.limit()];
		}		
		buf.get(b);
		return Hex.encodeHexString(b).toUpperCase();
		
	}
	private int getIntValue(CacheSession cacheSession,int tag){
		TLVKey key=new TLVKey(tag);
		IoBuffer buf=cacheSession.getTlvSegments().get(key);
		if(buf==null){
			return 0;
		}
		buf.flip();
		if(buf.limit()<4){
			return 0;
		}
		return buf.getInt();
	}	
	private long getLongValue(CacheSession cacheSession,int tag){
		TLVKey key=new TLVKey(tag);
		IoBuffer buf=cacheSession.getTlvSegments().get(key);
		if(buf==null){
			return 0;
		}
		buf.flip();
		if(buf.limit()<8){
			return 0;
		}
		return buf.getLong();
	}
	private String getTimeValue(CacheSession cacheSession,int tag){
		TLVKey key=new TLVKey(tag);
		IoBuffer buf=cacheSession.getTlvSegments().get(key);
		if(buf==null){
			return "";
		}
		buf.flip();
		if(buf.limit()<8){
			return "";
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		return format.format(new Date(buf.getLong()));
	}
	private String getIpValue(CacheSession cacheSession,int tag){
		TLVKey key=new TLVKey(tag);
		IoBuffer buf=cacheSession.getTlvSegments().get(key);
		if(buf==null){
			return "";
		}
		buf.flip();
		if(buf.limit()<4){
			return "";
		}
		byte[] b = new byte[4];
		buf.get(b);
		return BinaryHelper.bytesToIpString(b);
	}
	private String getMacValue(CacheSession cacheSession,int tag){
		TLVKey key=new TLVKey(tag);
		IoBuffer buf=cacheSession.getTlvSegments().get(key);
		if(buf==null){
			return "";
		}
		buf.flip();
		if(buf.limit()==0){
			return "";
		}
		byte[] b = new byte[buf.limit()];
		buf.get(b);
		return BinaryHelper.bytesToMac(b);
	}

	private void addSession(CacheSession cacheSession,int sessionKilledFlag){
		StringBuffer sb=new StringBuffer();
		sb.append("INSERT INTO SESSION_LOG(CCOLLECTOR_ID,CCOLLECTOR_IP,");
		sb.append("SSESSION_ID,AAPPLICATION_ID,PPROTOCOL_ID,FFLOW_ID,SSIP,SSPORT,");
		sb.append("DDIP,DDPORT,PPRIMARY_ACCOUNT,PPROTOCOL_VERSION,SSERVER_VERSION,");
		sb.append("CCLIENT_VERSION,LLOGIN_TIME,LLOGOUT_TIME,UUSERNAME,SSUB_PROTOCOL,");
		sb.append("SSUB_PROTOCOL_VERSION,RREQ_FLOW,RRESP_FLOW,SSMAC,DDMAC,LLOCAL_FILE,CCREATE_TIME) VALUES(");
		sb.append(cacheSession.getCollectorId());
		sb.append(",'");
		sb.append(cacheSession.getCollectorIp());
		sb.append("',");
		sb.append(cacheSession.getSessionId());
		sb.append(",");
		sb.append(cacheSession.getApplicationId());
		sb.append(",");
		sb.append(cacheSession.getProtocolId());
		sb.append(",'");
		sb.append(getBytesValue(cacheSession,0x01,Constants.FLOW_ID_MAX));//FFLOW_ID
		sb.append("','");
		sb.append(getIpValue(cacheSession,0x02));//SSIP
		sb.append("',");
		sb.append(getIntValue(cacheSession,0x03));//SSPORT
		sb.append(",'");
		sb.append(getIpValue(cacheSession,0x04));//DDIP
		sb.append("',");
		sb.append(getIntValue(cacheSession,0x05));//DDPORT
		sb.append(",'");
		sb.append(getStringValue(cacheSession,0x06,Constants.PRIMARY_ACCOUNT_MAX));//PPRIMARY_ACCOUNT
		sb.append("','");
		sb.append(getStringValue(cacheSession,0x07,Constants.PROTOCOL_VERSION_MAX));//PPROTOCOL_VERSION
		sb.append("','");
		sb.append(getStringValue(cacheSession,0x08,Constants.SERVER_VERSION_MAX));//SSERVER_VERSION
		sb.append("','");
		sb.append(getStringValue(cacheSession,0x09,Constants.CLIENT_VERSION_MAX));//CCLIENT_VERSION
		sb.append("','");
		sb.append(getTimeValue(cacheSession,0x0A));//LLOGIN_TIME
		sb.append("','");
		sb.append(getTimeValue(cacheSession,0x0C));//LLOGOUT_TIME
		sb.append("','");
		sb.append(getStringValue(cacheSession,0x0B,Constants.USERNAME_MAX));//UUSERNAME
		sb.append("',");
		sb.append(getIntValue(cacheSession,0x0D));//SSUB_PROTOCOL
		sb.append(",'");
		sb.append(getStringValue(cacheSession,0x0E,Constants.SUB_PROTOCOL_VERSION_MAX));//SSUB_PROTOCOL_VERSION
		sb.append("',");
		sb.append(getLongValue(cacheSession,0x0F));//RREQ_FLOW
		sb.append(",");
		sb.append(getLongValue(cacheSession,0x10));//RRESP_FLOW
		sb.append(",'");
		sb.append(getMacValue(cacheSession,0x11));//SSMAC
		sb.append("','");
		sb.append(getMacValue(cacheSession,0x12));//DDMAC
		sb.append("','");
		sb.append(FilenameHelper.createSessionFilename(cacheSession.getSessionId(),cacheSession.getCreateTime()));//LLOCAL_FILE
		sb.append("','");
		sb.append(cacheSession.getCreateTime());//CCREATE_TIME
		sb.append("'");
		if(sessionKilledFlag==Constants.SESSION_LOG_SESSION_KILLED_NORMAL){	
			sb.append(",SSTOP_TIME=");
			sb.append(System.currentTimeMillis());//SSTOP_TIME
			sb.append(",SSTOP_REASON="+Constants.SESSION_LOG_SESSION_KILLED_NORMAL);//SSTOP_REASON
		}else if(sessionKilledFlag==Constants.SESSION_LOG_SESSION_KILLED_TIMEOUT){	
			sb.append(",SSTOP_TIME=");
			sb.append(System.currentTimeMillis());//SSTOP_TIME
			sb.append(",SSTOP_REASON="+Constants.SESSION_LOG_SESSION_KILLED_TIMEOUT);//SSTOP_REASON			
		}
		sb.append(")");
		String sql=sb.toString();		
		log.debug("sql = "+sql);
		if(sqlExecuter!=null && sql.length()>0){
			sqlExecuter.addSql(sql);
		}
		
		if(cacheSession.getProtocolId() == 0x06||cacheSession.getProtocolId() == 0x07||cacheSession.getProtocolId() == 0x08){
			//SSH、SCP和SFTP
			StringBuffer sshAndSo = new StringBuffer();
			sshAndSo.append("INSERT INTO SSH_SESSION_LOG(");
			sshAndSo.append("SSESSION_ID,AAPPLICATION_ID,UUP_CHANNEL,DDOWN_CHANNEL) VALUES(");
			sshAndSo.append(cacheSession.getSessionId());
			sshAndSo.append(",");
			sshAndSo.append(cacheSession.getApplicationId());
			sshAndSo.append(",");
			sshAndSo.append(getIntValue(cacheSession,0x0101));//上行Channel
			sshAndSo.append(",");
			sshAndSo.append(getIntValue(cacheSession,0x0102));//下行Channel
			sshAndSo.append(")");
			String sshsql=sshAndSo.toString();		
			log.debug("httpsql = "+sshsql);
			if(sqlExecuter!=null && sshsql.length()>0){
				sqlExecuter.addSql(sshsql);
			}
		}else if(cacheSession.getProtocolId() == 0x11||cacheSession.getProtocolId() == 0x12){
			//HTTP(S)
			StringBuffer httpAndSo = new StringBuffer();
			httpAndSo.append("INSERT INTO HTTP_SESSION_LOG(");
			httpAndSo.append("SSESSION_ID,AAPPLICATION_ID,MMETHOD,UURL,HHOST,RREQ_COOKIE,");
			httpAndSo.append("RREQ_CONTENT_TYPE,RREQ_CONTENT_ENCODING,RREQ_BODY,AACCEPT,XX_FORWARDED_FOR,RREQ_CONTENT_LENGTH,SSTATUS_CODE,RREFERER,");
			httpAndSo.append("SSET_COOKIE,CCONTENT_TYPE,CCONTENT_ENCODING,CCONTENT,DDATE,SSTATUS_MESSAGE,LLOCATION,TTRANSFER_ENCODING,CCONTENT_LENGTH) VALUES(");
			httpAndSo.append(cacheSession.getSessionId());
			httpAndSo.append(",");
			httpAndSo.append(cacheSession.getApplicationId());
			httpAndSo.append(",'");
			httpAndSo.append(getStringValue(cacheSession,0x0101,Constants.HTTP_METHOD_MAX));//客户端方法
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x0102,Constants.HTTP_URL_MAX));//URL
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x0103,Constants.HTTP_HOST_MAX));//HOST
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x0104,Constants.HTTP_REQ_COOKIE_MAX));//Cookie
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x0105,Constants.HTTP_REQ_CONTENT_TYPE_MAX));//Content-Type
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x0106,Constants.HTTP_REQ_CONTENT_ENCODING_MAX));//Content-Encoding
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x0107,Constants.HTTP_REQ_BODY_MAX));//请求消息内容
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x0108,Constants.HTTP_ACCEPT));//accept
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x0109,Constants.HTTP_X_FORWADED_FOR));//X-Forwarded-For
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x010A,Constants.HTTP_REQ_CONTENT_LENGTH));//请求Content-Length
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x0181,Constants.HTTP_STATUS_CODE_MAX));//响应号码
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x0182,Constants.HTTP_RREFERER_MAX));//Referer
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x0183,Constants.HTTP_SET_COOKIE_MAX));//Set-Cookie
			httpAndSo.append("','");
			String contentType=getStringValue(cacheSession,0x0184,Constants.HTTP_CONTENT_TYPE_MAX);
			httpAndSo.append(contentType);//Content-Type
			httpAndSo.append("','");
			String contentEncoding=getStringValue(cacheSession,0x0185,Constants.HTTP_CONTENT_ENCODING_MAX);
			httpAndSo.append(contentEncoding);//Content-Encoding
			httpAndSo.append("','");
			if(contentType.startsWith("text") && (contentEncoding.length()==0)){
				httpAndSo.append(getStringValue(cacheSession,0x0186,Constants.HTTP_CONTENT));//响应消息内容	
			}else{
				httpAndSo.append(getBytesValue(cacheSession,0x0186,Constants.HTTP_CONTENT));//响应消息内容
			}
			
			httpAndSo.append("','");
			httpAndSo.append(getTimeValue(cacheSession,0x0187));//Date
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x0188,Constants.HTTP_STATUS_MESSAGE));//Status Message
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x0189,Constants.HTTP_LOCATION));//Location
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x018A,Constants.HTTP_TRANSFER_ENCODING));//Transfer-Encoding
			httpAndSo.append("','");
			httpAndSo.append(getStringValue(cacheSession,0x018B,Constants.HTTP_CONTENT_LENGTH));//响应Content-Length
			httpAndSo.append("')");
			String httpsql=httpAndSo.toString();		
			log.debug("httpsql = "+httpsql);
			if(sqlExecuter!=null && httpsql.length()>0){
				sqlExecuter.addSql(httpsql);
			}
		}
	}

	private void addOperation(CacheSession cacheSession,int sessionKilledFlag){
		StringBuffer sb=new StringBuffer();
		sb.append("INSERT INTO OPERATION_LOG(");
		sb.append("SSESSION_ID,AAPPLICATION_ID,PPROTOCOL_ID,OOPERATION_ID,FFLOW_ID,SSTART_TIME,");
		sb.append("EEND_TIME,RREQ,RRESP,RREQ_FLOW,RRESP_FLOW,IIS_FULL) VALUES(");
		sb.append(cacheSession.getSessionId());
		sb.append(",");
		sb.append(cacheSession.getApplicationId());
		sb.append(",");
		sb.append(cacheSession.getProtocolId());
		sb.append(",'");
		sb.append(getBytesValue(cacheSession,0x22,Constants.OPERATION_ID_MAX));//操作标识
		sb.append("','");
		sb.append(getBytesValue(cacheSession,0x21,Constants.FLOW_ID_MAX));//流标识
		sb.append("','");
		sb.append(getTimeValue(cacheSession,0x25));//操作开始时间
		sb.append("','");
		sb.append(getTimeValue(cacheSession,0x26));//操作结束时间
		sb.append("','");
		sb.append(getBytesValue(cacheSession,0x23,Constants.OPERATION_REQ_MAX));//操作命令
		sb.append("','");
		sb.append(getBytesValue(cacheSession,0x24,Constants.OPERATION_RESP_MAX));//操作响应
		sb.append("',");
		sb.append(getLongValue(cacheSession,0x27));//请求字节数
		sb.append(",");
		sb.append(getLongValue(cacheSession,0x28));//响应字节数
		if(null!=cacheSession.getTlvSegments().get(new TLVKey(0x24))){
			if(cacheSession.getTlvSegments().get(new TLVKey(0x24)).limit()>Constants.OPERATION_RESP_MAX){
				sb.append(",0");
			}else{
				sb.append(",1");
			}
		}else{
			sb.append(",1");
		}
		sb.append(")");
		String sql=sb.toString();		
		log.debug("sql = "+sql);
		if(sqlExecuter!=null && sql.length()>0){
			sqlExecuter.addSql(sql);
		}
	}

	private void addFile(CacheSession cacheSession,int sessionKilledFlag){
		StringBuffer sb=new StringBuffer();
		sb.append("INSERT INTO FILE_LOG(");
		sb.append("SSESSION_ID,AAPPLICATION_ID,PPROTOCOL_ID,FFILE_ID,OOPERATION_ID,FFLOW_ID,DDIRECTION,");
		sb.append("PPATH,SSTART_TIME,EEND_TIME) VALUES(");
		sb.append(cacheSession.getSessionId());
		sb.append(",");
		sb.append(cacheSession.getApplicationId());
		sb.append(",");
		sb.append(cacheSession.getProtocolId());
		sb.append(",'");
		sb.append(getBytesValue(cacheSession,0x48,Constants.FILE_ID_MAX));//文件传输标识		
		sb.append("','");
		sb.append(getBytesValue(cacheSession,0x41,Constants.OPERATION_ID_MAX));//操作标识
		sb.append("','");
		sb.append(getBytesValue(cacheSession,0x42,Constants.FLOW_ID_MAX));//流标识
		sb.append("',");
		sb.append(getIntValue(cacheSession,0x43));//方向
		sb.append(",'");
		sb.append(getBytesValue(cacheSession,0x44,Constants.FILE_PATH_MAX));//文件名称
		sb.append("','");
		sb.append(getTimeValue(cacheSession,0x46));//开始时间
		sb.append("','");
		sb.append(getTimeValue(cacheSession,0x47));//结束时间
		sb.append("')");
		String sql=sb.toString();		
		log.debug("sql = "+sql);
		if(sqlExecuter!=null && sql.length()>0){
			sqlExecuter.addSql(sql);
		}
	}
	
	private void updateSession(CacheSession cacheSession,int sessionKilledFlag){
		StringBuffer sb=new StringBuffer();
		sb.append("UPDATE SESSION_LOG SET ");
		sb.append("FFLOW_ID='");
		sb.append(getBytesValue(cacheSession,0x01,Constants.FLOW_ID_MAX));//FFLOW_ID
		sb.append("',SSIP='");
		sb.append(getIpValue(cacheSession,0x02));//SSIP
		sb.append("',SSPORT=");
		sb.append(getIntValue(cacheSession,0x03));//SSPORT
		sb.append(",DDIP='");
		sb.append(getIpValue(cacheSession,0x04));//DDIP
		sb.append("',DDPORT=");
		sb.append(getIntValue(cacheSession,0x05));//DDPORT
		sb.append(",PPRIMARY_ACCOUNT='");
		sb.append(getStringValue(cacheSession,0x06,Constants.PRIMARY_ACCOUNT_MAX));//PPRIMARY_ACCOUNT
		sb.append("',PPROTOCOL_VERSION='");
		sb.append(getStringValue(cacheSession,0x07,Constants.PROTOCOL_VERSION_MAX));//PPROTOCOL_VERSION
		sb.append("',SSERVER_VERSION='");
		sb.append(getStringValue(cacheSession,0x08,Constants.SERVER_VERSION_MAX));//SSERVER_VERSION
		sb.append("',CCLIENT_VERSION='");
		sb.append(getStringValue(cacheSession,0x09,Constants.CLIENT_VERSION_MAX));//CCLIENT_VERSION
		sb.append("',LLOGIN_TIME='");
		sb.append(getTimeValue(cacheSession,0x0A));//LLOGIN_TIME
		sb.append("',LLOGOUT_TIME='");
		sb.append(getTimeValue(cacheSession,0x0C));//LLOGOUT_TIME
		sb.append("',UUSERNAME='");
		sb.append(getStringValue(cacheSession,0x0B,Constants.USERNAME_MAX));//UUSERNAME
		sb.append("',SSUB_PROTOCOL=");
		sb.append(getIntValue(cacheSession,0x0D));//SSUB_PROTOCOL
		sb.append(",SSUB_PROTOCOL_VERSION='");
		sb.append(getStringValue(cacheSession,0x0E,Constants.SUB_PROTOCOL_VERSION_MAX));//SSUB_PROTOCOL_VERSION
		sb.append("',RREQ_FLOW=");
		sb.append(getLongValue(cacheSession,0x0F));//RREQ_FLOW
		sb.append(",RRESP_FLOW=");
		sb.append(getLongValue(cacheSession,0x10));//RRESP_FLOW
		sb.append(",SSMAC='");
		sb.append(getMacValue(cacheSession,0x11));//SSMAC
		sb.append("',DDMAC='");
		sb.append(getMacValue(cacheSession,0x12));//DDMAC
		sb.append("' ");
		if(sessionKilledFlag==Constants.SESSION_LOG_SESSION_KILLED_NORMAL){	
			sb.append(",SSTOP_TIME=");
			sb.append(System.currentTimeMillis());//SSTOP_TIME
			sb.append(",SSTOP_REASON="+Constants.SESSION_LOG_SESSION_KILLED_NORMAL);//SSTOP_REASON
		}else if(sessionKilledFlag==Constants.SESSION_LOG_SESSION_KILLED_TIMEOUT){	
			sb.append(",SSTOP_TIME=");
			sb.append(System.currentTimeMillis());//SSTOP_TIME
			sb.append(",SSTOP_REASON="+Constants.SESSION_LOG_SESSION_KILLED_TIMEOUT);//SSTOP_REASON			
		}
		sb.append(" WHERE SSESSION_ID=");
		sb.append(cacheSession.getSessionId());
		sb.append(" AND AAPPLICATION_ID=");
		sb.append(cacheSession.getApplicationId());
		String sql=sb.toString();		
		log.debug("UPDATE sql = "+sql);
		if(sqlExecuter!=null && sql.length()>0){
			sqlExecuter.addSql(sql);
		}
		
		if(cacheSession.getProtocolId() == 0x06||cacheSession.getProtocolId() == 0x07||cacheSession.getProtocolId() == 0x08){
			//SSH、SCP和SFTP
			StringBuffer sshAndSo = new StringBuffer();
			sshAndSo.append("UPDATE SSH_SESSION_LOG SET ");
			sshAndSo.append("UUP_CHANNEL=");
			sshAndSo.append(getIntValue(cacheSession,0x0101));//上行Channel
			sshAndSo.append(",DDOWN_CHANNEL=");
			sshAndSo.append(getIntValue(cacheSession,0x0102));//下行Channel
			sshAndSo.append(" WHERE SSESSION_ID=");
			sshAndSo.append(cacheSession.getSessionId());
			sshAndSo.append(" AND AAPPLICATION_ID=");
			sshAndSo.append(cacheSession.getApplicationId());
			String sshsql=sshAndSo.toString();		
			log.debug("UPDATE httpsql = "+sshsql);
			if(sqlExecuter!=null && sshsql.length()>0){
				sqlExecuter.addSql(sshsql);
			}
		}else if(cacheSession.getProtocolId() == 0x11||cacheSession.getProtocolId() == 0x12){
			//HTTP(S)
			StringBuffer httpAndSo = new StringBuffer();
			httpAndSo.append("UPDATE HTTP_SESSION_LOG SET ");
			httpAndSo.append("MMETHOD='");
			httpAndSo.append(getStringValue(cacheSession,0x0101,Constants.HTTP_METHOD_MAX));//客户端方法
			httpAndSo.append("',UURL='");
			httpAndSo.append(getStringValue(cacheSession,0x0102,Constants.HTTP_URL_MAX));//URL
			httpAndSo.append("',HHOST='");
			httpAndSo.append(getStringValue(cacheSession,0x0103,Constants.HTTP_HOST_MAX));//HOST
			httpAndSo.append("',RREQ_COOKIE='");
			httpAndSo.append(getStringValue(cacheSession,0x0104,Constants.HTTP_REQ_COOKIE_MAX));//Cookie
			httpAndSo.append("',RREQ_CONTENT_TYPE='");
			httpAndSo.append(getStringValue(cacheSession,0x0105,Constants.HTTP_REQ_CONTENT_TYPE_MAX));//Content-Type
			httpAndSo.append("',RREQ_CONTENT_ENCODING='");
			httpAndSo.append(getStringValue(cacheSession,0x0106,Constants.HTTP_REQ_CONTENT_ENCODING_MAX));//Content-Encoding
			httpAndSo.append("',RREQ_BODY='");
			httpAndSo.append(getStringValue(cacheSession,0x0107,Constants.HTTP_REQ_BODY_MAX));//请求消息内容
			httpAndSo.append("',AACCEPT='");
			httpAndSo.append(getStringValue(cacheSession,0x0108,Constants.HTTP_ACCEPT));//Accept
			httpAndSo.append("',XX_FORWARDED_FOR='");
			httpAndSo.append(getStringValue(cacheSession,0x0109,Constants.HTTP_X_FORWADED_FOR));//X-Forwarded-For
			httpAndSo.append("',RREQ_CONTENT_LENGTH='");
			httpAndSo.append(getStringValue(cacheSession,0x010A,Constants.HTTP_REQ_CONTENT_LENGTH));//请求Content-Length
			httpAndSo.append("',SSTATUS_CODE='");
			httpAndSo.append(getStringValue(cacheSession,0x0181,Constants.HTTP_STATUS_CODE_MAX));//响应号码
			httpAndSo.append("',RREFERER='");
			httpAndSo.append(getStringValue(cacheSession,0x0182,Constants.HTTP_RREFERER_MAX));//Referer
			httpAndSo.append("',SSET_COOKIE='");
			httpAndSo.append(getStringValue(cacheSession,0x0183,Constants.HTTP_SET_COOKIE_MAX));//Set-Cookie
			httpAndSo.append("',CCONTENT_TYPE='");
			String contentType=getStringValue(cacheSession,0x0184,Constants.HTTP_CONTENT_TYPE_MAX);
			httpAndSo.append(contentType);//Content-Type
			httpAndSo.append("',CCONTENT_ENCODING='");
			String contentEncoding=getStringValue(cacheSession,0x0185,Constants.HTTP_CONTENT_ENCODING_MAX);
			
			httpAndSo.append(contentEncoding);//Content-Encoding
			httpAndSo.append("',CCONTENT='");
			if(contentType.startsWith("text") && contentEncoding.length()==0){
				httpAndSo.append(getStringValue(cacheSession,0x0186,Constants.HTTP_CONTENT));//响应消息内容	
			}else{
				httpAndSo.append(getBytesValue(cacheSession,0x0186,Constants.HTTP_CONTENT));//响应消息内容
			}
			
			
			
			httpAndSo.append("',DDATE='");
			httpAndSo.append(getStringValue(cacheSession,0x0187,Constants.HTTP_DATE));//Date
			httpAndSo.append("',SSTATUS_MESSAGE='");
			httpAndSo.append(getStringValue(cacheSession,0x0188,Constants.HTTP_STATUS_MESSAGE));//Status Message
			httpAndSo.append("',LLOCATION='");
			httpAndSo.append(getStringValue(cacheSession,0x0189,Constants.HTTP_LOCATION));//Location
			httpAndSo.append("',TTRANSFER_ENCODING='");
			httpAndSo.append(getStringValue(cacheSession,0x018A,Constants.HTTP_TRANSFER_ENCODING));//Transfer-Encoding
			httpAndSo.append("',CCONTENT_LENGTH='");
			httpAndSo.append(getStringValue(cacheSession,0x018B,Constants.HTTP_CONTENT_LENGTH));//响应Content-Length
			httpAndSo.append("' WHERE SSESSION_ID=");
			httpAndSo.append(cacheSession.getSessionId());
			httpAndSo.append(" AND AAPPLICATION_ID=");
			httpAndSo.append(cacheSession.getApplicationId());
			String httpsql=httpAndSo.toString();		
			log.debug("UPDATE httpsql = "+httpsql);
			if(sqlExecuter!=null && httpsql.length()>0){
				sqlExecuter.addSql(httpsql);
			}
		}
	}
	
	private void updateOperation(CacheSession cacheSession,int sessionKilledFlag){
		StringBuffer sb=new StringBuffer();
		sb.append("UPDATE OPERATION_LOG SET ");
		sb.append("FFLOW_ID='");
		sb.append(getBytesValue(cacheSession,0x21,Constants.FLOW_ID_MAX));//流标识
		sb.append("',SSTART_TIME='");
		sb.append(getTimeValue(cacheSession,0x25));//操作开始时间
		sb.append("',EEND_TIME='");
		sb.append(getTimeValue(cacheSession,0x26));//操作结束时间
		sb.append("',RREQ='");
		sb.append(getBytesValue(cacheSession,0x23,Constants.OPERATION_REQ_MAX));//操作命令
		sb.append("',RRESP='");
		sb.append(getBytesValue(cacheSession,0x24,Constants.OPERATION_RESP_MAX));//操作响应
		sb.append("',RREQ_FLOW=");
		sb.append(getLongValue(cacheSession,0x27));//请求字节数
		sb.append(",RRESP_FLOW=");
		sb.append(getLongValue(cacheSession,0x28));//响应字节数
		sb.append(",IIS_FULL=");
		if(null!=cacheSession.getTlvSegments().get(new TLVKey(0x24))){
			if(cacheSession.getTlvSegments().get(new TLVKey(0x24)).limit()>Constants.OPERATION_RESP_MAX){
				sb.append("0");
			}else{
				sb.append("1");
			}
		}else{
			sb.append("1");
		}
		
		sb.append(" WHERE SSESSION_ID=");
		sb.append(cacheSession.getSessionId());
		sb.append(" AND AAPPLICATION_ID=");
		sb.append(cacheSession.getApplicationId());
		sb.append(" AND OOPERATION_ID='");
		sb.append(getBytesValue(cacheSession,0x22,Constants.OPERATION_ID_MAX));
		sb.append("'");
		String sql=sb.toString();		
		log.debug("UPDATE sql = "+sql);
		if(sqlExecuter!=null && sql.length()>0){
			sqlExecuter.addSql(sql);
		}
	}
	
	private void updateFile(CacheSession cacheSession,int sessionKilledFlag){
		StringBuffer sb=new StringBuffer();
		sb.append("UPDATE FILE_LOG SET ");
		sb.append("FFLOW_ID='");
		sb.append(getBytesValue(cacheSession,0x42,Constants.FLOW_ID_MAX));//流标识
		sb.append("',DDIRECTION=");
		sb.append(getIntValue(cacheSession,0x43));//方向
		sb.append(",PPATH='");
		sb.append(getBytesValue(cacheSession,0x44,Constants.FILE_PATH_MAX));//文件名称
		sb.append("',SSTART_TIME='");
		sb.append(getTimeValue(cacheSession,0x46));//开始时间
		sb.append("',EEND_TIME='");
		sb.append(getTimeValue(cacheSession,0x47));//结束时间
		sb.append("' WHERE SSESSION_ID=");
		sb.append(cacheSession.getSessionId());
		sb.append(" AND AAPPLICATION_ID=");
		sb.append(cacheSession.getApplicationId());
		sb.append(" AND FFILE_ID='");
		sb.append(getBytesValue(cacheSession,0x48,Constants.FILE_ID_MAX));		
		sb.append("' AND OOPERATION_ID='");
		sb.append(getBytesValue(cacheSession,0x41,Constants.OPERATION_ID_MAX));
		sb.append("'");
		String sql=sb.toString();		
		log.debug("UPDATE sql = "+sql);
		if(sqlExecuter!=null && sql.length()>0){
			sqlExecuter.addSql(sql);
		}
	}

	private ISqlExecuter sqlExecuter;

	public void setSqlExecuter(ISqlExecuter sqlExecuter) {
		this.sqlExecuter = sqlExecuter;
	}
}
