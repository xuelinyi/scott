package com.comverse.timesheet.web.server.codec;

import java.nio.ByteOrder;

import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.message.AdminLogMessage;
import com.comverse.timesheet.web.server.message.AuditLogMessage;
import com.comverse.timesheet.web.server.message.DeviceLogMessage;
import com.comverse.timesheet.web.server.message.LogMessageHeader;
import com.comverse.timesheet.web.server.util.BinaryHelper;

public class LogMessageDecoder extends CumulativeProtocolDecoder {
	private static Logger log = Logger.getLogger(LogMessageDecoder.class);
	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {		
		if (in.remaining() < Constants.SYSLOG_HEADER_LENGTH) {
			return false;
		}
		in.order(ByteOrder.BIG_ENDIAN);
		in.mark();// 标记当前位置，以便reset
			
		LogMessageHeader logMessage = new LogMessageHeader();
		
		logMessage.setIdentifier(in.get());
		
		logMessage.setId(in.getLong());
		logMessage.setType(in.get());
		int length = in.getUnsignedShort();//length		
		if((logMessage.getIdentifier()!=Constants.MAGIC_BYTE )|| (length<Constants.SYSLOG_HEADER_LENGTH)){
			log.error("Data not synchonized! id = "+logMessage.getId()+" type = "+logMessage.getType()+" length = "+length);
			while(in.remaining()>0){
				in.mark();
				byte b=in.get();
				if(b==Constants.MAGIC_BYTE){
					in.reset();
					return false;
				}
			}
			if(in.remaining()==0){
				return false;
			}			
		}		
		if ((logMessage.getIdentifier()!=Constants.MAGIC_BYTE )|| (in.remaining() < length-Constants.SYSLOG_HEADER_LENGTH)) {//如果消息内容不够，则重置，相当于不读取size
			in.reset();
			if(in.remaining()<Constants.MAX_SEGMENT_SIZE){				
				return false;// 接收新数据，以拼凑成完整数据				
			}else{//数据不同步了，需要找同步字段
				log.error("Data not synchonized! id = "+logMessage.getId()+" type = "+logMessage.getType()+" length = "+length);
				while(in.remaining()>0){
					in.mark();
					byte b=in.get();
					if(b==Constants.MAGIC_BYTE){
						in.reset();
						return false;
					}					
				}
				if(in.remaining()==0){
					return false;
				}
			}
		}
		//不能因为里面编码不对，跨段影响其他消息，采用count记录已经读取的长度
		int count=Constants.SYSLOG_HEADER_LENGTH;
		if (Constants.SYSLOG_TYPE_ADMIN == logMessage.getType()) {
			AdminLogMessage msg = new AdminLogMessage(logMessage);
			while ((in.remaining()>=4)&&(count<length)) {
				int tag = in.getUnsignedShort();
				int len = in.getUnsignedShort() - 4;
				count+=4;
				if((len<0) || (count>=length) || (in.remaining()<len)){
					log.error("message format error! id = "+msg.getId()+" type = "+msg.getType()+" length = "+length+" tag = "+tag+" len = "+len);
					byte[] bytes = new byte[length-count];
					in.get(bytes);
					break;
				}
				count+=len;
				switch (tag) {
				case 0x01: {
					msg.setTime(in.getLong());
				}
					break;
				case 0x02: {
					msg.setLevel(in.getInt());
				}
					break;
				case 0x04: {
					byte[] bytes = new byte[len];
					in.get(bytes);
					msg.setAccount(new String(bytes));
				}
					break;
				case 0x06: {
					byte[] bytes = new byte[len];
					in.get(bytes);
					msg.setIp(BinaryHelper.bytesToIpString(bytes));
				}
					break;
				case 0x03: {
					byte[] bytes = new byte[len];
					in.get(bytes);
					msg.setDesc(new String(bytes));
				}
					break;
				}
			}
			log.debug("receive message "+msg);
			out.write(msg);
			return true;
		} else if (Constants.SYSLOG_TYPE_AUDIT == logMessage.getType()) {
			AuditLogMessage msg = new AuditLogMessage(logMessage);
			while ((in.remaining()>=4)&&(count<length)) {
				int tag = in.getUnsignedShort();
				int len = in.getUnsignedShort() - 4;
				count+=4;
				if((len<0) || (count>length) || (in.remaining()<len)){
					log.error("message format error! id = "+msg.getId()+" type = "+msg.getType()+" length = "+length+" tag = "+tag+" len = "+len);
					byte[] bytes = new byte[length-count];
					in.get(bytes);
					break;
				}
				count+=len;
				switch (tag) {
				case 0x01: {
					msg.setTime(in.getLong());
				}
					break;
				case 0x02: {
					msg.setLevel(in.getInt());
				}
					break;
				case 0x07: {
					msg.setSessionId(in.getLong());
				}				
				break;
				case 0x08: {
					msg.setApplicationId(in.getLong());
				}				
					break;
				case 0x09: {
					msg.setProtocolId(in.get());
				}
					break;
				case 0x0a: {
					byte[] bytes = new byte[len];
					in.get(bytes);
					msg.setsIp(BinaryHelper.bytesToIpString(bytes));
				}
					break;
				case 0x0b: {
					byte[] bytes = new byte[len];
					in.get(bytes);
					msg.setdIp(BinaryHelper.bytesToIpString(bytes));
				}
					break;
				case 0x0c: {
					msg.setsPort(in.getInt());
				}				
					break;
				case 0x0d: {
					msg.setdPort(in.getInt());
				}				
					break;					
				case 0x03: {
					byte[] bytes = new byte[len];
					in.get(bytes);
					msg.setDesc(new String(bytes));
				}
					break;
				}
		
			}
			log.debug("receive message "+msg);
			out.write(msg);
			return true;
		} else if (Constants.SYSLOG_TYPE_DEVICE == logMessage.getType()) {
			DeviceLogMessage msg = new DeviceLogMessage(logMessage);
			while ((in.remaining()>=4)&&(count<length)) {
				int tag = in.getUnsignedShort();
				int len = in.getUnsignedShort() - 4;
				count+=4;
				if((len<0) || (count>=length) || (in.remaining()<len)){
					log.error("message format error! id = "+msg.getId()+" type = "+msg.getType()+" length = "+length+" tag = "+tag+" len = "+len);
					byte[] bytes = new byte[length-count];
					in.get(bytes);
					break;
				}
				count+=len;
				switch (tag) {
				case 0x01: {
					msg.setTime(in.getLong());
				}
					break;
				case 0x02: {
					msg.setLevel(in.getInt());
				}
					break;
				case 0x05: {
					byte[] bytes = new byte[len];
					in.get(bytes);
					msg.setDeviceName(new String(bytes));
				}
					break;
				case 0x06: {
					byte[] bytes = new byte[len];
					in.get(bytes);
					msg.setIp(new String(bytes));
				}
					break;
				case 0x03: {
					byte[] bytes = new byte[len];
					in.get(bytes);
					msg.setDesc(new String(bytes));
				}
					break;
				}
			}
			log.debug("receive message "+msg);
			out.write(msg);
			return true;
		}else if (Constants.MAGIC_BYTE == logMessage.getType()){			
			out.write(logMessage);
			return true;
		}else{
			log.error("message format error! id = "+logMessage.getId()+" type = "+logMessage.getType()+" length = "+length);
			byte[] bytes = new byte[length-Constants.SYSLOG_HEADER_LENGTH];
			in.get(bytes);
			return false;			
		}
	}

}
