package com.comverse.timesheet.web.controller;

import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

@Controller
public class SessionInjectionInterceptor extends HandlerInterceptorAdapter {
	private static Logger log = Logger
			.getLogger(SessionInjectionInterceptor.class);
	@Autowired
	private HttpSession session;
	private JdbcTemplate jdbcTemplate;
	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}

	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		log.error("RemoteAddr = "+request.getRemoteAddr());
		log.error("ContextPath = "+request.getContextPath());
		log.error("RequestURI = "+request.getRequestURI());
		log.error("user = "+session.getAttribute("user"));
		log.debug("clientIp: " + getClientIP(request));
		log.debug("clientIpHost: " + request.getRemoteHost());
		if(session.getAttribute("clientIp") == null){
			String clientIp=getClientIP(request);
			if(clientIp==null){
				session.setAttribute("clientIp", request.getRemoteHost());
			}else{
				session.setAttribute("clientIp", clientIp);
			}
		}
		
		Authentication authentication = SecurityContextHolder.getContext()
				.getAuthentication();
		if (authentication != null
				&& session.getAttribute("user") == null) {
			Object principal = authentication.getPrincipal();
			log.debug("principal = " + principal);
			String username="anonymousUser";
			if (principal instanceof UserDetails) {
				username = ((UserDetails)principal).getUsername();
	        } else {
	        	username = principal.toString();
	        }			
			if (!"anonymousUser".equals(username)) {
				List<Map<String, Object>> accountIpManaList = jdbcTemplate.queryForList("SELECT * FROM IP_MANAGEMENT WHERE AACCOUNT_NAME = '"+username+"' || AACCOUNT_NAME='�?��用户'");
				boolean loginFlag = false;
				if(null!=accountIpManaList){
					for (int i = 0; i < accountIpManaList.size(); i++) {
						String accountIpValue = (String) accountIpManaList.get(i).get("IIP");
						String accountNetMaskValue = (String) accountIpManaList.get(i).get("NNETMASK");
						long accountIpOfLong = stringToLong(accountIpValue);
						long accountNetMaskOfLong = stringToLong(accountNetMaskValue);
						long remoteAddrOfLong = stringToLong(session.getAttribute("clientIp").toString());
						if((remoteAddrOfLong&accountNetMaskOfLong)==accountIpOfLong){
							loginFlag = true;
							break;
						}
					}
				}
				if(username.equals("admin")){
					session.setAttribute("user",username);
					SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					String LoginSuccessSql = "INSERT INTO ADMIN_LOG(ttime,llevel,AACCOUNT,IIP,DDESC) VALUES('"+df.format(new Date())+"','80','"+username+"','"+session.getAttribute("clientIp")+"','用户:"+username+"登陆成功);";
					jdbcTemplate.execute(LoginSuccessSql);
				}else{
					if(loginFlag){
						session.setAttribute("user",username);
						SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						String LoginSuccessSql = "INSERT INTO ADMIN_LOG(ttime,llevel,AACCOUNT,IIP,DDESC) VALUES('"+df.format(new Date())+"','80','"+username+"','"+session.getAttribute("clientIp")+"','用户:"+username+"登陆成功);";
						jdbcTemplate.execute(LoginSuccessSql);
					}else{
						SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
						String LoginSql = "INSERT INTO ADMIN_LOG(ttime,llevel,AACCOUNT,IIP,DDESC) VALUES('"+df.format(new Date())+"','96','"+username+"','"+session.getAttribute("clientIp")+"','用户:"+username+"登陆失败);";
						jdbcTemplate.execute(LoginSql);
						Authentication auth = SecurityContextHolder.getContext().getAuthentication();
						session.setAttribute("user", null);
						new SecurityContextLogoutHandler().logout(request, response, auth);
					}
					
				}
			} else {
			}
		}
		return super.preHandle(request, response, handler);
		
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
			log.error(e,e);
		}
		return "";
	}

    public static void main(String[] args) {
    	long a = stringToLong("192.168.222.138");
    	long b = stringToLong("192.168.222.138");
    	long c = stringToLong("255.255.255.254");
    	System.out.println((a&c)==b);
    }
    public static long stringToLong(String strIp){
    	long result=0;
    	if(strIp==null){
    		return result;
    	}
		try {
			String[] strArr = strIp.split("\\.");
			for (int i = 0; i < strArr.length; i++) {
				int value = Integer.parseInt(strArr[i]);
				result=(result<<8)+value;
			}
		} catch (Exception e) {
			log.error(e,e);
		}
		return result;
    }
    
    public static String getClientIP(HttpServletRequest request) {  
        String fromSource = "X-Real-IP";  
        String ip = request.getHeader("X-Real-IP");  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("X-Forwarded-For");  
            fromSource = "X-Forwarded-For";  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("Proxy-Client-IP");  
            fromSource = "Proxy-Client-IP";  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getHeader("WL-Proxy-Client-IP");  
            fromSource = "WL-Proxy-Client-IP";  
        }  
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {  
            ip = request.getRemoteAddr();  
            fromSource = "request.getRemoteAddr";  
        }  
        return ip;  
    }  
}
