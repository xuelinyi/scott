package com.comverse.timesheet.web.server.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.comverse.timesheet.web.server.IServer;
import com.comverse.timesheet.web.server.ISqlExecuter;


public class AsynSQLExecuterImpl implements ISqlExecuter,IServer {
	private static Logger log = Logger.getLogger(AsynSQLExecuterImpl.class);
	
	private int threadNumber=1;
	
	public int getThreadNumber() {
		return threadNumber;
	}

	public void setThreadNumber(int threadNumber) {
		this.threadNumber = threadNumber;
	}

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		log.debug("dataSource = "+dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}


	private List<AsynSQLWorkerThread> workers = new ArrayList<AsynSQLWorkerThread>();

	public void start() {
		log.info("start ... ");
		log.info("threadNumber = "+threadNumber);
		for(int i =0;i<threadNumber;i++){
			workers.add(new AsynSQLWorkerThread(jdbcTemplate));	
		}
	}

	public void stop() {
		for(AsynSQLWorkerThread worker:workers){
			worker.stop();
		}		
		log.info("stoped!!!");
	}


	public void addSql(long sessionId,String sql) {
		System.out.println("===========================================>>sql:"+sql);
		AsynSQLWorkerThread worker=workers.get((int)((sessionId%threadNumber+threadNumber)%threadNumber));
		worker.addSql(sql);
	}

	

}
class AsynSQLWorkerThread implements Runnable{
	private static Logger log = Logger.getLogger(AsynSQLWorkerThread.class);
	private Queue<String> sqlQueue = new ConcurrentLinkedQueue<String>();

	private int MAX_QUEUE_SIZE = 10000;
	private int MAX_BATCH_SIZE = 1024;

	private long maxQueueSize = 0;
	private long counter = 0;
	private Thread thread = null;
	private boolean stop = false;
	
	private JdbcTemplate jdbcTemplate;
	public AsynSQLWorkerThread(JdbcTemplate jdbcTemplate){
		this.jdbcTemplate=jdbcTemplate;
		thread = new Thread(this);
		thread.start();
	}
	public void stop() {
		log.info("sqlQueue.size= " + sqlQueue.size()+"@"+this);
		stop = true;
		try {
			thread.join();
		} catch (Exception e) {
			log.error(e, e);
		}		
		log.info("stoped!!!"+"@"+this);
	}
	
	private static final long PERIOD=60L*1000L;
	private long lastTime=0;
	private long lastOffer=0;
	private long lastPoll=0;
	public void addSql(String sql) {
		if(System.currentTimeMillis()-lastTime>PERIOD){
			double offerRate=1000.0D*lastOffer/(System.currentTimeMillis()-lastTime);
			double pollRate=1000.0D*lastPoll/(System.currentTimeMillis()-lastTime);
			log.info("offerRate = " + offerRate+" sqlQueueSize = " + sqlQueue.size()+"@"+this);
			log.info("pollRate = " + pollRate+" sqlQueueSize = " + sqlQueue.size()+"@"+this);			
			lastOffer=0;
			lastPoll=0;
			lastTime=System.currentTimeMillis();
		}
		lastOffer++;
		
		
		if (maxQueueSize < sqlQueue.size()) {
			maxQueueSize = sqlQueue.size();
		}
		if ((counter++) % MAX_QUEUE_SIZE == 0) {
			log.info("maxQueueSize = " + maxQueueSize+"@"+this);
		}

		if (sqlQueue.size() > MAX_QUEUE_SIZE) {
			log.warn("exceed MAX_QUEUE_SIZE! "+", sqlQueueSize = "+sqlQueue.size()+"@"+this);
		}
		sqlQueue.offer(sql);
	}

	public void run() {
		log.debug("start of Thread"+"@"+this);
		List<String>sqlList=new ArrayList<String>();
		
		while (!stop) {		
			while (!sqlQueue.isEmpty()) {
				if(sqlList.size()<MAX_BATCH_SIZE){
					lastPoll++;
					sqlList.add(sqlQueue.poll());
					
				}else{				
					try {
						String[] array =new String[sqlList.size()];
						sqlList.toArray(array);
						jdbcTemplate.batchUpdate(array);
						sqlList.clear();
					} catch (Exception e) {
						log.error(e, e);
					}					
				}
			}
			if(sqlList.size()>0){
				try {
					String[] array =new String[sqlList.size()];
					sqlList.toArray(array);
					jdbcTemplate.batchUpdate(array);
					sqlList.clear();
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
				lastPoll++;
				jdbcTemplate.execute(sqlQueue.poll());
			} catch (Exception e) {
				log.error(e, e);
			}
		}

		log.debug("end of Thread"+"@"+this);
	}
}
