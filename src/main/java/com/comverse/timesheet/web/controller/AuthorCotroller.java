package com.comverse.timesheet.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comverse.timesheet.web.bean.author.Author;
import com.comverse.timesheet.web.bean.book.BookTemporary;
import com.comverse.timesheet.web.business.IAuthorBusiness;
import com.comverse.timesheet.web.business.IBookBusiness;

@Controller
public class AuthorCotroller {
	private static final Logger log = Logger.getLogger(AuthorCotroller.class);
	@Autowired
    private IAuthorBusiness authorBusiness; 
	/**
	 *  跳转作者管理首页
	 * @return
	 */
	@RequestMapping("author/authorList")
	public String jumpBook(ModelMap modelMap) {
		log.debug("跳转到作者管理首页。");
		modelMap.addAttribute("authorList", authorBusiness.findAuthor());
		return "authorList";
	}
	
	@RequestMapping("author/getAuthor")
	@ResponseBody
	public Author getAuthor(@RequestParam(value = "authorId", required = true)int authorId,ModelMap modelMap) {
		log.debug("根据作者ID查找对应的作者信息id:" +authorId);
		if(0!=authorId) {
			return authorBusiness.getAuthor(authorId);
		}
		return null;
	}
	@RequestMapping("author/addAuthor")
	@ResponseBody
	public boolean addAuthor(Author author) {
		log.debug("添加作者信息为author:" + author);
		if(null != author) {
			return authorBusiness.addAuthor(author);
		}
		return false;
	}
	@RequestMapping("author/updateAuthor")
	@ResponseBody
	public boolean updateAuthor(Author author) {
		log.debug("编辑作者信息为author:" + author);
		if(null != author) {
			return authorBusiness.updateAuthor(author);
		}
		return false;
	}
	@RequestMapping("author/deleteAuthor")
	@ResponseBody
	public boolean deleteAuthor(@RequestParam(value = "authorId", required = true)int authorId,ModelMap modelMap) {
		log.debug("根据ID删除作者信息。");
		if(0!=authorId) {
			return authorBusiness.deleteAuthor(authorId);
		}
		return false;
	}
}
