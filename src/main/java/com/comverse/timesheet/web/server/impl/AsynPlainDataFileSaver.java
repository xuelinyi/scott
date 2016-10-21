package com.comverse.timesheet.web.server.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteOrder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sql.DataSource;


import org.apache.commons.codec.binary.Hex;
import org.apache.log4j.Logger;
import org.apache.mina.core.buffer.IoBuffer;
import org.springframework.jdbc.core.JdbcTemplate;

import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.IPlainDataFileSaver;
import com.comverse.timesheet.web.server.IServer;
import com.comverse.timesheet.web.server.message.PlainDataSegmentMessage;
import com.comverse.timesheet.web.server.util.BinaryHelper;
import com.comverse.timesheet.web.server.util.FilenameHelper;

public class AsynPlainDataFileSaver implements IPlainDataFileSaver, Runnable, IServer {
	private static Logger log = Logger.getLogger(AsynPlainDataFileSaver.class);

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
	
	public void timeout(){
		log.debug("timeout...");
		long current=System.currentTimeMillis();
		Set<PlainDataFile> timeOutSet=new HashSet<PlainDataFile>();
		for(PlainDataFile file:map.values()){
			if(file!=null && current>file.getLast()+Constants.SESSION_LOG_TIMEOUT){
				log.debug("session = "+file.getSessionId()+" timeout, "+file.getFilename()+" will be closed.");
				timeOutSet.add(file);
			}
		}
		
		for(PlainDataFile file:timeOutSet){
			file.close();
			addRecord(file.getSessionId(),file.getAddress(),file.getFilename(),file.getPath(),file.getCreateTime());
			map.remove(file.getSessionId());			
		}
	}

	private ConcurrentHashMap<Long, PlainDataFile> map = new ConcurrentHashMap<Long, PlainDataFile>();

	private void appendFile(PlainDataSegmentMessage message) {
		if (message == null) {
			return;
		}		
		log.debug("sessionId = " + message.getSessionId() + " message = " + message.getSegmentData().length);
		PlainDataFile file = map.get(message.getSessionId());
		log.debug("file = "+file);
		if (file == null) {
			file = new PlainDataFile(dataDir, message.getSessionId(),message.getAddress());
			map.put(message.getSessionId(), file);
		}
		file.write(message);
		if (message.getSegmentData().length==0) {
			log.debug("\r\nend of session "+file.getSessionId());
			file.close();
			addRecord(file.getSessionId(),file.getAddress(),file.getFilename(),file.getPath(),file.getCreateTime());
			map.remove(message.getSessionId());
		}
	}

	private void addRecord(long sessionId,String collectorIp,String filename,String path,long createTime) {
		log.debug("\r\naddRecord sessionId = "+sessionId+" filename = "+filename);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT INTO PLAIN_DATA_FILE(");
			sb.append("SSESSION_ID,CCOLLECTOR_IP,FFILENAME,PPATH,CCREATE_TIME) VALUES(");
			sb.append(sessionId);
			sb.append(",'");
			sb.append(collectorIp);
			sb.append("','");
			sb.append(filename);
			sb.append("','");
			sb.append(path);
			sb.append("','");
			sb.append(sdf.format(new Date(createTime)));
			sb.append("')");
			String sql = sb.toString();
			log.debug("sql = " + sql);
			if (jdbcTemplate != null && sql.length() > 0) {
				jdbcTemplate.execute(sql);
			}
	}
	

	private Queue<PlainDataSegmentMessage> queue = new ConcurrentLinkedQueue<PlainDataSegmentMessage>();

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

	public void add(PlainDataSegmentMessage message) {
		if(stop){
			log.debug("Server is stoped. drop "+message);
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
					PlainDataSegmentMessage message = queue.poll();
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
				PlainDataSegmentMessage message = queue.poll();
				if (message != null) {
					appendFile(message);
				}
			} catch (Exception e) {
				log.error(e, e);
			}
		}

		for (long sessionId : map.keySet()) {
			PlainDataFile file = map.get(sessionId);
			log.debug("handle the remaining sessionId = " + sessionId + " file = " + file);
			file.close();
			addRecord(file.getSessionId(),file.getAddress(),file.getFilename(),file.getPath(),file.getCreateTime());
		}
		map.clear();

		log.debug("end of Thread");
	}

}

class PlainDataFile {
	private static Logger log = Logger.getLogger(TempFile.class);
	private String dataDir;
	private long sessionId;	
	private String address;
	private String filename;
	private String path;
	private long createTime;
	private long last;
	private IoBuffer buffer;
	private Map<String,Integer>seqMap=new HashMap<String,Integer>();
	
	private BufferedOutputStream output;


	public PlainDataFile(String dataDir,long sessionId,String address) {
		super();
		this.dataDir=dataDir;
		this.sessionId=sessionId;
		this.address=address;
		buffer=IoBuffer.allocate(2048).setAutoExpand(true);
	}

