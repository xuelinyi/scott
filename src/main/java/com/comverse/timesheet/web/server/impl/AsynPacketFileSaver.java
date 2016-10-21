package com.comverse.timesheet.web.server.impl;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import javax.sql.DataSource;


import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;

import com.comverse.timesheet.web.server.Constants;
import com.comverse.timesheet.web.server.IPacketFileSaver;
import com.comverse.timesheet.web.server.IServer;
import com.comverse.timesheet.web.server.message.PacketMessage;

public class AsynPacketFileSaver implements IPacketFileSaver, Runnable, IServer {
	private static Logger log = Logger.getLogger(AsynPacketFileSaver.class);

	private JdbcTemplate jdbcTemplate;

	private String dataDir = "/linux/IXApp/ftpd/pcap";

	private void refreshPacketDataDir() {
		String value = jdbcTemplate.queryForObject("SELECT VVALUE FROM SYSCONFIG WHERE IID=?",
				new Object[] { Constants.PACKET_DATA_DIR_IID }, java.lang.String.class);
		if (value != null) {
			if (!dataDir.equals(value)) {
				log.info("packetDataDir changed from " + dataDir + " to " + value);
				if (value.lastIndexOf("/") >= 0) {
					value = value.substring(0, value.length() - 1);
				}
				dataDir = value;
			}
		}
	}

	private ConcurrentHashMap<String, TempFile> tempFileMap = new ConcurrentHashMap<String, TempFile>();

	private void appendFile(String address, PacketMessage packetMessage) {		
		if (address == null || packetMessage == null) {
			return;
		}
		log.debug("address = " + address + " packetMessage = " + packetMessage.getCaplen());
		
		TempFile tempFile = tempFileMap.get(address);
		if (tempFile == null) {
			tempFile = new TempFile(dataDir, address);
			tempFileMap.put(address, tempFile);
		}
		tempFile.write(packetMessage);
		if (tempFile.getLength() > Constants.PACKET_FILE_MAX_SIZE) {
			rotate(address, tempFile);
		}
	}

	private void rotate(String address, TempFile tempFile) {
		log.debug("\r\nrotate address = "+address+" tempFile = "+tempFile);
		String filename = tempFile.rename();
		if (filename != null) {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
			StringBuffer sb = new StringBuffer();
			sb.append("INSERT INTO PACKET_FILE(");
			sb.append("CCOLLECTOR_IP,FFILENAME,PPATH,CCREATE_TIME) VALUES('");
			sb.append(address);
			sb.append("','");
			sb.append(filename);
			sb.append("','");
			sb.append("/" + filename);
			sb.append("','");
			sb.append(sdf.format(new Date()));
			sb.append("')");
			String sql = sb.toString();
			log.debug("sql = " + sql);
			if (jdbcTemplate != null && sql.length() > 0) {
				jdbcTemplate.execute(sql);
			}
		}

	}

	private Queue<PacketMessage> queue = new ConcurrentLinkedQueue<PacketMessage>();

	private int MAX_QUEUE_SIZE = 10000;

