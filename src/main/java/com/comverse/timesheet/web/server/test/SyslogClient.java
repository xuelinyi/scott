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

import com.comverse.timesheet.web.server.codec.LogMessageCodecFactory;
import com.comverse.timesheet.web.server.impl.Constants;
import com.comverse.timesheet.web.server.message.AdminLogMessage;
import com.comverse.timesheet.web.server.message.AuditLogMessage;
import com.comverse.timesheet.web.server.message.DeviceLogMessage;
import com.comverse.timesheet.web.server.util.BinaryHelper;

public class SyslogClient extends IoHandlerAdapter implements Runnable{
	private IoSession session=null;
    public void sessionOpened(IoSession session) throws Exception {
    	this.session=session;
    	new Thread(this).start();
    }
    private static final byte[]LEVEL={0x10,0x20,0x30,0x40,0x50,0x60};
    private static final char[]WORDS={'我','你','他'};
    private static String getRandomString(int len){
    	StringBuffer sb=new StringBuffer();
    	for(int i=0;i<len;i++){
    		sb.append(WORDS[(int)(3*Math.random())]);
    	}
    	return sb.toString();
    }    
    public void run(){
    	System.out.println(this.getClass().getName()+" start...");
    	while(true){
    		if(session==null || session.isClosing()){
    			break;
    		}
    		
			IoBuffer buffer = IoBuffer.allocate(2048).setAutoExpand(true);			
			buffer.order(ByteOrder.BIG_ENDIAN);
				buffer.put(new byte[5]);
				
				buffer.put((byte)0xFE);
				buffer.putLong(0xFEFEFEFEFEFEFEFEL);
				buffer.put((byte)0xFE);
				buffer.putUnsignedShort(0x0C);
				buffer.flip();
				session.write(buffer);
			
    		AdminLogMessage admlm=new AdminLogMessage();
    		admlm.setId(System.currentTimeMillis());
    		admlm.setTime(System.currentTimeMillis());
    		admlm.setAccount(getRandomString((int)(Math.random()*32)));
    		admlm.setIp(BinaryHelper.intToIpString((int)(System.currentTimeMillis())));
    		admlm.setLevel(LEVEL[(int)(System.currentTimeMillis()%6)]);
    		admlm.setDesc(getRandomString((int)(Math.random()*1000)));
    		session.write(admlm);
    		
    		DeviceLogMessage dlm=new DeviceLogMessage();
    		dlm.setId(System.currentTimeMillis());    		
    		dlm.setTime(System.currentTimeMillis());
    		dlm.setLevel(LEVEL[(int)(System.currentTimeMillis()%6)]);
    		dlm.setDeviceName(getRandomString((int)(Math.random()*32)));
    		dlm.setIp(BinaryHelper.intToIpString((int)(System.currentTimeMillis())));
    		dlm.setDesc(getRandomString((int)(Math.random()*1000)));

    		session.write(dlm);
    		

    		AuditLogMessage alm=new AuditLogMessage();
    		alm.setId(System.currentTimeMillis());    		
    		alm.setTime(System.currentTimeMillis());
    		alm.setLevel(LEVEL[(int)(System.currentTimeMillis()%6)]);
    		alm.setSessionId(System.currentTimeMillis());
    		alm.setApplicationId((int)(System.currentTimeMillis()%10));
    		alm.setProtocolId((int)(System.currentTimeMillis()%40));
    		alm.setsIp(BinaryHelper.intToIpString((int)(System.currentTimeMillis())));
    		alm.setdIp(BinaryHelper.intToIpString((int)(System.currentTimeMillis())));
    		alm.setDesc(getRandomString((int)(Math.random()*1000)));
    		session.write(alm);
    		
    		try {
				Thread.sleep(1000L);
			} catch (InterruptedException e) {
			}
    	}
    	System.out.println(this.getClass().getName()+" end ...");
    }
     
   @Override  
   public void messageReceived(IoSession session, Object message)  
           throws Exception {           
       System.out.println("message = " + message);  
   }  
     
   @Override  
   public void sessionClosed(IoSession session) throws Exception {   
       session.close();
       this.session=null;
   }  
	public static void main(String[] args) {
	       // 创建一个socket连接        
        NioSocketConnector connector=new NioSocketConnector();  
        // 获取过滤器链          
        DefaultIoFilterChainBuilder chain=connector.getFilterChain();  
           
        // 添加编码过滤器 处理乱码、编码问题    
        chain.addLast("objectFilter",new ProtocolCodecFilter(new LogMessageCodecFactory()));  
        // 消息核心处理器       
        connector.setHandler(new SyslogClient());
        // 设置链接超时时间       
        connector.setConnectTimeoutCheckInterval(30);  
        // 连接服务器，知道端口、地址      
        //ConnectFuture cf = connector.connect(new InetSocketAddress("192.168.1.199",10000+Constants.SYSLOG_SERVER_DEFAULT_PORT));
        ConnectFuture cf = connector.connect(new InetSocketAddress("localhost",10000+Constants.SYSLOG_SERVER_DEFAULT_PORT));  
        //ConnectFuture cf = connector.connect(new InetSocketAddress("192.168.8.222",Constants.SYSLOG_SERVER_PORT));
        // 等待连接创建完成      
        cf.awaitUninterruptibly();  
        cf.getSession().getCloseFuture().awaitUninterruptibly();  
        connector.dispose();  
	}

}
