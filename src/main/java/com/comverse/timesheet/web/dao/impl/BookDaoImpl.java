package com.comverse.timesheet.web.dao.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.mortbay.log.Log;
import org.springframework.stereotype.Repository;

import com.comverse.timesheet.web.BookEnum;
import com.comverse.timesheet.web.bean.book.BookTemporary;
import com.comverse.timesheet.web.dao.IBookDao;
import com.comverse.timesheet.web.dao.ITestTableDAO;
import com.comverse.timesheet.web.dto.BookTemporaryDTO;
import com.comverse.timesheet.web.util.BasicSqlSupport;

@Repository
public class BookDaoImpl extends BasicSqlSupport implements IBookDao {

	public List<BookTemporaryDTO> findTemporaryBook(BookTemporaryDTO bookTemporaryDTO) throws Exception {
		Log.debug("查询所有的书籍信息");
		List<BookTemporary>bookTemporaryList = session.selectList("mybatis.mapper.Book.selectBookByNull",bookTemporaryDTO);
		List<BookTemporaryDTO>bookTemporaryDTOList = new ArrayList<BookTemporaryDTO>();
		if((null != bookTemporaryList)&&(0!=bookTemporaryList.size())) {
			for (BookTemporary bookTemporary : bookTemporaryList) {
				bookTemporaryDTOList.add(BookTemporary.conversionBookTemporary(bookTemporary));
			}
		}
		return bookTemporaryDTOList;
	}

	public boolean addTemporaryBook(BookTemporary bookTemporary) throws Exception {
		Log.debug("增加书籍信息。bookTemporary ： " + bookTemporary);
		boolean flag=false;
		if((null!=bookTemporary)) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			bookTemporary.setCreateTime(formatter.format(new Date()));
			bookTemporary.setIsCheck(BookEnum.IS_CHECK_NO.getSexFlag());
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
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			bookTemporary.setModifyTime(formatter.format(new Date()));
			int count = session.update("mybatis.mapper.Book.updateBookTemporary", bookTemporary);
			if(count>0){ 
	            return true; 
			} 
		}
		return false;
	} 

}
