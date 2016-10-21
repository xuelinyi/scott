package com.comverse.timesheet.web.server.codec;

import java.nio.ByteOrder;

import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.message.PlainDataMessage;
import com.comverse.timesheet.web.server.util.BinaryHelper;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

public class PlainDataMessageEncoder extends ProtocolEncoderAdapter {
	private static final int MAX_MSG_LEN = 1024;

	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
		if (message instanceof PlainDataMessage) {
			PlainDataMessage msg = (PlainDataMessage) message;

			if (msg.getDataLen() <= 0) {
				IoBuffer buffer = IoBuffer.allocate(2048).setAutoExpand(true);
				buffer.order(ByteOrder.BIG_ENDIAN);
				
				buffer.put(Constants.MAGIC_BYTE);
				buffer.putLong(msg.getSessionId());
				buffer.putLong(0);
				buffer.put((byte) 0x0);
				buffer.putUnsignedShort(0);
				
				buffer.putUnsignedShort(18, buffer.position());// modify length				
				buffer.flip();
				byte[] b = new byte[buffer.limit()];
				buffer.get(b);
				buffer.flip();
				out.write(buffer);
			} else {
				{
					IoBuffer buffer = IoBuffer.allocate(2048).setAutoExpand(true);
					buffer.order(ByteOrder.BIG_ENDIAN);
					
					buffer.put(Constants.MAGIC_BYTE);
					
					buffer.putLong(msg.getSessionId());
					buffer.putLong(0);
					buffer.put((byte) 1);// first
					buffer.putUnsignedShort(0);// current segment length

					buffer.putInt(msg.getDataLen());
					buffer.put((byte) msg.getProtocolId());
					buffer.putLong(msg.getTime());
					buffer.put(BinaryHelper.macToBinary(msg.getSrcMac()));
					buffer.put(BinaryHelper.macToBinary(msg.getDstMac()));
					buffer.put(BinaryHelper.ipv4ToBinary(msg.getSrcIp()));
					buffer.putShort((short) msg.getSrcPort());
					buffer.put(BinaryHelper.ipv4ToBinary(msg.getDstIp()));
					buffer.putShort((short) msg.getDstPort());
					buffer.putUnsignedShort(18, buffer.position());// modify
																	// length
					buffer.flip();
					byte[] b = new byte[buffer.limit()];
					buffer.get(b);
					System.out.println(">>>"+Hex.encodeHexString(b));
					buffer.flip();
					out.write(buffer);
				}
				int segments = msg.getDataLen() / MAX_MSG_LEN;
				if (msg.getDataLen() % MAX_MSG_LEN > 0) {
					segments++;
				}
				for(int i = 0; i < segments; i++) {
					IoBuffer buffer = IoBuffer.allocate(2048).setAutoExpand(true);
					buffer.order(ByteOrder.BIG_ENDIAN);
					
					buffer.put(Constants.MAGIC_BYTE);
					buffer.putLong(msg.getSessionId());
					buffer.putLong(0);
					buffer.put((byte) (i + 2));
					buffer.putUnsignedShort(0);// current segment length
					if (i == segments - 1) {// last one
						buffer.put(16, (byte) 0);
						byte[] buf = new byte[msg.getDataLen() - MAX_MSG_LEN * i];
						System.arraycopy(msg.getData(), i * MAX_MSG_LEN, buf, 0, msg.getDataLen() - MAX_MSG_LEN * i);
						buffer.put(buf);
					} else {
						byte[] buf = new byte[MAX_MSG_LEN];
						System.arraycopy(msg.getData(), i * MAX_MSG_LEN, buf, 0, MAX_MSG_LEN);
						buffer.put(buf);
					}
					buffer.putUnsignedShort(18, buffer.position());// modify
																	// length
					buffer.flip();
					byte[] b = new byte[buffer.limit()];
					buffer.get(b);
					System.out.println(">>>"+Hex.encodeHexString(b));
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
