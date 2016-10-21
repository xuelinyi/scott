package com.comverse.timesheet.web.server.dto;

import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.Map;

import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.message.SessionSegmentMessage;
import com.comverse.timesheet.web.server.message.TLVMessage;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;

public class CacheSession{
	private static Logger log = Logger.getLogger(CacheSession.class);
	
	private long collectorId;
	private String collectorIp;
	private long sessionId;
	private long applicationId;
	private int protocolId;	
	private int type;
	private long createTime;
	private Map<TLVKey,IoBuffer>tlvSegments=new HashMap<TLVKey,IoBuffer>();
	private long last=0;//最后更新时间	
	
	private boolean added=false;
	/**
	 * 判断插入数据库的时机
	 * @return
	 */
	public boolean allowAdded() {
		switch (type) {
		case Constants.SESSION_LOG_TYPE_OPERATION:
			if (tlvSegments.get(new TLVKey(0x22)) != null) {// 操作标识
				return true;
			} else {
				return false;
			}
		case Constants.SESSION_LOG_TYPE_FILE:
			if (tlvSegments.get(new TLVKey(0x42)) != null) {// 操作标识
				return true;
			} else {
				return false;
			}
		default:
			return true;
		}
	}
	
	public boolean isAdded() {
		return added;
	}

	public void setAdded(boolean added) {
		this.added = added;
	}

	public CacheSession(SessionSegmentMessage message){
		setSessionId(message.getSessionId());
		setApplicationId(message.getApplicationId());
		setProtocolId(message.getProtocolId());
		setType(message.getType());
		setCollectorId(message.getCollectorId());
		setCollectorIp(message.getCollectorIp());
		setCreateTime(message.getRecvTime());
	}
	
	
	public void merge(SessionSegmentMessage message) {
		for(TLVMessage tlv:message.getTlvList()){
			TLVKey key=new TLVKey(tlv.getTag());
			if((Constants.SESSION_LOG_TYPE_FILE==type)&&(0x45 == key.getTag())){//file
				continue;
			}else if((Constants.SESSION_LOG_TYPE_OPERATION==type)&&(0x24 == key.getTag())){//resp
				//数据中保存部分resp数据
			}else if((Constants.SESSION_LOG_TYPE_SESSION==type)&&((0x11==protocolId)||(0x12==protocolId))&&(0x0186 == key.getTag())){//http content
				//数据中保存部分resp数据
			}else if((Constants.SESSION_LOG_TYPE_SESSION==type)&&(0x18==protocolId)&&(0x0104 == key.getTag())){//smtp data
				//数据中保存部分resp数据
			}
			IoBuffer buf=tlvSegments.get(key);
			if(buf==null){
				buf=IoBuffer.allocate(64).setAutoExpand(true);
				buf.order(ByteOrder.BIG_ENDIAN);
				tlvSegments.put(key, buf);
			}
			
			if((tlv.getTag()&0x08000)==0x08000){//TODO 未考虑顺序问题，目前采用SSL传输，一定是保序的
				if(tlv.getValue().length>12){
					buf.put(tlv.getValue(),12,tlv.getValue().length-12);
				}else if(tlv.getValue().length<12){
					log.error("session = "+message.getSessionId()+" tag = "+tlv.getTag()+" TLV格式错误!");
				}
			}else{
				buf.put(tlv.getValue());
			}
		}		
		last=System.currentTimeMillis();
	}
	
	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public long getApplicationId() {
		return applicationId;
	}
	public void setApplicationId(long applicationId) {
		this.applicationId = applicationId;
	}
	public int getProtocolId() {
		return protocolId;
	}
	public void setProtocolId(int protocolId) {
		this.protocolId = protocolId;
	}
	
	public long getCollectorId() {
		return collectorId;
	}

	public void setCollectorId(long collectorId) {
		this.collectorId = collectorId;
	}

	public String getCollectorIp() {
		return collectorIp;
	}

	public void setCollectorIp(String collectorIp) {
		this.collectorIp = collectorIp;
	}

	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public Map<TLVKey, IoBuffer> getTlvSegments() {
		return tlvSegments;
	}
	public void setTlvSegments(Map<TLVKey, IoBuffer> tlvSegments) {
		this.tlvSegments = tlvSegments;
	}
	public long getLast() {
		return last;
	}
	public void setLast(long last) {
		this.last = last;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("CacheSession [collectorId=");
		builder.append(collectorId);
		builder.append(", collectorIp=");
		builder.append(collectorIp);
		builder.append(", sessionId=");
		builder.append(sessionId);
		builder.append(", applicationId=");
		builder.append(applicationId);
		builder.append(", protocolId=");
		builder.append(protocolId);
		builder.append(", type=");
		builder.append(type);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", tlvSegments=");
		builder.append(tlvSegments);
		builder.append(", last=");
		builder.append(last);
		builder.append(", added=");
		builder.append(added);
		builder.append("]");
		return builder.toString();
	}

	
	
	
}

