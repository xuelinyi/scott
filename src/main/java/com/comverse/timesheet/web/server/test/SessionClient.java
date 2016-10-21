package com.comverse.timesheet.web.server.test;

import java.net.InetSocketAddress;
import java.nio.ByteOrder;


import org.apache.mina.core.buffer.IoBuffer;
import org.apache.mina.core.filterchain.DefaultIoFilterChainBuilder;
import org.apache.mina.core.future.ConnectFuture;
import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.comverse.timesheet.web.server.codec.SessionMessageCodecFactory;
import com.comverse.timesheet.web.server.message.SessionSegmentMessage;
import com.comverse.timesheet.web.server.message.TLVMessage;
import com.comverse.timesheet.web.server.util.BinaryHelper;

public class SessionClient extends IoHandlerAdapter implements Runnable {
	private IoSession session = null;

	public void sessionOpened(IoSession session) throws Exception {
		this.session = session;
		new Thread(this).start();
	}

	private static final char[] WORDS = { '我', '你', '他' };

	private static String getRandomString(int len) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < len; i++) {
			sb.append(WORDS[(int) (3 * Math.random())]);
		}
		return sb.toString();
	}




	private void sendSSHSession() throws Exception {
		long sessionId = System.currentTimeMillis();
		long applicationId = System.currentTimeMillis() / 2;
		int protocolId = 0x06;
		{
			SessionSegmentMessage msg = new SessionSegmentMessage();
			msg.setSessionId(sessionId);
			msg.setApplicationId(applicationId);
			msg.setProtocolId(protocolId);
			msg.setType(0x01);
			byte[] flow = new byte[(int) (1 + Math.random() * 64)];
			for (int i = 0; i < flow.length; i++) {
				flow[i] = (byte) (1000 * Math.random());
			}
			msg.getTlvList().add(new TLVMessage(0x01, flow));
			msg.getTlvList().add(
					new TLVMessage(0x02, BinaryHelper.ipv4ToBinary(BinaryHelper.intToIpString((int) (System
							.currentTimeMillis())))));
			msg.getTlvList().add(new TLVMessage(0x03, (int) (65536 * Math.random())));
			msg.getTlvList().add(
					new TLVMessage(0x04, BinaryHelper.ipv4ToBinary(BinaryHelper.intToIpString((int) (System
							.currentTimeMillis())))));
			msg.getTlvList().add(new TLVMessage(0x05, (int) (65536 * Math.random())));
			msg.getTlvList().add(new TLVMessage(0x06, getRandomString(64)));
			msg.getTlvList().add(new TLVMessage(0x07, getRandomString(64)));
			msg.getTlvList().add(new TLVMessage(0x08, getRandomString(64)));
			msg.getTlvList().add(new TLVMessage(0x09, getRandomString(64)));
			msg.getTlvList().add(new TLVMessage(0x0a, System.currentTimeMillis()));
			msg.getTlvList().add(new TLVMessage(0x0b, getRandomString(64)));
			msg.getTlvList().add(new TLVMessage(0x0c, System.currentTimeMillis()));
			msg.getTlvList().add(new TLVMessage(0x0d, (int) (System.currentTimeMillis() % 10)));
			msg.getTlvList().add(new TLVMessage(0x0e, getRandomString(64)));
			msg.getTlvList().add(new TLVMessage(0x0f, (long) (1024 * Math.random())));
			msg.getTlvList().add(new TLVMessage(0x10, (long) (1024 * 1024 * Math.random())));
			byte[] srcMac = new byte[6];
			for (int i = 0; i < srcMac.length; i++) {
				srcMac[i] = (byte) (1000 * Math.random());
			}
			msg.getTlvList().add(new TLVMessage(0x11, srcMac));
			byte[] dstMac = new byte[6];
			for (int i = 0; i < dstMac.length; i++) {
				dstMac[i] = (byte) (1000 * Math.random());
			}
			msg.getTlvList().add(new TLVMessage(0x12, dstMac));

			msg.getTlvList().add(new TLVMessage(0x0101, 1));// 上行Channel
			msg.getTlvList().add(new TLVMessage(0x0102, 2));// 下行Channel

			session.write(msg);
		}
		for (int x = 0; x < 100; x++) {
			SessionSegmentMessage msg = new SessionSegmentMessage();
			msg.setSessionId(sessionId);
			msg.setApplicationId(applicationId);
			msg.setProtocolId(protocolId);
			msg.setType(0x02);
			byte[] flow = new byte[(int) (1 + Math.random() * 64)];
			for (int i = 0; i < flow.length; i++) {
				flow[i] = (byte) (1000 * Math.random());
			}
			msg.getTlvList().add(new TLVMessage(0x21, flow));
			byte[] operation = new byte[(int) (1 + Math.random() * 64)];
			for (int i = 0; i < operation.length; i++) {
				operation[i] = (byte) (1000 * Math.random());
			}
			msg.getTlvList().add(new TLVMessage(0x22, operation));			
			byte[] req = new byte[(int) (64 + Math.random() * 20000)];
			
			for (int i = 0; i < req.length; i++) {
				req[i] = (byte) (0x30 + i % 10);
			}
			req[0] = (byte) (0x30 + x%10);
			msg.getTlvList().add(new TLVMessage(0x23, req));
			byte[] resp = new byte[(int) (64 + Math.random() * 20000)];
			for (int i = 0; i < resp.length; i++) {
				resp[i] = (byte) (0x30 + i % 10);
			}
			resp[0] = (byte) (0x30 + x%10);
			msg.getTlvList().add(new TLVMessage(0x24, resp));
			msg.getTlvList().add(new TLVMessage(0x25, System.currentTimeMillis()));
			msg.getTlvList().add(new TLVMessage(0x26, System.currentTimeMillis()));
			msg.getTlvList().add(new TLVMessage(0x27, System.currentTimeMillis()));
			msg.getTlvList().add(new TLVMessage(0x28, System.currentTimeMillis()));
			
			session.write(msg);
		}
	}

	public void run() {
		System.out.println(this.getClass().getName() + " start...");
		while (true) {
			if (session == null || session.isClosing()) {
				break;
			}
			try {
				sendSSHSession();

			} catch (Exception e1) {
				e1.printStackTrace();
			}
			
			try {
				Thread.sleep(1000L);
			} catch (Exception e) {
			}
		}
		System.out.println(this.getClass().getName() + " end ...");
	}

	@Override
	public void messageReceived(IoSession session, Object message) throws Exception {
		System.out.println("message = " + message);
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		session.close();
		this.session = null;
	}

	public static void main(String[] args) {
		// 创建一个socket连接
		NioSocketConnector connector = new NioSocketConnector();
		// 获取过滤器链
		DefaultIoFilterChainBuilder chain = connector.getFilterChain();

		// 添加编码过滤器 处理乱码、编码问题
		chain.addLast("objectFilter", new ProtocolCodecFilter(new SessionMessageCodecFactory()));
		// 消息核心处理器
		connector.setHandler(new SessionClient());
		// 设置链接超时时间
		connector.setConnectTimeoutCheckInterval(30);
		// 连接服务器，知道端口、地址SessionClient.java
		ConnectFuture cf = connector.connect(new InetSocketAddress("localhost",10000+9020));
		//ConnectFuture cf = connector.connect(new InetSocketAddress("192.168.222.222", 10000+Constants.SESSION_LOG_SERVER_DEFAULT_PORT));
		// 等待连接创建完成
		cf.awaitUninterruptibly();
		cf.getSession().getCloseFuture().awaitUninterruptibly();
		connector.dispose();
	}

}
