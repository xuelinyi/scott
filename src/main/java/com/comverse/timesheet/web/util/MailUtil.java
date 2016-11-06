package com.comverse.timesheet.web.util;

import java.io.IOException;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.commons.net.smtp.SMTPClient;
import org.apache.commons.net.smtp.SMTPReply;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Record;
import org.xbill.DNS.Type;

public class MailUtil {
	public static void sendMail(String mailHost,String userName,String password,String sendUser,String To,String cc,String subJect,String connect) throws Exception{
		Properties prop=new Properties();
		prop.put("mail.host",mailHost);
		prop.put("mail.transport.protocol", "smtp");
		prop.put("mail.smtp.auth", true);
		//使用java发送邮件5步骤
		//1.创建sesssion
		Session session=Session.getInstance(prop);
		//开启session的调试模式，可以查看当前邮件发送状态
		session.setDebug(true);


		//2.通过session获取Transport对象（发送邮件的核心API）
		Transport ts=session.getTransport();
		//3.通过邮件用户名密码链接
		ts.connect(userName, password);


		//4.创建邮件

		Message msg=createSimpleMail(session,sendUser,To,cc,subJect,connect);


		//5.发送电子邮件

		ts.sendMessage(msg, msg.getAllRecipients());
		}


		public static MimeMessage createSimpleMail(Session session,String sendUser,String To,String cc,String subJect,String connect) throws AddressException,MessagingException{
		//创建邮件对象
		MimeMessage mm=new MimeMessage(session);
		//设置发件人
		mm.setFrom(new InternetAddress("13521190257@163.com"));
		//设置收件人
		mm.setRecipient(Message.RecipientType.TO, new InternetAddress(To));
		//设置抄送人
		mm.setRecipient(Message.RecipientType.CC, new InternetAddress(cc));

		mm.setSubject(subJect);
		mm.setContent(connect, "text/html;charset=gbk");
		return mm;
	}
		public static boolean checkEmail(String email) {
	        if (!email.matches("[\\w\\.\\-]+@([\\w\\-]+\\.)+[\\w\\-]+")) {
	            return false;
	        }

	        String host = "";
	        String hostName = email.split("@")[1];
	        Record[] result = null;
	        SMTPClient client = new SMTPClient();

	        try {
	            // 查找MX记录
	            Lookup lookup = new Lookup(hostName, Type.MX);
	            lookup.run();
	            if (lookup.getResult() != Lookup.SUCCESSFUL) {
	                return false;
	            } else {
	                result = lookup.getAnswers();
	            }

	            // 连接到邮箱服务器
	            for (int i = 0; i < result.length; i++) {
	                host = result[i].getAdditionalName().toString();
	                client.connect(host);
	                if (!SMTPReply.isPositiveCompletion(client.getReplyCode())) {
	                    client.disconnect();
	                    continue;
	                } else {
	                    break;
	                }
	            }
	            //以下2项自己填写快速的，有效的邮箱
	            client.login("163.com");
	            client.setSender("13521190257@163.com");
	            client.addRecipient(email);
	            if (250 == client.getReplyCode()) {
	                return true;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	                client.disconnect();
	            } catch (IOException e) {
	            }
	        }
	        return false;
	    }
	public static void main(String[] args) throws MessagingException {
		System.out.println(checkEmail("107@qq.com"));

		}
}
