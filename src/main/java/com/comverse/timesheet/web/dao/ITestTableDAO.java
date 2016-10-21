package com.comverse.timesheet.web.dao;

import java.util.List;

import com.comverse.timesheet.web.bean.TestTable;

public interface ITestTableDAO { 
    public boolean add(TestTable test) throws Exception; 
    public List<TestTable> find() throws Exception; 
}