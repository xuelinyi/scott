package com.comverse.timesheet.web.business.impl;

import java.io.IOException;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import com.comverse.timesheet.web.controller.SessionInjectionInterceptor;

public class AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private static Logger log = Logger.getLogger(AuthenticationFailureHandler.class);

	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		String clientIp=SessionInjectionInterceptor.getClientIP(request);
		String username=request.getParameter("username");
		log.warn("onAuthenticationFailure start username = "+username);
		
			try{
					int lockCount = Integer.parseInt(jdbcTemplate.queryForObject( "SELECT VVALUE FROM SYSCONFIG WHERE IID = ?", new Object[] {"MAX_LOGIN_COUNT"}, java.lang.String.class));
					int loginNumber = Integer.parseInt(jdbcTemplate.queryForObject( "SELECT LLOGIN_NUMBER FROM AACCOUNT WHERE NNAME = ?", new Object[] {username}, java.lang.String.class));
					if(loginNumber<lockCount){
						String sql = "UPDATE AACCOUNT SET LLOGIN_NUMBER = "+(loginNumber+1)+" WHERE NNAME = '"+username+"'";
						jdbcTemplate.execute(sql);
					}else{
						SimpleDateFormat df=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						//得到系统配置的用户的锁定时间
						int lockTime =Integer.parseInt(jdbcTemplate.queryForObject( "SELECT VVALUE FROM SYSCONFIG WHERE IID = ?", new Object[] {"UNLOCK_ACCOUNT_TIME"}, java.lang.String.class));
						Date date = new Date(System.currentTimeMillis()+lockTime*1000L);
						String sql = "UPDATE AACCOUNT SET LLOGIN_NUMBER = "+(loginNumber+1)+"  , LLOCKEND_TIME = '"+df.format(date)+"' WHERE NNAME = '"+username+"'";
						jdbcTemplate.execute(sql);
					}
				SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
				String LoginSql = "INSERT INTO ADMIN_LOG(ttime,llevel,AACCOUNT,IIP,DDESC) VALUES('"+df.format(new Date())+"','96','"+username+"','"+clientIp+"','用户:"+username+"登陆失败')";
				jdbcTemplate.execute(LoginSql);
			}catch(Exception e){
				log.error(e,e);
			}
		
		log.warn("onAuthenticationFailure end username = "+username);
		super.onAuthenticationFailure(request, response, exception);
	}
	@SuppressWarnings({ "static-access", "unused" })
	public String getLocalIp(){
		InetAddress ia=null;
		try {
			ia=ia.getLocalHost();
			
			String localName=ia.getHostName();
			String localIp=ia.getHostAddress();
			return localIp;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
}
