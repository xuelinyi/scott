package com.comverse.timesheet.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.comverse.timesheet.web.bean.book.BookTemporary;
import com.comverse.timesheet.web.business.IAuthorBusiness;
import com.comverse.timesheet.web.business.IBookBusiness;
import com.comverse.timesheet.web.dto.AuthorAndBookDTO;

@Controller
public class BookCotroller  extends BaseController{
	private static final Logger log = Logger.getLogger(BookCotroller.class);
	@Autowired
    private IBookBusiness bookBusiness; 
	@Autowired
	private IAuthorBusiness authorBusiness;
	/**
	 *  跳转图书管理首页
	 * @return
	 */
	@RequestMapping("book/temporaryBookList")
	public String jumpgetTemporaryBook(ModelMap modelMap) {
		log.debug("跳转到图书管理首页。");
		modelMap.addAttribute("bookList", bookBusiness.findTemporaryBook());
		return "bookList";
	}
	@RequestMapping("book/getTemporaryBook")
	@ResponseBody
	public AuthorAndBookDTO getTemporaryBook(@RequestParam(value = "bookId", required = true)int bookId,ModelMap modelMap) {
		log.debug("根据书籍ID查找对应的书籍信息id:" +bookId);
		AuthorAndBookDTO authorAndBookDTO = new AuthorAndBookDTO();
		if(0!=bookId) {
			authorAndBookDTO.setBookTemporary(bookBusiness.getTemporaryBook(bookId));
			authorAndBookDTO.setAuthorList(authorBusiness.findAuthor());
			return authorAndBookDTO;
		}
		return null;
	}
	@RequestMapping("book/updateTemporaryBook")
	@ResponseBody
	public boolean updateTemporaryBook(BookTemporary book) {
		log.debug("编辑书籍信息为book:" + book);
		boolean updateResult = false;
		if(null != book) {
			updateResult = bookBusiness.updateTemporaryBook(book);
			if(updateResult) 
				success("编辑书籍信息成功。book:"+book);
			else
				fail("编辑书籍信息失败。book:"+book);
		}
		return updateResult;
	}
}
