package com.comverse.timesheet.web.server.codec;

import java.nio.ByteOrder;



import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.message.PlainDataSegmentMessage;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class PlainDataMessageDecoder extends CumulativeProtocolDecoder {
	private static Logger log = Logger.getLogger(PlainDataMessageDecoder.class);
	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() < Constants.PLAIN_DATA_HEADER_LENGTH) {
			return false;
		}
		in.order(ByteOrder.BIG_ENDIAN);
		in.mark();// 标记当前位置，以便reset
		PlainDataSegmentMessage msg = new PlainDataSegmentMessage();
		
		msg.setIdentifier(in.get());
		msg.setSessionId(in.getLong());
		msg.setDataId(in.getLong());
		msg.setSegment(in.get());
		msg.setSegmentLen(in.getUnsignedShort());
		if((msg.getIdentifier()!=Constants.MAGIC_BYTE )||(msg.getSegmentLen()<Constants.PLAIN_DATA_HEADER_LENGTH)){
			log.error("Data not synchonized! sessionId = "+msg.getSessionId()+" dataId = "+msg.getDataId()+" segment = "+msg.getSegment()+" segmentLen = "+msg.getSegmentLen());
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
		if ((msg.getIdentifier()!=Constants.MAGIC_BYTE )||(in.remaining() < msg.getSegmentLen()-Constants.PLAIN_DATA_HEADER_LENGTH)) {// 如果消息内容不够，则重置，相当于不读取size
			in.reset();
			if(in.remaining()<Constants.MAX_SEGMENT_SIZE){
				return false;// 接收新数据，以拼凑成完整数据
			}else{//数据不同步了，需要找同步字段
				log.error("Data not synchonized! sessionId = "+msg.getSessionId()+" dataId = "+msg.getDataId()+" segment = "+msg.getSegment()+" segmentLen = "+msg.getSegmentLen());
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
		
		msg.setSegmentData(new byte[msg.getSegmentLen()-Constants.PLAIN_DATA_HEADER_LENGTH]);
		in.get(msg.getSegmentData());
		
		log.debug("receive message "+msg);
		out.write(msg);
		return true;
		

	}

}
