package com.comverse.timesheet.web.business.impl;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.comverse.timesheet.web.bean.TestTable; 
import com.comverse.timesheet.web.dao.ITestTableDAO; 
import com.comverse.timesheet.web.util.BasicSqlSupport;
@Repository
public class ITestTableDAOImpl extends BasicSqlSupport implements ITestTableDAO { 

    public boolean add(TestTable test) throws Exception { 
        boolean flag=false; 
        int count=session.insert("mybatis.mapper.Test.add",test); 
        if(count>0){ 
            flag=true; 
        } 
        return flag; 
    }

	public List<TestTable> find() throws Exception {
		// TODO Auto-generated method stub
		return session.selectList("mybatis.mapper.Test.selectByNull");
	} 
}
