package com.comverse.timesheet.web.business;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.comverse.timesheet.web.bean.book.BookTemporary;
import com.comverse.timesheet.web.dto.BookTemporaryDTO;

public interface IBookBusiness {
	public boolean addTemporaryBook(String bookName,int authorId,int bookType,String bookSynopsis,String filePath);
	public String uploadingBookForm(MultipartFile bookFile);
	public List<BookTemporaryDTO> findTemporaryBook(BookTemporaryDTO bookTemporaryDTO);
	public BookTemporary getTemporaryBook(int bookTemporaryId);
	public boolean updateTemporaryBook(BookTemporary bookTemporary);
}
