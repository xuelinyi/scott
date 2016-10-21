package com.comverse.timesheet.web.server.dto;

public class TLVKey{
	int tag;

	public TLVKey(int tag) {
		super();
		this.tag = tag&0x7FFF;
	}

	public int getTag() {
		return tag;
	}

	public void setTag(int tag) {
		this.tag = tag;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + tag;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TLVKey other = (TLVKey) obj;
		if (tag != other.tag)
			return false;
		return true;
	}
	
}
