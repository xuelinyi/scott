package com.comverse.timesheet.web.business;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mortbay.log.Log;
import org.springframework.stereotype.Component;

import com.comverse.timesheet.web.bean.book.BookTemporary;
import com.comverse.timesheet.web.dao.IBookDao;

@Component
public class BookBusiness {
	private static final Logger log = Logger.getLogger(BookBusiness.class);
	@Resource
	private IBookDao bookDao;
	public boolean addBook(BookTemporary bookTemporary) throws Exception {
		Log.debug("增加书籍。bookTemporary ： " + bookTemporary);
		boolean addResult = false;
		if(null != bookTemporary) {
			addResult = bookDao.add(bookTemporary);
		}
		return addResult;
	}
	public List<BookTemporary> findBookTemporary(){
		Log.debug("查询所有书籍。");
		try {
			return bookDao.find();
		} catch (Exception e) {
			log.error("查询书籍产生异常：e" +e);
		}
		return Collections.EMPTY_LIST;
	}
	public BookTemporary getBook(int bookId) {
		log.debug("根据书籍ID查找对应的书籍信息。bookID：" +bookId);
		if(0 != bookId) {
			try {
				return bookDao.getBookTemporary(bookId);
			} catch (Exception e) {
				log.error("根据书籍ID查找对应的书籍信息发生异常e:"+e);
			}
		}
		return null;
	}
	public boolean updateBook(BookTemporary bookTemporary){
		log.debug("编辑书籍信息bookTemporary:"+bookTemporary);
		if(null != bookTemporary) {
			try {
				return bookDao.updateBookTemporary(bookTemporary);
			} catch (Exception e) {
				log.error("编辑书籍信息发生异常e:"+e);
			}
		}
		return false;
	}
}
