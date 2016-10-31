package com.comverse.timesheet.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comverse.timesheet.web.bean.book.BookTemporary;
import com.comverse.timesheet.web.business.BookBusiness;

@Controller
public class BookCotroller {
	private static final Logger log = Logger.getLogger(BookCotroller.class);
	@Autowired
    private BookBusiness bookBusiness; 
	/**
	 *  跳转图书管理首页
	 * @return
	 */
	@RequestMapping("book/bookList")
	public String jumpBook(ModelMap modelMap) {
		log.debug("跳转到图书管理首页。");
		modelMap.addAttribute("bookList", bookBusiness.findBookTemporary());
		return "bookList";
	}
	@RequestMapping("book/getBook")
	@ResponseBody
	public BookTemporary getBook(@RequestParam(value = "bookId", required = true)int bookId,ModelMap modelMap) {
		log.debug("根据书籍ID查找对应的书籍信息id:" +bookId);
		if(0!=bookId) {
			return bookBusiness.getBook(bookId);
		}
		return null;
	}
	@RequestMapping("book/updateBook")
	@ResponseBody
	public boolean updateBook(BookTemporary book) {
		log.debug("编辑书籍信息为book:" + book);
		if(null != book) {
			return bookBusiness.updateBook(book);
		}
		return false;
	}
}
