package com.comverse.timesheet.web.server.codec;

import java.nio.ByteOrder;

import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.message.PacketMessage;

import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.CumulativeProtocolDecoder;
import org.apache.mina.filter.codec.ProtocolDecoderOutput;

public class PacketMessageDecoder extends CumulativeProtocolDecoder {
	private static Logger log = Logger.getLogger(PacketMessageDecoder.class);
	
	@Override
	protected boolean doDecode(IoSession session, IoBuffer in, ProtocolDecoderOutput out) throws Exception {
		if (in.remaining() < Constants.PACKET_HEADER_LENGTH) {
			return false;
		}
		in.order(ByteOrder.BIG_ENDIAN);
		in.mark();// 标记当前位置，以便reset
		PacketMessage msg = new PacketMessage();
		
		msg.setIdentifier(in.get());
		msg.setTime(in.getLong());
		msg.setCaplen(in.getUnsignedShort());
		msg.setLen(in.getUnsignedShort());
		System.out.println("\r\nPacketMessage = "+msg);
		
		if((msg.getIdentifier()!=Constants.MAGIC_BYTE )|| (msg.getCaplen()<0)){
			log.error("Data not synchonized! time = "+msg.getTime()+" caplen = "+msg.getCaplen());
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
		if ((msg.getIdentifier()!=Constants.MAGIC_BYTE )||(in.remaining() < msg.getCaplen())) {// 如果消息内容不够，则重置，相当于不读取size
			in.reset();
			if(in.remaining()<Constants.MAX_SEGMENT_SIZE){				
				return false;// 接收新数据，以拼凑成完整数据				
			}else{//数据不同步了，需要找同步字段
				log.error("Data not synchonized! time = "+msg.getTime()+" caplen = "+msg.getCaplen());
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
		msg.setPacket(new byte[msg.getCaplen()]);
		in.get(msg.getPacket());

		log.debug("receive message "+msg);
		System.out.println("\r\nPacketMessage = "+msg);
		
		out.write(msg);
		return true;
	}

}
