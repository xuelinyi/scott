package com.comverse.timesheet.web.server;

public class Constants {
	public static final int SERVER_READ_BUFFER_SIZE=2*1024*1024;
	public static final long MAGIC_NUMBER=0xFEFEFEFEFEFEFEFEL;

	public static final byte MAGIC_BYTE=(byte)0xFE;
	public static final int MAX_SEGMENT_SIZE=65535;//default value 2048, test value 65535
	
	
	public static final int SYSLOG_SERVER_DEFAULT_PORT=9030;
	public static final int SYSLOG_TYPE_ADMIN=0;
	public static final int SYSLOG_TYPE_AUDIT=2;
	public static final int SYSLOG_TYPE_DEVICE=1;
	public static final int SYSLOG_MAX_LENGTH=2048;
	public static final int SYSLOG_HEADER_LENGTH=12;
	
	public static final int PACKET_SERVER_DEFAULT_PORT=9000;
	public static final int PACKET_HEADER_LENGTH=13;
	public static final long PACKET_FILE_MAX_SIZE=20*1024;
	public static final String PACKET_DATA_DIR_IID="PACKET_DATA_DIR";
	
	public static final int SESSION_LOG_SERVER_DEFAULT_PORT=9020;
	public static final int SESSION_LOG_HEADER_LENGTH=21;
	public static final int SESSION_LOG_TYPE_SESSION=0x01;
	public static final int SESSION_LOG_TYPE_OPERATION=0x02;
	public static final int SESSION_LOG_TYPE_FILE=0x03;
	public static final long SESSION_LOG_TIMEOUT=5L*60L*1000L;//default 30L*60L*1000L
	
	public static final int SESSION_LOG_SESSION_KILLED_NO=0x00;
	public static final int SESSION_LOG_SESSION_KILLED_NORMAL=0x01;
	public static final int SESSION_LOG_SESSION_KILLED_TIMEOUT=0x02;
	
	
	public static final int PLAIN_DATA_SERVER_DEFAULT_PORT=9010;
	public static final int PLAIN_DATA_HEADER_LENGTH=20;	
	public static final String PLAIN_DATA_DIR_IID="SESSION_DATA_DIR";
	public static final int MAX_PLAIN_DATA_SIZE=65535;
	
	public static final int FLOW_ID_MAX=128-1;
	public static final int PRIMARY_ACCOUNT_MAX=128-1;
	public static final int PROTOCOL_VERSION_MAX=64-1;
	public static final int SERVER_VERSION_MAX=64-1;
	public static final int CLIENT_VERSION_MAX=64-1;
	public static final int USERNAME_MAX=128-1;
	public static final int SUB_PROTOCOL_VERSION_MAX=64-1;
	
	public static final int HTTP_METHOD_MAX=8-1;
	public static final int HTTP_URL_MAX=2048-1;
	public static final int HTTP_HOST_MAX=1024-1;
	public static final int HTTP_REQ_COOKIE_MAX=1024-1;
	public static final int HTTP_REQ_CONTENT_TYPE_MAX=64-1;
	public static final int HTTP_REQ_CONTENT_ENCODING_MAX=64-1;
	public static final int HTTP_REQ_BODY_MAX=2048-1;
	public static final int HTTP_ACCEPT=64-1;
	public static final int HTTP_X_FORWADED_FOR=64-1;
	public static final int HTTP_REQ_CONTENT_LENGTH=64-1;
	public static final int HTTP_STATUS_CODE_MAX=8-1;
	public static final int HTTP_RREFERER_MAX=2048-1;
	public static final int HTTP_SET_COOKIE_MAX=1024-1;
	public static final int HTTP_CONTENT_TYPE_MAX=64-1;
	public static final int HTTP_CONTENT_ENCODING_MAX=64-1;
	public static final int HTTP_CONTENT=2048-1;
	public static final int HTTP_STATUS_MESSAGE=64-1;
	public static final int HTTP_DATE=64-1;
	public static final int HTTP_LOCATION=64-1;
	public static final int HTTP_TRANSFER_ENCODING=64-1;
	public static final int HTTP_CONTENT_LENGTH=64-1;
	
	
	public static final int FILE_ID_MAX=128-1;
	public static final int OPERATION_ID_MAX=128-1;
	public static final int OPERATION_REQ_MAX=2048-1;
	public static final int OPERATION_RESP_MAX=2048-1;
	
	public static final int FILE_PATH_MAX=1024-1;
	
}
