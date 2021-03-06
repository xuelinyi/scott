package com.comverse.timesheet.web.dao;

import java.util.List;

import com.comverse.timesheet.web.bean.author.Author;
import com.comverse.timesheet.web.bean.book.BookTemporary;
import com.comverse.timesheet.web.dto.AuthorDTO;

public interface IAuthorDao {
	public List<AuthorDTO> findAuthor() throws Exception; 
	public boolean addAuthor(Author author) throws Exception; 
	public Author getAuthor(int authorId) throws Exception;
	public boolean updateAuthor(Author author) throws Exception;
	public boolean deleteAuthor(int authorId) throws Exception;
}
