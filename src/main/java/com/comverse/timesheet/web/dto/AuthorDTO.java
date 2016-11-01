package com.comverse.timesheet.web.dto;

import com.comverse.timesheet.web.bean.author.Author;

public class AuthorDTO extends Author{
	private String sexStr;

	public String getSexStr() {
		return sexStr;
	}

	public void setSexStr(String sexStr) {
		this.sexStr = sexStr;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AuthorDTO [sexStr=");
		builder.append(sexStr);
		builder.append(", getId()=");
		builder.append(getId());
		builder.append(", getName()=");
		builder.append(getName());
		builder.append(", getAge()=");
		builder.append(getAge());
		builder.append(", getSex()=");
		builder.append(getSex());
		builder.append(", getBirthday()=");
		builder.append(getBirthday());
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
