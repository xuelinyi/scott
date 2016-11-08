package com.comverse.timesheet.web.controller;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.comverse.timesheet.web.bean.book.BookTemporary;
import com.comverse.timesheet.web.business.IAuthorBusiness;
import com.comverse.timesheet.web.business.IBookBusiness;
import com.comverse.timesheet.web.business.ISystemBusiness;
import com.comverse.timesheet.web.dto.AuthorAndBookDTO;
import com.comverse.timesheet.web.dto.BookTemporaryDTO;

@Controller
public class BookCotroller  extends BaseController{
	private static final Logger log = Logger.getLogger(BookCotroller.class);
	@Autowired
    private IBookBusiness bookBusiness; 
	@Autowired
	private IAuthorBusiness authorBusiness;
	@Autowired
	private ISystemBusiness systemBusiness;
	/**
	 *  跳转图书管理首页
	 * @return
	 */
	@RequestMapping("book/temporaryBookList")
	public String jumpgetTemporaryBook(ModelMap modelMap) {
		log.debug("跳转到图书管理首页。");
		modelMap.addAttribute("bookList", bookBusiness.findTemporaryBook(new BookTemporaryDTO()));
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
	@RequestMapping(value="/book/jumpAddBook")
	public String jumpAddBook(ModelMap modelMap) {
		log.debug("跳转上传书籍页面");
		modelMap.addAttribute("authorList", authorBusiness.findAuthor());
		return "addBookPage";
	}
	
	@RequestMapping(value="/book/addBookSave", method = RequestMethod.POST)
	@ResponseBody
	public boolean getTemporaryBook(@RequestParam(value = "bookFile", required = true)MultipartFile bookFile,@RequestParam(value = "bookName", required = true) String bookName
			,@RequestParam(value = "authorId", required = true) int authorId,@RequestParam(value = "bookType", required = true) int bookType
			,@RequestParam(value = "bookSynopsis", required = true) String bookSynopsis) {
		log.debug("file:"+bookFile);
		boolean result = false;
		if((null!=bookFile)&&(0!=bookFile.getSize())) {
			String bookFilePath = bookBusiness.uploadingBookForm(bookFile);
			if(null != bookFilePath) {
				result = bookBusiness.addTemporaryBook(bookName,authorId,bookType,bookSynopsis,bookFilePath);
			}
		}
		return result;
	}
}