	private static final byte[] pcapHeader = { (byte) 0xD4, (byte) 0xC3, (byte) 0xB2, (byte) 0xA1, (byte) 0x02,
			(byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x01,
			(byte) 0x00, (byte) 0x00, (byte) 0x00 };

	private void open() {
		if (output == null) {
			this.createTime=System.currentTimeMillis();
			path=FilenameHelper.createPlainDataFilename(sessionId, createTime);
			File dir = new File(dataDir,path);
			if (!dir.getParentFile().exists()) {
				dir.getParentFile().mkdirs();
			}
			File file = new File(dataDir,path);
			filename=file.getName();
			if (file.exists()) {
				try {
					output = new BufferedOutputStream(new FileOutputStream(file, true));					
				} catch (FileNotFoundException e) {
					log.error(e, e);
				}
			} else {
				try {
					output = new BufferedOutputStream(new FileOutputStream(file));
					output.write(pcapHeader);
				} catch (Exception e) {
					log.error(e, e);
				}
			}
			
		}
	}

	public void write(PlainDataSegmentMessage message) {
		log.debug("sessionId = "+message.getSessionId()+" segmentLen = "+message.getSegmentLen()+" data.length = "+message.getSegmentData().length);
		log.debug("output = "+output);
		last=System.currentTimeMillis();
		if (output == null) {
			open();
		}		
		if (output != null) {
			buffer.put(message.getSegmentData());
			buffer.flip();
			buffer.order(ByteOrder.BIG_ENDIAN);
			buffer.mark();// 标记当前位置，以便reset
			if (buffer.remaining() < 4) {// 如果消息内容不够，则重置，相当于不读取size
				buffer.reset();
				buffer.compact();			
				return;
			}
			int dataLen=0;
			int protocolId;
			long time;
			byte[] srcMac=new byte[6];
			byte[] dstMac=new byte[6];
			byte[] srcIp=new byte[4];
			int srcPort;
			byte[] dstIp=new byte[4];
			int dstPort;
			
			dataLen = buffer.getInt();
			log.debug("dataLen = "+dataLen);
			if(dataLen<0){
				log.debug("明文数据长度小于0, remaining = "+buffer.remaining()+" dataLen = "+dataLen);
				buffer.clear();
				return;
			}else if(dataLen>Constants.MAX_PLAIN_DATA_SIZE){
				log.debug("明文数据长度过长 , remaining = "+buffer.remaining()+" dataLen = "+dataLen);
				buffer.clear();
				return;
			}
			
			if (buffer.remaining() < dataLen+33) {// 如果消息内容不够，则重置，相当于不读取size
				log.debug("明文数据内容不够 remaining = "+buffer.remaining()+" dataLen = "+dataLen);
				buffer.reset();
				buffer.compact();
				return;// 接收新数据，以拼凑成完整数据
			}
			
			protocolId=buffer.get();
			time=buffer.getLong();
			buffer.get(srcMac);
			buffer.get(dstMac);
			buffer.get(srcIp);
			srcPort=buffer.getUnsignedShort();
			buffer.get(dstIp);
			dstPort=buffer.getUnsignedShort();
			byte[]data=new byte[dataLen];
			buffer.get(data);
			buffer.compact();
			log.debug("srcMac = "+Hex.encodeHexString(srcMac));
			log.debug("dstMac = "+Hex.encodeHexString(dstMac));
			log.debug("srcIp = "+Hex.encodeHexString(srcIp));
			log.debug("dstIp = "+Hex.encodeHexString(dstIp));
			log.debug("srcPort = "+srcPort);
			log.debug("dstPort = "+dstPort);
			
			
			byte[] packetHeader = new byte[16];
			// Timestamp：时间戳高位，精确到seconds
			// Timestamp：时间戳低位，精确到microseconds
			// Caplen：当前数据区的长度，即抓取到的数据帧长度，由此可以得到下一个数据帧的位置。
			// Len：离线数据长度：网络中实际数据帧的长度，一般不大于caplen，多数情况下和Caplen数值相等。
			long seconds = time / 1000;
			long microseconds = time % 1000;
			int len = dataLen+54;
			IoBuffer pcap=IoBuffer.allocate(54).setAutoExpand(true);
			pcap.order(ByteOrder.LITTLE_ENDIAN);
			pcap.putInt((int)seconds);
			pcap.putInt((int)microseconds);
			pcap.putInt(len);//caplen
			pcap.putInt(len);
			//mac header
			pcap.put(dstMac);
			pcap.put(srcMac);
			pcap.put(new byte[]{0x08,0x00});
			//ip header
			pcap.put(new byte[]{0x45,0x00});
			pcap.order(ByteOrder.BIG_ENDIAN);
			pcap.putUnsignedShort(dataLen+40);
			pcap.putUnsignedShort(sessionId);
			pcap.put(new byte[]{0x40,0x00,0x40,0x06,0x00,0x00});//flags,fragment offset, ttl,protocol,check sum
			pcap.put(srcIp);
			pcap.put(dstIp);
			//tcp header
			pcap.putUnsignedShort(srcPort);
			pcap.putUnsignedShort(dstPort);
			String srcIpString=BinaryHelper.bytesToIpString(srcIp);
			int seq=0;
			if(seqMap.get(srcIpString)!=null){
				seq=seqMap.get(srcIpString);
			}
			pcap.putInt(seq);//seq
			seqMap.put(srcIpString, seq+dataLen);			
			String dstIpString=BinaryHelper.bytesToIpString(dstIp);
			int ack=0;
			if(seqMap.get(dstIpString)!=null){
				ack=seqMap.get(dstIpString);
			}
			pcap.putInt(ack);//ack
			pcap.put(new byte[]{0x50,0x18,0x00,(byte)0xe5,0x00,0x00,0x00,0x00});//header len,flags,window size,checksum,emergence point
			pcap.flip();
			byte[] headers = new byte[pcap.limit()];
			pcap.get(headers);
			
			try {
				output.write(headers);
				output.write(data);
			} catch (IOException e) {
				log.error(e, e);
			}
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public long getCreateTime() {
		return createTime;
	}

	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}

	public BufferedOutputStream getOutput() {
		return output;
	}

	public void setOutput(BufferedOutputStream output) {
		this.output = output;
	}

	@Override
	public String toString() {
		return "PlainDataFile [sessionId=" + sessionId + ", path=" + path + ", createTime=" + createTime + "]";
	}



}