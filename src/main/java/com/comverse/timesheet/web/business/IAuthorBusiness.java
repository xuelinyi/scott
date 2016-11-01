package com.comverse.timesheet.web.business;

import java.util.List;

import com.comverse.timesheet.web.bean.author.Author;
import com.comverse.timesheet.web.bean.book.BookTemporary;

public interface IAuthorBusiness {
	public List<Author> findAuthor();
	public boolean addAuthor(Author author);
	public Author getAuthor(int authorId);
	public boolean updateAuthor(Author author);
	public boolean deleteAuthor(int authorId);
}
