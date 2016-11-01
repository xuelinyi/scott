package com.comverse.timesheet.web.business.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mortbay.log.Log;
import org.springframework.stereotype.Component;

import com.comverse.timesheet.web.bean.book.BookTemporary;
import com.comverse.timesheet.web.business.IBookBusiness;
import com.comverse.timesheet.web.dao.IBookDao;
@Component
public class BookBusinessImpl implements IBookBusiness{
	private static final Logger log = Logger.getLogger(BookBusinessImpl.class);
	@Resource
	private IBookDao bookDao;
	public boolean addTemporaryBook(BookTemporary bookTemporary){
		Log.debug("增加书籍。bookTemporary ： " + bookTemporary);
		boolean addResult = false;
		if(null != bookTemporary) {
			try {
				addResult = bookDao.addTemporaryBook(bookTemporary);
			}catch(Exception e) {
				log.error("增加书籍失败。e:"+e);
			}
		}
		return addResult;
	}
	public List<BookTemporary> findTemporaryBook(){
		Log.debug("查询所有书籍。");
		try {
			return bookDao.findTemporaryBook();
		} catch (Exception e) {
			log.error("查询书籍产生异常：e" +e);
		}
		return Collections.EMPTY_LIST;
	}
	public BookTemporary getTemporaryBook(int bookTemporaryId) {
		log.debug("根据书籍ID查找对应的书籍信息。bookTemporaryId：" +bookTemporaryId);
		if(0 != bookTemporaryId) {
			try {
				return bookDao.getBookTemporary(bookTemporaryId);
			} catch (Exception e) {
				log.error("根据书籍ID查找对应的书籍信息发生异常e:"+e);
			}
		}
		return null;
	}
	public boolean updateTemporaryBook(BookTemporary bookTemporary){
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
