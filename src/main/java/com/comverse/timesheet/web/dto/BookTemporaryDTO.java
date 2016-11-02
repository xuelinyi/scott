package com.comverse.timesheet.web.dto;

import com.comverse.timesheet.web.bean.book.BookTemporary;

public class BookTemporaryDTO extends BookTemporary{
	private String bookTypeStr;

	public String getBookTypeStr() {
		return bookTypeStr;
	}

	public void setBookTypeStr(String bookTypeStr) {
		this.bookTypeStr = bookTypeStr;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BookTemporaryDTO [bookTypeStr=");
		builder.append(bookTypeStr);
		builder.append(", getId()=");
		builder.append(getId());
		builder.append(", getBookName()=");
		builder.append(getBookName());
		builder.append(", getAuthor()=");
		builder.append(getAuthor());
		builder.append(", getBookType()=");
		builder.append(getBookType());
		builder.append(", getBookSynopsis()=");
		builder.append(getBookSynopsis());
		builder.append(", getBookFile()=");
		builder.append(getBookFile());
		builder.append(", getCreateTime()=");
		builder.append(getCreateTime());
		builder.append(", getModifyTime()=");
		builder.append(getModifyTime());
		builder.append(", toString()=");
		builder.append(super.toString());
		builder.append(", getClass()=");
		builder.append(getClass());
		builder.append(", hashCode()=");
		builder.append(hashCode());
		builder.append("]");
		return builder.toString();
	}

	
}
