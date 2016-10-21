package com.comverse.timesheet.web.server.codec;

import java.nio.ByteOrder;

import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.message.SessionSegmentMessage;
import com.comverse.timesheet.web.server.message.TLVMessage;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class SessionMessageDecoder extends CumulativeProtocolDecoder {
	private static Logger log = Logger.getLogger(SessionMessageDecoder.class);
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out){
		if (in.remaining() < Constants.SESSION_LOG_HEADER_LENGTH) {
			return false;
		}
		in.order(ByteOrder.BIG_ENDIAN);
		in.mark();// 标记当前位置，以便reset
		SessionSegmentMessage msg = new SessionSegmentMessage();
		
		msg.setIdentifier(in.get());
		msg.setSessionId(in.getLong());
		msg.setApplicationId(in.getLong());
		msg.setProtocolId(in.get()&0x0FF);
		msg.setType(in.get()&0x0FF);
		int length = in.getUnsignedShort();
		if((msg.getIdentifier()!=Constants.MAGIC_BYTE )|| (length<Constants.SESSION_LOG_HEADER_LENGTH)){//too small length
			log.error("Data not synchonized! sessionId = "+msg.getSessionId()+" length = "+length);
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
		if ((msg.getIdentifier()!=Constants.MAGIC_BYTE )|| (in.remaining() < length - Constants.SESSION_LOG_HEADER_LENGTH)) {// 如果消息内容不够，则重置，相当于不读取size
			in.reset();
			if(in.remaining()<Constants.MAX_SEGMENT_SIZE){
				return false;// 接收新数据，以拼凑成完整数据				
			}else{//数据不同步了，需要找同步字段
				log.error("Data not synchonized! sessionId = "+msg.getSessionId()+" length = "+length);
				while(in.remaining()>0){
					in.mark();
					byte b=in.get();
					if(b==Constants.MAGIC_BYTE){
						in.reset();
						return false;
					}
				}
				if(in.remaining()==0){//!!!
					return false;
				}
			}
		}
		
		//不能因为里面编码不对，跨段影响其他消息，采用count记录已经读取的长度
		int count=Constants.SESSION_LOG_HEADER_LENGTH;
		in.mark();
		while ((in.remaining()>=4)&&(count<length)) {
			int tag = in.getUnsignedShort();
			int len = in.getUnsignedShort();
			count+=4;
			if((len>=4) && (count<=length) && (in.remaining()>=len-4)){
				byte[] bytes = new byte[len-4];
				in.get(bytes);
				msg.getTlvList().add(new TLVMessage(tag, bytes));
				count+=len-4;
			}else{				
				log.error("sessionId = "+msg.getSessionId()+" message format error! tag = "+tag+" len = "+len+" count = "+count+" length = "+length);
				in.reset();
				return false;
			}
		}
		log.debug("receive message "+msg);
		out.write(msg);
		return true;
	}

}
