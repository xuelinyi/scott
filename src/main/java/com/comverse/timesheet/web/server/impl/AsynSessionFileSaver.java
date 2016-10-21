package com.comverse.timesheet.web.server.impl;

import java.io.BufferedOutputStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sql.DataSource;


import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.springframework.jdbc.core.JdbcTemplate;

import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.IServer;
import com.comverse.timesheet.web.server.ISessionFileSaver;
import com.comverse.timesheet.web.server.message.SessionSegmentMessage;
import com.comverse.timesheet.web.server.message.TLVMessage;
import com.comverse.timesheet.web.server.util.FilenameHelper;

public class AsynSessionFileSaver implements ISessionFileSaver, Runnable, IServer {
	private static Logger log = Logger.getLogger(AsynSessionFileSaver.class);

	private JdbcTemplate jdbcTemplate;

	private String dataDir = "/linux/IXApp/ftpd/session";

	private void refreshDataDir() {
		String value = jdbcTemplate.queryForObject("SELECT VVALUE FROM SYSCONFIG WHERE IID=?",
				new Object[] { Constants.PLAIN_DATA_DIR_IID }, java.lang.String.class);
		if (value != null) {
			if (!dataDir.equals(value)) {
				log.info("dataDir changed from " + dataDir + " to " + value);
				if (value.lastIndexOf("/") >= 0) {
					value = value.substring(0, value.length() - 1);
				}
				dataDir = value;
			}
		}
	}

	public void timeout() {
		long current = System.currentTimeMillis();
		Set<SessionFile> timeOutSet = new HashSet<SessionFile>();
		for (SessionFile file : map.values()) {
			if (file != null && current > file.getLast() + Constants.SESSION_LOG_TIMEOUT) {
				timeOutSet.add(file);
			}
		}

		for (SessionFile file : timeOutSet) {
			file.close();
			map.remove(file.getSessionId());
		}
	}

	private ConcurrentHashMap<Long, SessionFile> map = new ConcurrentHashMap<Long, SessionFile>();

	private void appendFile(SessionSegmentMessage message) {
		if (message == null) {
			return;
		}
		log.debug("sessionId = " + message.getSessionId());
		SessionFile file = map.get(message.getSessionId());
		log.debug("file = " + file);
		if (file == null) {
			file = new SessionFile(dataDir, message.getSessionId(), message.getRecvTime());
			map.put(message.getSessionId(), file);
		}
		file.write(message);
		if (message.getTlvList().size() == 0) {
			log.debug("\r\nend of " + file.getSessionId());
			file.close();
			map.remove(message.getSessionId());
		}
	}

	private Queue<SessionSegmentMessage> queue = new ConcurrentLinkedQueue<SessionSegmentMessage>();

	private int MAX_QUEUE_SIZE = 10000;

	public void setDataSource(DataSource dataSource) {
		log.debug("dataSource = " + dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private boolean stop = false;
	private Thread thread = null;

	public void start() {
		log.info("start ... ");

		refreshDataDir();
		thread = new Thread(this);
		thread.start();
	}

	public void stop() {
		log.info("queue.size= " + queue.size());
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

	public void add(SessionSegmentMessage message) {
		if (stop) {
			log.debug("Server is stoped. drop " + message);
			return;
		}
		if (maxQueueSize < queue.size()) {
			maxQueueSize = queue.size();
		}
		if ((counter++) % 10000 == 0) {
			log.info("maxQueueSize = " + maxQueueSize);
		}

		if (queue.size() > MAX_QUEUE_SIZE) {
			log.warn("exceed MAX_QUEUE_SIZE!");
		}
		queue.offer(message);
	}

	public void run() {
		log.debug("start of Thread");
		while (!stop) {
			while (!queue.isEmpty()) {
				try {
					SessionSegmentMessage message = queue.poll();
					if (message != null) {
						appendFile(message);
					}
				} catch (Exception e) {
					log.error(e, e);
				}
			}

			try {
				Thread.sleep(50L);
			} catch (InterruptedException e) {
			}

		}

		log.debug("Server will stop...");
		while (!queue.isEmpty()) {
			try {
				SessionSegmentMessage message = queue.poll();
				if (message != null) {
					appendFile(message);
				}
			} catch (Exception e) {
				log.error(e, e);
			}
		}

		for (long sessionId : map.keySet()) {
			SessionFile file = map.get(sessionId);
			log.debug("handle the remaining sessionId = " + sessionId + " file = " + file);
			file.close();
		}
		map.clear();

		log.debug("end of Thread");
	}

}

class SessionFile {
	private static Logger log = Logger.getLogger(SessionFile.class);
	private String dataDir;
	private long sessionId;
	private long createTime;
	private long last;
	private IoBuffer buffer;
	private BufferedOutputStream output;

	public SessionFile(String dataDir, long sessionId, long createTime) {
		super();
		this.dataDir = dataDir;
		this.sessionId = sessionId;
		this.createTime = createTime;
		buffer = IoBuffer.allocate(2048).setAutoExpand(true);
		buffer.order(ByteOrder.BIG_ENDIAN);

	}

	private static final byte[] PREAMBLE = { (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE,
			(byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE,
			(byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0xFE, (byte) 0x00, (byte) 0x14 };

	private void open() {
		if (output == null) {
			String path = FilenameHelper.createSessionFilename(sessionId, createTime);
			File dir = new File(dataDir, path);
			if (!dir.getParentFile().exists()) {
				dir.getParentFile().mkdirs();
			}
			File file = new File(dataDir, path);
			if (file.exists()) {
				try {
					output = new BufferedOutputStream(new FileOutputStream(file, true));
				} catch (FileNotFoundException e) {
					log.error(e, e);
				}
			} else {
				try {
					output = new BufferedOutputStream(new FileOutputStream(file));
				} catch (Exception e) {
					log.error(e, e);
				}
			}
		}
	}

	public void write(SessionSegmentMessage msg) {
		log.debug("output = " + output);
		last = System.currentTimeMillis();
		if (output == null) {
			open();
		}
		try {
			if (output != null) {
				buffer.clear();
				buffer.put(PREAMBLE);
				buffer.putLong(msg.getSessionId());
				buffer.putLong(msg.getApplicationId());
				buffer.put((byte) msg.getProtocolId());
				buffer.put((byte) msg.getType());
				buffer.putUnsignedShort(0);
				for (int i = 0; i < msg.getTlvList().size(); i++) {
					TLVMessage tlv = msg.getTlvList().get(i);
					buffer.putUnsignedShort(tlv.getTag());
					buffer.putUnsignedShort(tlv.getLen());
					buffer.put(tlv.getValue());
				}
				buffer.putUnsignedShort(20+18, buffer.position()-20);// modify length
				buffer.flip();
				byte[] b = new byte[buffer.limit()];
				buffer.get(b);

				output.write(b);

			}
		} catch (IOException e) {
			log.error(e, e);
		}
	}

	public void close() {
		if (output == null) {
			return;
		}

		try {

			output.flush();
			output.close();
			output = null;
		} catch (IOException e) {
			log.error(e, e);
		}
	}

	public long getLast() {
		return last;
	}

	public void setLast(long last) {
		this.last = last;
	}

	public long getSessionId() {
		return sessionId;
	}

	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}

	public BufferedOutputStream getOutput() {
		return output;
	}

	public void setOutput(BufferedOutputStream output) {
		this.output = output;
	}

	@Override
	public String toString() {
		return "SessionFile [dataDir=" + dataDir + ", sessionId=" + sessionId + ", last=" + last + ", output=" + output
				+ "]";
	}

}