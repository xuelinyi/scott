package com.comverse.timesheet.web.dao;

import java.util.List;

import com.comverse.timesheet.web.bean.book.BookTemporary;
import com.comverse.timesheet.web.dto.BookTemporaryDTO;

public interface IBookDao {
	public List<BookTemporaryDTO> findTemporaryBook(BookTemporaryDTO bookTemporaryDTO) throws Exception; 
	public boolean addTemporaryBook(BookTemporary bookTemporary) throws Exception; 
	public BookTemporary getBookTemporary(int id) throws Exception;
	public boolean updateBookTemporary(BookTemporary bookTemporary) throws Exception;
}
