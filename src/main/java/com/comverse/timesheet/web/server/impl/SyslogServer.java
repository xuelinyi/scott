package com.comverse.timesheet.web.server.impl;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Date;

import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

import com.comverse.timesheet.web.server.IServer;
import com.comverse.timesheet.web.server.ISyslogSaver;
import com.comverse.timesheet.web.server.codec.LogMessageCodecFactory;
import com.comverse.timesheet.web.server.message.LogMessageHeader;


public class SyslogServer  extends IoHandlerAdapter implements IServer {
	private static Logger log = Logger.getLogger(SyslogServer.class);
	private int port=Constants.SYSLOG_SERVER_DEFAULT_PORT;
	public void setPort(int port) {
		this.port = port;
	}

	private ISyslogSaver syslogSaver=null;

	public void setSyslogSaver(ISyslogSaver syslogSaver) {
		this.syslogSaver = syslogSaver;
	}

	private NioSocketAcceptor acceptor = null;

	public void start() {
		try {

			acceptor = new NioSocketAcceptor();
			//acceptor.getFilterChain().addLast("logger", new LoggingFilter());
			acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter(new LogMessageCodecFactory()));//指定编码过滤器 
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
	
	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		log.error(cause, cause);
	}

	@Override
	public void sessionCreated(IoSession session) {
		// 显示客户端的ip和端口
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if(message instanceof LogMessageHeader){
			LogMessageHeader logMessage=(LogMessageHeader)message;
			if(logMessage.getId()==Constants.MAGIC_NUMBER){
				log.debug("PREAMBLE...");
				return;
			}
			syslogSaver.add(logMessage);
		}
	}

	public static void main(String[] args) throws IOException {
		SyslogServer ps = new SyslogServer();
		ps.start();

	}

}