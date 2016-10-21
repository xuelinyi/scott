package com.comverse.timesheet.web.server.impl;

import java.io.IOException;



import java.net.InetSocketAddress;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListSet;

import javax.sql.DataSource;


import org.apache.log4j.Logger;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.IServer;
import com.comverse.timesheet.web.server.ISessionCache;
import com.comverse.timesheet.web.server.ISessionFileSaver;
import com.comverse.timesheet.web.server.codec.SessionMessageCodecFactory;
import com.comverse.timesheet.web.server.message.SessionSegmentMessage;
import com.comverse.timesheet.web.server.util.BinaryHelper;

public class SessionServer  extends IoHandlerAdapter implements IServer {
	private static Logger log = Logger.getLogger(SessionServer.class);
	private JdbcTemplate jdbcTemplate=null;
	private ISessionCache sessionCache=null;
	private ISessionFileSaver sessionFileSaver=null;
	private int port=Constants.SESSION_LOG_SERVER_DEFAULT_PORT;
	
	public void setPort(int port) {
		this.port = port;
	}
	
	private Map<String,Long> collectorMap=new HashMap<String,Long>();
	private static final long REFRESH_PERIOD=1000L*60L;
	private long last=0;
	public void refreshColletorMap(){
		long current=System.currentTimeMillis();
		if(Math.abs(current-last)>REFRESH_PERIOD){
			try {
				jdbcTemplate.query("SELECT * FROM CCOLLECTOR",new RowMapper<Object>(){
					public Object mapRow(ResultSet rs, int value)
							throws SQLException {
						long collectorId=rs.getLong("IID");
						String collectorIp=rs.getString("IIP");
						collectorMap.put(collectorIp, collectorId);
						return collectorIp;
					}
				});						
			} catch (Exception e) {
				log.error(e, e);
			}
			last=current;	
		}
	}
	public ISessionCache getSessionCache() {
		return sessionCache;
	}
	public void setSessionCache(ISessionCache sessionCache) {
		this.sessionCache = sessionCache;
	}
	public void setDataSource(DataSource dataSource) {
		log.debug("dataSource = " + dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}	

	public void setSessionFileSaver(ISessionFileSaver sessionFileSaver) {
		this.sessionFileSaver = sessionFileSaver;
	}

	private NioSocketAcceptor acceptor = null;

	public void start() {
		try {
			acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors()>2?Runtime.getRuntime().availableProcessors()/2:1);
			//acceptor.getFilterChain().addLast("logger", new LoggingFilter());
			acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter(new SessionMessageCodecFactory()));//指定编码过滤器 
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
	
	private long lastTime=System.currentTimeMillis();
	private long lastBytes=0;
	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		if(message instanceof SessionSegmentMessage){
			SessionSegmentMessage msg=(SessionSegmentMessage)message;
			String collectorIp=(String)session.getAttribute("collectorIp");
			if(collectorIp==null){
				collectorIp=((InetSocketAddress)session.getRemoteAddress()).getHostString();
				session.setAttribute("collectorIp",collectorIp);
			}

			if(msg.getType()==0x0FE){
				if(msg.getSessionId()==Constants.MAGIC_NUMBER){
					log.debug("PREAMBLE...");
					return;	
				}else{
					log.debug("Error SessionSegmentMessage! msg = "+msg);
					return;
				}
			}else if(msg.getType()==0x0FD){
				log.debug("client ipaddress from proxy!");
				if(msg.getProtocolId()==0x04){
					long tmpIp=(0xFFFFFFFF00000000L&msg.getSessionId())>>32;					
					collectorIp=BinaryHelper.intToIpString((int)tmpIp);
					session.setAttribute("collectorIp",collectorIp);
					log.debug("collectorIp = "+collectorIp);
					return;
				}else if(msg.getProtocolId()==0x06){
					log.debug("Not support! msg = "+msg);
					return;						
				}
			}
			if(msg.getSessionId()==Constants.MAGIC_NUMBER){
				log.warn("May be error! SessionId = "+msg.getSessionId());
			}
			

			Long collectorId=0L;
			if(jdbcTemplate!=null){
				refreshColletorMap();
				collectorId=collectorMap.get(collectorIp);
			}
			msg.setCollectorId(collectorId==null?0:collectorId);
			msg.setCollectorIp(collectorIp);
			msg.setRecvTime(System.currentTimeMillis());			
			
			if(sessionCache!=null){
				sessionCache.addSessionSegmentMessage(msg);
			}
			if(sessionFileSaver!=null){
				sessionFileSaver.add(msg);
			}
		}
		
		long curTime = System.currentTimeMillis();
		if (curTime - lastTime > 10L * 1000L) {
			synchronized (this) {
				log.info("state: " + sessions.size() + " sessions");
				long totalBytes = 0;
				for (IoSession tmp : sessions) {
					totalBytes += tmp.getReadBytes();
				}
				long curBytes = totalBytes - lastBytes;
				log.info("state: " + (curBytes * 8 / (curTime - lastTime)) + " Kbps");
				log.info("state: " + (curBytes * 8 / (curTime - lastTime) / 1000L) + " Mbps");
				lastTime = System.currentTimeMillis();
				lastBytes = totalBytes;
			}
		}
	}

	public static void main(String[] args) throws IOException {
//		PacketServer ps = new PacketServer();
//		ps.start();

	}

}