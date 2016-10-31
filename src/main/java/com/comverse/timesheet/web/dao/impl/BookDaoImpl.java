package com.comverse.timesheet.web.dao.impl;

import java.util.List;

import org.mortbay.log.Log;
import org.springframework.stereotype.Repository;

import com.comverse.timesheet.web.bean.book.BookTemporary;
import com.comverse.timesheet.web.dao.IBookDao;
import com.comverse.timesheet.web.dao.ITestTableDAO;
import com.comverse.timesheet.web.util.BasicSqlSupport;

@Repository
public class BookDaoImpl extends BasicSqlSupport implements IBookDao {

	public List<BookTemporary> find() throws Exception {
		Log.debug("查询所有的书籍信息");
		return session.selectList("mybatis.mapper.Book.selectBookByNull");
	}

	public boolean add(BookTemporary bookTemporary) throws Exception {
		Log.debug("增加书籍信息。bookTemporary ： " + bookTemporary);
		boolean flag=false;
		if((null!=bookTemporary)) {
	        int count=session.insert("mybatis.mapper.Book.addBook",bookTemporary); 
	        if(count>0){ 
	            flag=true; 
	        } 
		}
	    return flag; 
	}

	public BookTemporary getBookTemporary(int id) throws Exception {
		Log.debug("根据书籍的ID查询书籍：id"+id);
		if(0 != id) {
			BookTemporary bookTemporary = session.selectOne("mybatis.mapper.Book.selectBookById", id);
			return bookTemporary;
		}
		return null;
	}

	public boolean updateBookTemporary(BookTemporary bookTemporary)
			throws Exception {
		Log.debug("编辑书籍信息。bookTemporary："+bookTemporary);
		if(null != bookTemporary) {
			int count = session.update("mybatis.mapper.Book.updateBookTemporary", bookTemporary);
			if(count>0){ 
	            return true; 
			} 
		}
		return false;
	} 

}
