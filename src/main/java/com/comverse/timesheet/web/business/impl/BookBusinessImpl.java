package com.comverse.timesheet.web.business.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.mortbay.log.Log;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.comverse.timesheet.web.bean.author.Author;
import com.comverse.timesheet.web.bean.book.BookTemporary;
import com.comverse.timesheet.web.business.IBookBusiness;
import com.comverse.timesheet.web.business.ISystemBusiness;
import com.comverse.timesheet.web.dao.IAuthorDao;
import com.comverse.timesheet.web.dao.IBookDao;
import com.comverse.timesheet.web.dto.BookTemporaryDTO;
@Component
public class BookBusinessImpl implements IBookBusiness{
	private static final Logger log = Logger.getLogger(BookBusinessImpl.class);
	@Resource
	private IBookDao bookDao;
	@Resource
	private IAuthorDao authorDao;
	@Resource
	private ISystemBusiness systemBusiness;
	public boolean addTemporaryBook(String bookName,int authorId,int bookType,String bookSynopsis,String filePath){
		Log.debug("增加书籍。bookName ： " + bookName);
		log.debug("authorId:"+authorId);
		log.debug("bookType:"+bookType);
		log.debug("bookSynopsis:"+bookSynopsis);
		log.debug("filePath:"+filePath);
		boolean addResult = false;
		if((null != bookSynopsis)&&(0 != bookType)&&(0 != authorId)&&(null != bookName)) {
			try {
				Author author = authorDao.getAuthor(authorId);
				if(null != author){ 
					BookTemporary bookTemporary = new BookTemporary(bookName, author, bookType, bookSynopsis, filePath);
					addResult = bookDao.addTemporaryBook(bookTemporary);
				}
			}catch(Exception e) {
				log.error("增加书籍失败。e:"+e);
			}
		}
		return addResult;
	}
	public List<BookTemporaryDTO> findTemporaryBook(BookTemporaryDTO bookTemporaryDTO){
		Log.debug("查询所有书籍。");
		try {
			return bookDao.findTemporaryBook(bookTemporaryDTO);
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
	public String uploadingBookForm(MultipartFile bookFile) {
		log.debug("将上传的文件保存到服务器。bookFile："+bookFile);
		if(null != bookFile) {
			try {
				String path = systemBusiness.getSysConfigure("BOOK_FILE_ADDRESS").getValue();
				String fileName = bookFile.getOriginalFilename(); //得到上传时的文件名
				SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMdd");
				String filePath = path + formatter.format(new Date())+File.separator + new Date().getTime()+"_" +fileName;
				File targetFile = new File(filePath);
				 if (!targetFile.exists()) {
					 targetFile.mkdirs();//如果文件夹不存在则创建
				 }
				bookFile.transferTo(targetFile);
				return filePath;
			}catch(Exception e) {
				log.error("上传文件失败。e:" + e);
			}
		}
		return null;
	}
}
