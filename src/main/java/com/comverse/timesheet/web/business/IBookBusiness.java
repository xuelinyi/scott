package com.comverse.timesheet.web.business;

import java.util.List;

import com.comverse.timesheet.web.bean.book.BookTemporary;

public interface IBookBusiness {
	public boolean addTemporaryBook(BookTemporary bookTemporary);
	public List<BookTemporary> findTemporaryBook();
	public BookTemporary getTemporaryBook(int bookTemporaryId);
	public boolean updateTemporaryBook(BookTemporary bookTemporary);
}
