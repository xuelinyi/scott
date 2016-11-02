package com.comverse.timesheet.web.business.impl;

import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mortbay.log.Log;
import org.springframework.stereotype.Component;

import com.comverse.timesheet.web.bean.author.Author;
import com.comverse.timesheet.web.bean.book.BookTemporary;
import com.comverse.timesheet.web.business.IAuthorBusiness;
import com.comverse.timesheet.web.business.IBookBusiness;
import com.comverse.timesheet.web.dao.IAuthorDao;
import com.comverse.timesheet.web.dao.IBookDao;
import com.comverse.timesheet.web.dto.AuthorDTO;
@Component
public class AuthorBusinessImpl implements IAuthorBusiness{
	private static final Logger log = Logger.getLogger(AuthorBusinessImpl.class);
	@Resource
	private IAuthorDao authorDao;
	public boolean addAuthor(Author author){
		Log.debug("增加作者。author ： " + author);
		boolean addResult = false;
		if(null != author) {
			try {
				addResult = authorDao.addAuthor(author);
			}catch(Exception e) {
				log.error("增加作者失败。e:"+e);
			}
		}
		return addResult;
	}
	public List<AuthorDTO> findAuthor(){
		Log.debug("查询所有作者。");
		try {
			return authorDao.findAuthor();
		} catch (Exception e) {
			log.error("查询作者产生异常：e" +e);
		}
		return Collections.EMPTY_LIST;
	}
	public Author getAuthor(int authorId) {
		log.debug("根据书籍ID查找对应的作者信息。authorId：" +authorId);
		if(0 != authorId) {
			try {
				return authorDao.getAuthor(authorId);
			} catch (Exception e) {
				log.error("根据作者ID查找对应的作者信息发生异常e:"+e);
			}
		}
		return null;
	}
	public boolean updateAuthor(Author author){
		log.debug("编辑书籍信息author:"+author);
		if(null != author) {
			try {
				return authorDao.updateAuthor(author);
			} catch (Exception e) {
				log.error("编辑作者信息发生异常e:"+e);
			}
		}
		return false;
	}
	public boolean deleteAuthor(int authorId) {
		log.debug("根据ID删除作者信息。authorId:"+authorId);
		if(0!=authorId) {
			try{
				return authorDao.deleteAuthor(authorId);
			}catch(Exception e) {
				log.error("根据ID"+authorId+"删除作者信息产生异常。e:"+e);
			}
		}
		return false;
	}
}
