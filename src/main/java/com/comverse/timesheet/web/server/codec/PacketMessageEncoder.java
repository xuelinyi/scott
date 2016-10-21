package com.comverse.timesheet.web.server.codec;

import java.nio.ByteOrder;



import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.message.PacketMessage;

import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class PacketMessageEncoder extends ProtocolEncoderAdapter {
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		if (message instanceof PacketMessage) {
			PacketMessage msg = (PacketMessage) message;
			IoBuffer buffer = IoBuffer.allocate(2048).setAutoExpand(true);
			buffer.order(ByteOrder.BIG_ENDIAN);
			
			buffer.put(Constants.MAGIC_BYTE);
			buffer.putLong(msg.getTime());
			buffer.putUnsignedShort(msg.getCaplen());
			buffer.putUnsignedShort(msg.getLen());
			buffer.put(msg.getPacket());
			buffer.flip();
			byte[] b = new byte[buffer.limit()];
			buffer.get(b);

			buffer.flip();
			out.write(buffer);
		}else if(message instanceof IoBuffer){
			IoBuffer buffer=(IoBuffer)message;
			buffer.flip();
			out.write(buffer);				
		}
	}

}
