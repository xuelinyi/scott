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
import com.comverse.timesheet.web.server.IPlainDataFileSaver;
import com.comverse.timesheet.web.server.IServer;
import com.comverse.timesheet.web.server.codec.PlainDataMessageCodecFactory;
import com.comverse.timesheet.web.server.message.PlainDataSegmentMessage;

public class PlainDataServer  extends IoHandlerAdapter implements IServer {
	private static Logger log = Logger.getLogger(PlainDataServer.class);
	private int port=Constants.PLAIN_DATA_SERVER_DEFAULT_PORT;
	public void setPort(int port) {
		this.port = port;
	}	
	private IPlainDataFileSaver plainDataFileSaver;

	public void setPlainDataFileSaver(IPlainDataFileSaver plainDataFileSaver) {
		this.plainDataFileSaver = plainDataFileSaver;
	}

	private NioSocketAcceptor acceptor = null;

	public void start() {
		try {
			acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors()>2?Runtime.getRuntime().availableProcessors()/2:1);
			//acceptor.getFilterChain().addLast("logger", new LoggingFilter());
			acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter(new PlainDataMessageCodecFactory()));//指定编码过滤器 
			acceptor.setHandler(this);//指定业务逻辑处理器 
			acceptor.setDefaultLocalAddress( new InetSocketAddress(port));//设置端口号
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
		if(message instanceof PlainDataSegmentMessage){
			PlainDataSegmentMessage msg=(PlainDataSegmentMessage)message;
			if(msg.getSessionId()==Constants.MAGIC_NUMBER){
				log.debug("PREAMBLE...");
				return;
			}
			msg.setAddress(((InetSocketAddress)session.getRemoteAddress()).getHostString());
			if(plainDataFileSaver!=null){
				plainDataFileSaver.add(msg);
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
