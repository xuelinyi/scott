package com.comverse.timesheet.web.server.codec;
import java.nio.ByteOrder;

import org.apache.commons.codec.binary.Hex;
import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderAdapter;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;

import com.comverse.timesheet.web.server.impl.Constants;
import com.comverse.timesheet.web.server.message.AdminLogMessage;
import com.comverse.timesheet.web.server.message.AuditLogMessage;
import com.comverse.timesheet.web.server.message.DeviceLogMessage;


public class LogMessageEncoder extends ProtocolEncoderAdapter {
	public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {		
		if(message instanceof AdminLogMessage){
			AdminLogMessage msg=(AdminLogMessage)message;
			IoBuffer buffer = IoBuffer.allocate(2048).setAutoExpand(true);			
			buffer.order(ByteOrder.BIG_ENDIAN);

			buffer.put(Constants.MAGIC_BYTE);
			buffer.putLong(msg.getId());
			
			buffer.put((byte)msg.getType());
			
			buffer.putUnsignedShort(0);//length
			
			buffer.putShort((short)0x01);
			buffer.putShort((short)12);
			buffer.putLong(msg.getTime());

			buffer.putShort((short)0x02);
			buffer.putShort((short)8);
			buffer.putInt(msg.getLevel());

			buffer.putShort((short)0x04);
			buffer.putShort((short)(4+msg.getAccount().getBytes().length));
			buffer.put(msg.getAccount().getBytes());
			
			buffer.putShort((short)0x06);
			buffer.putShort((short)(4+msg.getIpBinary().length));
			buffer.put(msg.getIpBinary());
			
			buffer.putShort((short)0x03);
			buffer.putShort((short)(4+msg.getDesc().getBytes().length));
			buffer.put(msg.getDesc().getBytes());
			
			buffer.putUnsignedShort(10,buffer.position());//modify length
			buffer.flip();
			byte[] b = new byte[buffer.limit()];
			buffer.get(b);
			
			
			
			

			buffer.flip();
	        out.write(buffer);

	        
		}else if(message instanceof AuditLogMessage){
			AuditLogMessage msg=(AuditLogMessage)message;
			IoBuffer buffer = IoBuffer.allocate(2048).setAutoExpand(true);
			buffer.order(ByteOrder.BIG_ENDIAN);
			
			buffer.put(Constants.MAGIC_BYTE);
			buffer.putLong(msg.getId());
			
			buffer.put((byte)msg.getType());
			
			buffer.putUnsignedShort(0);//length
			
			buffer.putShort((short)0x01);
			buffer.putShort((short)12);
			buffer.putLong(msg.getTime());

			buffer.putShort((short)0x02);
			buffer.putShort((short)8);
			buffer.putInt(msg.getLevel());

			buffer.putShort((short)0x07);
			buffer.putShort((short)12);
			buffer.putLong(msg.getSessionId());
			
			buffer.putShort((short)0x08);
			buffer.putShort((short)12);
			buffer.putLong(msg.getApplicationId());
			
			buffer.putShort((short)0x09);
			buffer.putShort((short)5);
			buffer.put((byte)msg.getProtocolId());		
			
		
			buffer.putShort((short)0x0a);
			buffer.putShort((short)(4+msg.getSIpBinary().length));
			buffer.put(msg.getSIpBinary());
			

			
			buffer.putShort((short)0x0b);
			buffer.putShort((short)(4+msg.getDIpBinary().length));
			buffer.put(msg.getDIpBinary());
			
			buffer.putShort((short)0x0c);
			buffer.putShort((short)8);
			buffer.putInt(msg.getsPort());
			
			buffer.putShort((short)0x0d);
			buffer.putShort((short)8);
			buffer.putInt(msg.getdPort());

			buffer.putShort((short)0x03);
			buffer.putShort((short)(4+msg.getDesc().getBytes().length));
			buffer.put(msg.getDesc().getBytes());
			
			
			buffer.putUnsignedShort(10,(short)buffer.position());//modify length
			buffer.flip();
			byte[] b = new byte[buffer.limit()];
			buffer.get(b);
			
			
			
			

			buffer.flip();
	        out.write(buffer);			
			
		}else if(message instanceof DeviceLogMessage){
			DeviceLogMessage msg=(DeviceLogMessage)message;
			IoBuffer buffer = IoBuffer.allocate(2048).setAutoExpand(true);
			buffer.order(ByteOrder.BIG_ENDIAN);
			
			buffer.put(Constants.MAGIC_BYTE);
			buffer.putLong(msg.getId());
			
			buffer.put((byte)msg.getType());
			
			buffer.putUnsignedShort(0);//length
			
			buffer.putShort((short)0x01);
			buffer.putShort((short)12);
			buffer.putLong(msg.getTime());

			buffer.putShort((short)0x02);
			buffer.putShort((short)8);
			buffer.putInt(msg.getLevel());

			buffer.putShort((short)0x05);
			buffer.putShort((short)(4+msg.getDeviceName().getBytes().length));
			buffer.put(msg.getDeviceName().getBytes());
			
			buffer.putShort((short)0x06);
			buffer.putShort((short)(4+msg.getIp().getBytes().length));
			buffer.put(msg.getIp().getBytes());
			
			buffer.putShort((short)0x03);
			buffer.putShort((short)(4+msg.getDesc().getBytes().length));
			buffer.put(msg.getDesc().getBytes());
			
			buffer.putUnsignedShort(10,(short)buffer.position());//modify length
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