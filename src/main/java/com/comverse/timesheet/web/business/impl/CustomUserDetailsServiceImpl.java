package com.comverse.timesheet.web.business.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.sql.DataSource;

import org.apache.log4j.Logger;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@SuppressWarnings("serial")
public class CustomUserDetailsServiceImpl extends HttpServlet implements UserDetailsService {
	private static Logger log = Logger
			.getLogger(CustomUserDetailsServiceImpl.class);
	private JdbcTemplate jdbcTemplate;

	public void setDataSource(DataSource dataSource) {
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		if (username == null) {
			username = "";
		}
		CustomAccount account = null;
		try {
			account = jdbcTemplate.queryForObject(
					"SELECT * FROM AACCOUNT WHERE NNAME='" + username + "'",
					new CustomAccountRowMapper());
		} catch (Exception e) {
			log.error(e, e);
			if (e instanceof IncorrectResultSizeDataAccessException) {
				account = null;
				log.info("username not exist. name = " + username);
				throw new UsernameNotFoundException("Username not found");
			} else {
				throw new UsernameNotFoundException("Data Access Exception");
			}
		}
		if (null == account) {
			log.debug("User not found");
			throw new UsernameNotFoundException("Username not found");
		}
		
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		if ("admin".equals(username)) {
			authorities.add(new SimpleGrantedAuthority("ROLE_ACCOUNT"));
			authorities.add(new SimpleGrantedAuthority("ROLE_CONFIG"));
			authorities.add(new SimpleGrantedAuthority("ROLE_BACKUP_SESSION"));
			authorities.add(new SimpleGrantedAuthority("ROLE_AUDIT"));
		} else {
			List<Long> permissionCodeList = new ArrayList<Long>();
			try {
				permissionCodeList = jdbcTemplate
						.query(
								"SELECT p.CCODE as CCODE FROM AACCOUNT as a, ACCOUNT_RROLE_RELATION as arr, RROLE as r ,RROLE_PERMISSION_RELATION  as rpr, PPERMISSION as p WHERE a.NNAME='"
										+ username
										+ "' AND arr.AACCOUNT_ID=a.IID AND arr.RROLE_ID=r.IID AND r.IID = rpr.RROLE_ID AND rpr.PPERMISSION_ID = p.IID",
								new CustomPermissionCodeRowMapper());
			} catch (Exception e) {
				log.error(e, e);
			}
			for (Long code : permissionCodeList) {
				switch (code.intValue()) {
				case 10000:
					authorities.add(new SimpleGrantedAuthority("ROLE_ACCOUNT"));
					break;
				case 20000:
					authorities.add(new SimpleGrantedAuthority("ROLE_CONFIG"));
					break;
				case 30000:
					authorities.add(new SimpleGrantedAuthority(
							"ROLE_BACKUP_SESSION"));
					break;
				case 40000:
					authorities.add(new SimpleGrantedAuthority("ROLE_AUDIT"));
					break;
				}
			}
		}
		return new org.springframework.security.core.userdetails.User(account
				.getUsername(), account.getPassword(), true, true, true,
				!account.isLocked(), authorities);
	}

	private class CustomPermissionCodeRowMapper implements RowMapper<Long> {
		public Long mapRow(ResultSet rs, int value) throws SQLException {
			return rs.getLong("CCODE");
		}
	}

	private class CustomAccountRowMapper implements RowMapper<CustomAccount> {
		public CustomAccount mapRow(ResultSet rs, int value)
				throws SQLException {
			
			CustomAccount account = new CustomAccount();
			account.setUsername(rs.getString("NNAME"));
			account.setPassword(rs.getString("PPASSWORD"));
			try {
					SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
					int loginNumber = Integer
							.parseInt(jdbcTemplate.queryForObject(
									"SELECT VVALUE FROM SYSCONFIG WHERE IID = ?",
									new Object[] { "MAX_LOGIN_COUNT" },
									java.lang.String.class));
					log.debug("系统配置的最大登录次数是" + loginNumber);
					if ((rs.getLong("LLOGIN_NUMBER") >= loginNumber)&&((null != rs.getString("LLOCKEND_TIME"))
									&& (!"".equals(rs.getString("LLOCKEND_TIME"))) && (df
									.parse(rs.getString("LLOCKEND_TIME")).after(new Date())))){
							account.setLocked(true);
					}
					else if((rs.getLong("LLOGIN_NUMBER") >= loginNumber)&&(null == rs.getString("LLOCKEND_TIME"))){
						account.setLocked(true);
					}else if((rs.getLong("LLOGIN_NUMBER") >= loginNumber)&&("".equals(rs.getString("LLOCKEND_TIME")))){
						account.setLocked(true);
					}
					
					if (((null != rs.getString("LLOCKEND_TIME"))
							&& (!"".equals(rs.getString("LLOCKEND_TIME"))) && (df
							.parse(rs.getString("LLOCKEND_TIME")).before(new Date())))){
						String sql = "UPDATE AACCOUNT SET LLOGIN_NUMBER = 0 , LLOCKEND_TIME = NULL WHERE NNAME = '"
							+ rs.getString("NNAME") + "'";
						jdbcTemplate.execute(sql);
						account.setLocked(false);
					}		
			} catch (Exception e) {
				log.error(e, e);
			}
			return account;
		}
	}

	private class CustomAccount {
		private String username;
		private String password;
		private boolean isLocked;

		public String getUsername() {
			return username;
		}

		public void setUsername(String username) {
			this.username = username;
		}

		public String getPassword() {
			return password;
		}

		public void setPassword(String password) {
			this.password = password;
		}

		public boolean isLocked() {
			return isLocked;
		}

		public void setLocked(boolean isLocked) {
			this.isLocked = isLocked;
		}

	}
}