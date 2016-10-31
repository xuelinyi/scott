package com.comverse.timesheet.web.bean.book;

public class BookTemporary {
	private int id;
	private String bookName;
	private int authorId;
	private int bookType;
	private String bookSynopsis;
	private String bookFile;
	private String createFile;
	private String modifyTime;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getBookName() {
		return bookName;
	}
	public void setBookName(String bookName) {
		this.bookName = bookName;
	}
	public int getAuthorId() {
		return authorId;
	}
	public void setAuthorId(int authorId) {
		this.authorId = authorId;
	}
	public int getBookType() {
		return bookType;
	}
	public void setBookType(int bookType) {
		this.bookType = bookType;
	}
	public String getBookSynopsis() {
		return bookSynopsis;
	}
	public void setBookSynopsis(String bookSynopsis) {
		this.bookSynopsis = bookSynopsis;
	}
	public String getBookFile() {
		return bookFile;
	}
	public void setBookFile(String bookFile) {
		this.bookFile = bookFile;
	}
	public String getCreateFile() {
		return createFile;
	}
	public void setCreateFile(String createFile) {
		this.createFile = createFile;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Book [id=");
		builder.append(id);
		builder.append(", bookName=");
		builder.append(bookName);
		builder.append(", authorId=");
		builder.append(authorId);
		builder.append(", bookType=");
		builder.append(bookType);
		builder.append(", bookSynopsis=");
		builder.append(bookSynopsis);
		builder.append(", bookFile=");
		builder.append(bookFile);
		builder.append(", createFile=");
		builder.append(createFile);
		builder.append(", modifyTime=");
		builder.append(modifyTime);
		builder.append("]");
		return builder.toString();
	}
}
