package com.comverse.timesheet.web.business;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.comverse.timesheet.web.bean.TestTable; 
import com.comverse.timesheet.web.dao.ITestTableDAO;
@Component
public class TestBusiness { 
	@Resource
    private ITestTableDAO testDAO; 
    public void add() throws Exception{ 
        TestTable test=new TestTable(); 
        test.setName("yuanmomo6"); 
        test.setBirthday(new Date()); 
        try { 
            testDAO.add(test); 
        } catch (Exception e) { 
            throw e; 
        } 
    } 
    public List<TestTable> find() throws Exception{ 
        try { 
            return testDAO.find(); 
        } catch (Exception e) { 
            throw e; 
        } 
    } 
}
