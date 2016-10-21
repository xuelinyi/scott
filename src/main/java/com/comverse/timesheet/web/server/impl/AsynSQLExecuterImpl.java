package com.comverse.timesheet.web.server.impl;

import java.net.InetSocketAddress;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.springframework.jdbc.core.JdbcTemplate;

import com.comverse.timesheet.web.server.IServer;
import com.comverse.timesheet.web.server.codec.SessionMessageCodecFactory;


public class AsynSQLExecuterImpl implements Runnable,IServer {
	private static Logger log = Logger.getLogger(AsynSQLExecuterImpl.class);
	
	private JdbcTemplate jdbcTemplate;

	private Queue<String> sqlQueue = new ConcurrentLinkedQueue<String>();

	private int MAX_QUEUE_SIZE = 10000;

	public void setDataSource(DataSource dataSource) {
		log.debug("dataSource = "+dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private boolean stop = false;
	private Thread thread = null;
	private NioSocketAcceptor acceptor = null;
	public void start() {
		acceptor = new NioSocketAcceptor(Runtime.getRuntime().availableProcessors()>2?Runtime.getRuntime().availableProcessors()/2:1);
		//acceptor.getFilterChain().addLast("logger", new LoggingFilter());
		acceptor.getFilterChain().addLast( "codec", new ProtocolCodecFilter(new SessionMessageCodecFactory()));//指定编码过滤器 
		acceptor.setDefaultLocalAddress( new InetSocketAddress(10000+9020));//设置端口号
		acceptor.setReuseAddress(true);
		acceptor.getSessionConfig().setReadBufferSize(2*1024*1024);
		//acceptor.bind();
		log.info("start ... ");
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		log.info("sqlQueue.size= " + sqlQueue.size());
		stop = true;
		try {
			thread.join();
		} catch (Exception e) {
			log.error(e, e);
		}
		log.info("stoped!!!");
	}

	private long maxQueueSize = 0;
	private long counter = 0;

	public void addSql(String sql) {
		if (maxQueueSize < sqlQueue.size()) {
			maxQueueSize = sqlQueue.size();
		}
		if ((counter++) % 10000 == 0) {
			log.info("maxQueueSize = " + maxQueueSize);
		}

		if (sqlQueue.size() > MAX_QUEUE_SIZE) {
			log.warn("exceed MAX_QUEUE_SIZE!");
		}
		sqlQueue.offer(sql);
	}

	public void run() {
		log.debug("start of Thread");
		while (!stop) {
			while (!sqlQueue.isEmpty()) {
				try {
					jdbcTemplate.execute(sqlQueue.poll());
				} catch (Exception e) {
					log.error(e, e);
				}
			}

			try {
				Thread.sleep(50L);
			} catch (InterruptedException e) {
			}

		}

		while (!sqlQueue.isEmpty()) {
			try {
				jdbcTemplate.execute(sqlQueue.poll());
			} catch (Exception e) {
				log.error(e, e);
			}
		}

		log.debug("end of Thread");
	}

}