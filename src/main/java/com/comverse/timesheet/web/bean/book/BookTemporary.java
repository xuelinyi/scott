package com.comverse.timesheet.web.bean.book;

import com.comverse.timesheet.web.BookEnum;
import com.comverse.timesheet.web.bean.author.Author;
import com.comverse.timesheet.web.dto.BookTemporaryDTO;

public class BookTemporary {
	private int id;
	private String bookName;
//	private int authorId;
	private Author author;
	private int bookType;
	private String bookSynopsis;
	private String bookFile;
	private int isCheck;
	private int IllegalCharacter; 
	private String createTime;
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
	public Author getAuthor() {
		return author;
	}
	public void setAuthor(Author author) {
		this.author = author;
	}
	//	public int getAuthorId() {
//		return authorId;
//	}
//	public void setAuthorId(int authorId) {
//		this.authorId = authorId;
//	}
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
	public String getCreateTime() {
		return createTime;
	}
	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}
	public String getModifyTime() {
		return modifyTime;
	}
	public void setModifyTime(String modifyTime) {
		this.modifyTime = modifyTime;
	}
	public int getIsCheck() {
		return isCheck;
	}
	public void setIsCheck(int isCheck) {
		this.isCheck = isCheck;
	}
	
	public int getIllegalCharacter() {
		return IllegalCharacter;
	}
	public void setIllegalCharacter(int illegalCharacter) {
		IllegalCharacter = illegalCharacter;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("BookTemporary [id=");
		builder.append(id);
		builder.append(", bookName=");
		builder.append(bookName);
		builder.append(", author=");
		builder.append(author);
		builder.append(", bookType=");
		builder.append(bookType);
		builder.append(", bookSynopsis=");
		builder.append(bookSynopsis);
		builder.append(", bookFile=");
		builder.append(bookFile);
		builder.append(", isCheck=");
		builder.append(isCheck);
		builder.append(", createTime=");
		builder.append(createTime);
		builder.append(", modifyTime=");
		builder.append(modifyTime);
		builder.append("]");
		return builder.toString();
	}
	public static BookTemporaryDTO conversionBookTemporary(BookTemporary bookTemporary) {
		BookTemporaryDTO bookTemporaryDTO = new BookTemporaryDTO();
		bookTemporaryDTO.setId(bookTemporary.getId());
		int isChecked = bookTemporary.getIsCheck();
		BookEnum.IS_CHECK_FAIL.getSexFlag();
		bookTemporaryDTO.setIsCheck(isChecked);
		bookTemporaryDTO.setIsCheckedWithStr(BookEnum.getValue(isChecked));
		bookTemporaryDTO.setBookName(bookTemporary.getBookName());
		bookTemporaryDTO.setAuthor(bookTemporary.getAuthor());
		bookTemporaryDTO.setBookType(bookTemporary.getBookType());
		bookTemporaryDTO.setBookTypeStr(BookEnum.getValue(bookTemporary.getBookType()));
		bookTemporaryDTO.setBookSynopsis(bookTemporary.getBookSynopsis());
		bookTemporaryDTO.setBookFile(bookTemporary.getBookFile());
		bookTemporaryDTO.setCreateTime(bookTemporary.getCreateTime());
		bookTemporaryDTO.setModifyTime(bookTemporary.getModifyTime());
		bookTemporaryDTO.setIllegalCharacter(bookTemporary.getIllegalCharacter());
		return bookTemporaryDTO;
	}
	public BookTemporary(){
		
	}
	public BookTemporary(String bookName,Author author,int bookType,String bookSynopsis,String bookFile) {
		this.bookFile = bookFile;
		this.bookName = bookName;
		this.author = author;
		this.bookType = bookType;
		this.bookSynopsis = bookSynopsis;
	}
}
