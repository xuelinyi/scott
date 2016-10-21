package com.comverse.timesheet.web.util;

import javax.annotation.Resource;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
@Repository
public class BasicSqlSupport{ 
	@Resource
    protected SqlSession session;

    public SqlSession getSession() { 
        return session; 
    } 
    public void setSession(SqlSession session) { 
        this.session = session; 
    } 
}
