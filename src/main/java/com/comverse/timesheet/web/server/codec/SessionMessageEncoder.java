package com.comverse.timesheet.web.server.codec;

import java.nio.ByteOrder;

import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.message.SessionSegmentMessage;
import com.comverse.timesheet.web.server.message.TLVMessage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

/**
 * 由于Encoder仅用于测试，封装并不严格,仅按照TLV的type来分段，并没有按照2048长度分段，可能会出现大于2048的段出现
 * 
 * @author Lei Liu<liul@dsp.ac.cn>
 *
 */
public class SessionMessageEncoder extends ProtocolEncoderAdapter {
	private static final int MAX_SEGMENT_LENGTH=2000;
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		if (message instanceof SessionSegmentMessage) {
			SessionSegmentMessage msg = (SessionSegmentMessage) message;
			for (int i = 0; i < msg.getTlvList().size() + 1; i++) {//+1 for last
				int segments=1;
				if (i < msg.getTlvList().size()) {
					TLVMessage tlv = msg.getTlvList().get(i);
					segments=(tlv.getLen()-4)/MAX_SEGMENT_LENGTH;
					if((tlv.getLen()-4)%MAX_SEGMENT_LENGTH>0){
						segments++;	
					}
				}
				for(int segment=0;segment<segments;segment++){
					IoBuffer buffer = IoBuffer.allocate(2048).setAutoExpand(true);
					buffer.order(ByteOrder.BIG_ENDIAN);
					
					buffer.put(Constants.MAGIC_BYTE);
					buffer.putLong(msg.getSessionId());
					buffer.putLong(msg.getApplicationId());
					buffer.put((byte) msg.getProtocolId());
					buffer.put((byte) msg.getType());

					buffer.putUnsignedShort(0);// 本段长度

					if (i < msg.getTlvList().size()) {
						TLVMessage tlv = msg.getTlvList().get(i);
						if(segments==1){
							buffer.putUnsignedShort(tlv.getTag());
							buffer.putUnsignedShort(tlv.getLen());
							buffer.put(tlv.getValue());							
						}else{
							int sendLength=MAX_SEGMENT_LENGTH;
							if(segment==segments-1){
								sendLength=(tlv.getLen()-4)%MAX_SEGMENT_LENGTH;
							}							
							buffer.putUnsignedShort(0x08000|tlv.getTag());							
							buffer.putUnsignedShort(4+12+sendLength);
							buffer.putUnsignedShort(0x01);
							buffer.putUnsignedShort(4+4);
							buffer.putInt(segment);
							buffer.putUnsignedShort(0x02);
							buffer.putUnsignedShort(4+sendLength);
							buffer.put(tlv.getValue(),segment*MAX_SEGMENT_LENGTH,sendLength);							
						}
					}

					buffer.putUnsignedShort(19, buffer.position());// modify length
					buffer.flip();
					byte[] b = new byte[buffer.limit()];
					buffer.get(b);


					buffer.flip();
					out.write(buffer);					
				}
			}

		} else if (message instanceof IoBuffer) {
			IoBuffer buffer = (IoBuffer) message;
			buffer.flip();
			out.write(buffer);
		}

	}

}
