package com.comverse.timesheet.web.dao.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.mortbay.log.Log;
import org.springframework.stereotype.Repository;

import com.comverse.timesheet.web.bean.author.Author;
import com.comverse.timesheet.web.dao.IAuthorDao;
import com.comverse.timesheet.web.util.BasicSqlSupport;

@Repository
public class AuthorDaoImpl extends BasicSqlSupport implements IAuthorDao {

	public List<Author> findAuthor() throws Exception {
		Log.debug("查询所有的作者信息");
		return session.selectList("mybatis.mapper.Author.selectAuthorByNull");
	}

	public boolean addAuthor(Author author) throws Exception {
		Log.debug("增加作者信息。author ： " + author);
		boolean flag=false;
		if((null!=author)) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			author.setCreateTime(formatter.format(new Date()));
			author.setModifyTime(formatter.format(new Date()));
	        int count=session.insert("mybatis.mapper.Author.addAuthor",author); 
	        if(count>0){ 
	            flag=true; 
	        } 
		}
	    return flag; 
	}

	public Author getAuthor(int id) throws Exception {
		Log.debug("根据作者的ID查询作者：id"+id);
		if(0 != id) {
			Author author = session.selectOne("mybatis.mapper.Author.selectAuthorById", id);
			return author;
		}
		return null;
	}

	public boolean updateAuthor(Author author)
			throws Exception {
		Log.debug("编辑作者信息。author："+author);
		if(null != author) {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			author.setModifyTime(formatter.format(new Date()));
			int count = session.update("mybatis.mapper.Author.updateAuthor", author);
			if(count>0){ 
	            return true; 
			} 
		}
		return false;
	}

	public boolean deleteAuthor(int id) throws Exception {
		Log.debug("根据作者的ID删除对应的作者信息。authorId："+id);
		if(0!=id) {
			int count = session.delete("mybatis.mapper.Author.deleteAuthor", id);
			if(count>0) {
				return true;
			}
		}
		return false;
	} 

}
