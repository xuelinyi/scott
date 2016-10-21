package com.comverse.timesheet.web.server.impl;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sql.DataSource;


import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.comverse.timesheet.web.server.IServer;
import com.comverse.timesheet.web.server.ISqlExecuter;


public class AsynSQLExecuterImpl implements ISqlExecuter, Runnable,IServer {
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

	public void start() {
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