	public void setDataSource(DataSource dataSource) {
		log.debug("dataSource = " + dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	private boolean stop = false;
	private Thread thread = null;

	public void start() {
		log.info("start ... ");

		refreshPacketDataDir();
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

	public void add(PacketMessage packetMessage) {
		if(stop){
			log.debug("Server is stoped. drop "+packetMessage);
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
		queue.offer(packetMessage);
	}

	
	public void run() {
		log.debug("start of Thread");
		while (!stop) {
			while (!queue.isEmpty()) {
				try {
					PacketMessage packetMessage = queue.poll();
					if (packetMessage != null) {
						appendFile(packetMessage.getAddress(), packetMessage);
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
				PacketMessage message = queue.poll();
				if (message != null) {
					appendFile(message.getAddress(), message);
				}
			} catch (Exception e) {
				log.error(e, e);
			}
		}

		for (String address : tempFileMap.keySet()) {
			TempFile tempFile = tempFileMap.get(address);
			log.debug("handle the remaining address = " + address + " TempFile = " + tempFile);
			rotate(address, tempFile);
		}
		tempFileMap.clear();

		log.debug("end of Thread");
	}

}

class TempFile {
	private static Logger log = Logger.getLogger(TempFile.class);
	private String packetDataDir;
	private String address;
	private String filename;
	private BufferedOutputStream output;
	private long length;

	public TempFile(String packetDataDir, String address) {
		super();
		this.packetDataDir = packetDataDir;
		this.address = address;
		this.filename = packetDataDir + "/" + address + "_temp";
		File dir = new File(packetDataDir);
		if (!dir.exists()) {
			dir.mkdirs();
		}
	}

	private static final byte[] pcapHeader = { (byte) 0xD4, (byte) 0xC3, (byte) 0xB2, (byte) 0xA1, (byte) 0x02,
			(byte) 0x00, (byte) 0x04, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
			(byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0xFF, (byte) 0xFF, (byte) 0x00, (byte) 0x00, (byte) 0x01,
			(byte) 0x00, (byte) 0x00, (byte) 0x00 };

	private void open() {
		if (output == null) {
			File tmpFile = new File(filename);
			if (tmpFile.exists()) {
				length = tmpFile.length();
				try {
					output = new BufferedOutputStream(new FileOutputStream(tmpFile, true));
				} catch (FileNotFoundException e) {
					log.error(e, e);
				}
			} else {
				length = 0;
				try {
					output = new BufferedOutputStream(new FileOutputStream(tmpFile));

					output.write(pcapHeader);
				} catch (Exception e) {
					log.error(e, e);
				}

			}
		}
	}

	public void write(PacketMessage packetMessage) {
		if (output == null) {
			open();
		}
		if (output != null) {
			byte[] packetHeader = new byte[16];
			// Timestamp：时间戳高位，精确到seconds
			// Timestamp：时间戳低位，精确到microseconds
			// Caplen：当前数据区的长度，即抓取到的数据帧长度，由此可以得到下一个数据帧的位置。
			// Len：离线数据长度：网络中实际数据帧的长度，一般不大于caplen，多数情况下和Caplen数值相等。
			long time = packetMessage.getTime();
			long seconds = time / 1000000L;
			long microseconds = time % 1000000L;
			int caplen = packetMessage.getCaplen();
			int len = packetMessage.getLen();
			packetHeader[0] = (byte) ((seconds >> 0) & 0xff);
			packetHeader[1] = (byte) ((seconds >> 8) & 0xff);
			packetHeader[2] = (byte) ((seconds >> 16) & 0xff);
			packetHeader[3] = (byte) ((seconds >> 24) & 0xff);
			packetHeader[4] = (byte) ((microseconds >> 0) & 0xff);
			packetHeader[5] = (byte) ((microseconds >> 8) & 0xff);
			packetHeader[6] = (byte) ((microseconds >> 16) & 0xff);
			packetHeader[7] = (byte) ((microseconds >> 24) & 0xff);
			packetHeader[8] = (byte) ((caplen >> 0) & 0xff);
			packetHeader[9] = (byte) ((caplen >> 8) & 0xff);
			packetHeader[10] = (byte) ((caplen >> 16) & 0xff);
			packetHeader[11] = (byte) ((caplen >> 24) & 0xff);
			packetHeader[12] = (byte) ((len >> 0) & 0xff);
			packetHeader[13] = (byte) ((len >> 8) & 0xff);
			packetHeader[14] = (byte) ((len >> 16) & 0xff);
			packetHeader[15] = (byte) ((len >> 24) & 0xff);
			try {
				output.write(packetHeader);
				length += packetHeader.length;
				output.write(packetMessage.getPacket());
				length += packetMessage.getPacket().length;

			} catch (IOException e) {
				log.error(e, e);
			}
		}

	}

	public String rename() {
		if (output == null) {
			return null;
		}

		try {

			output.flush();
			output.close();
			length = 0;
			output = null;
			File tmpFile = new File(filename);

			if (tmpFile.exists()) {
				SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
				File dstFile = new File(packetDataDir + "/" + address + "_" + sdf.format(new Date()) + ".pcap");
				log.debug("new file " + dstFile);
				tmpFile.renameTo(dstFile);
				return dstFile.getName();
			}
		} catch (IOException e) {
			log.error(e, e);
		}

		return null;

	}

	public String getPacketDataDir() {
		return packetDataDir;
	}

	public void setPacketDataDir(String packetDataDir) {
		this.packetDataDir = packetDataDir;
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

	public BufferedOutputStream getOutput() {
		return output;
	}

	public void setOutput(BufferedOutputStream output) {
		this.output = output;
	}

	public long getLength() {
		return length;
	}

	public void setLength(long length) {
		this.length = length;
	}

	@Override
	public String toString() {
		return "TempFile [packetDataDir=" + packetDataDir + ", address=" + address + ", filename=" + filename
				+ ", output=" + output + ", length=" + length + "]";
	}

}