package com.comverse.timesheet.web.dto;

import java.util.List;

import com.comverse.timesheet.web.bean.author.Author;
import com.comverse.timesheet.web.bean.book.BookTemporary;

public class AuthorAndBookDTO {
	private BookTemporary bookTemporary;
	private List<AuthorDTO> authorList;
	public BookTemporary getBookTemporary() {
		return bookTemporary;
	}
	public void setBookTemporary(BookTemporary bookTemporary) {
		this.bookTemporary = bookTemporary;
	}
	public List<AuthorDTO> getAuthorList() {
		return authorList;
	}
	public void setAuthorList(List<AuthorDTO> authorList) {
		this.authorList = authorList;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuthorAndBookDTO [bookTemporary=");
		builder.append(bookTemporary);
		builder.append(", authorList=");
		builder.append(authorList);
		builder.append("]");
		return builder.toString();
	}
}
