package com.comverse.timesheet.web.server.impl;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.IPacketFileSaver;
import com.comverse.timesheet.web.server.IServer;
import com.comverse.timesheet.web.server.codec.PacketMessageCodecFactory;
import com.comverse.timesheet.web.server.message.PacketMessage;
import com.comverse.timesheet.web.server.util.BinaryHelper;

public class PacketServer extends IoHandlerAdapter implements IServer {
	private static Logger log = Logger.getLogger(PacketServer.class);
	private int port=Constants.PACKET_SERVER_DEFAULT_PORT;
	public void setPort(int port) {
		this.port = port;
	}	
	private IPacketFileSaver packetFileSaver=null;
	public void setPacketFileSaver(IPacketFileSaver packetFileSaver) {
		this.packetFileSaver = packetFileSaver;
	}

	private NioSocketAcceptor acceptor = null;

	public void start() {
		try {
			acceptor = new NioSocketAcceptor();
			//acceptor.getFilterChain().addLast("logger", new LoggingFilter());
			acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new PacketMessageCodecFactory()));// 指定编码过滤器
			acceptor.setHandler(this);//指定业务逻辑处理器 
			acceptor.setDefaultLocalAddress(new InetSocketAddress(port));// 设置端口号

			acceptor.setReuseAddress(true);
			acceptor.getSessionConfig().setReadBufferSize(Constants.SERVER_READ_BUFFER_SIZE);
			
			acceptor.bind();
			
		} catch (IOException e) {
			log.error(e, e);
		}// 启动监听
	}
	public void stop() {
		try {

			acceptor.dispose();
		} catch (Exception e) {
			log.error(e, e);
		}
	}
	private long lastTime=System.currentTimeMillis();
	private long lastBytes=0;
	private List<IoSession> sessions=new ArrayList<IoSession>();
	
	@Override
	public void sessionCreated(IoSession session) {
		if(session!=null){
			synchronized(this){
				sessions.add(session);	
			}
			
		}		
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		if(session!=null){
			synchronized(this){
				sessions.remove(session);
			}
		}				
		super.sessionClosed(session);
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if(message instanceof PacketMessage){
			PacketMessage msg=(PacketMessage)message;
			
			String collectorIp=(String)session.getAttribute("collectorIp");
			if(collectorIp==null){
				collectorIp=((InetSocketAddress)session.getRemoteAddress()).getHostString();
				session.setAttribute("collectorIp",collectorIp);
			}
			
			if(msg.getTime()==Constants.MAGIC_NUMBER){
				log.debug("PREAMBLE...");
				return;
			}else if(msg.getTime()==0xFDFDFDFDFDFDFDFDL){			
				log.debug("client ipaddress from proxy!");
				if(msg.getCaplen()==5){
					byte[]bytes=new byte[4];
					bytes[0]=msg.getPacket()[1];
					bytes[1]=msg.getPacket()[2];
					bytes[2]=msg.getPacket()[3];
					bytes[3]=msg.getPacket()[4];
					collectorIp=BinaryHelper.bytesToIpString(bytes);
					session.setAttribute("collectorIp",collectorIp);
					log.debug("collectorIp = "+collectorIp);
					return;
				}else if(msg.getCaplen()==17){
					log.debug("Not support! msg = "+msg);
					return;						
				}
			}
			
			msg.setAddress(collectorIp);	
			
			if(packetFileSaver!=null){				
				packetFileSaver.add(msg);
			}
		}
	long curTime = System.currentTimeMillis();
		
		if (curTime - lastTime > 10L * 1000L) {
			synchronized (this) {
				log.info("state: " + sessions.size()+" sessions");
				long totalBytes = 0;
				for (IoSession tmp : sessions) {
					totalBytes += tmp.getReadBytes();
				}
				long curBytes=totalBytes-lastBytes;
				log.info("state: " + (curBytes * 8 / (curTime - lastTime)) + " Kbps");
				log.info("state: " + (curBytes * 8 / (curTime - lastTime)/ 1000L) + " Mbps");
				lastTime = System.currentTimeMillis();
				lastBytes=totalBytes;
			}
		}
	}

	public static void main(String[] args) throws IOException {
		PacketServer ps = new PacketServer();
		ps.start();

	}

}
