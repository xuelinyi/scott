package com.comverse.timesheet.web.server.message;

import org.apache.commons.codec.binary.Hex;

public class TLVMessage {
	private int tag=-1;
	private int len=0;
	private byte[]value=new byte[0];
	
	public TLVMessage() {
		super();
	}
	public TLVMessage(int tag, String value) {
		super();
		this.tag = tag;
		this.value = value.getBytes();
		this.len = this.value.length+4;
		
	}
	
	public TLVMessage(int tag, byte[] value) {
		super();
		this.tag = tag;
		this.value = value;
		this.len = this.value.length+4;		
	}
	public TLVMessage(int tag, int value) {
		super();
		this.tag = tag;
		this.value = new byte[4];
		int index=3;
		while(value>0){
			this.value[index--]=(byte)(value%256);
			value=value/256;
		}
		this.len = this.value.length+4;		
	}
	public TLVMessage(int tag, long value) {
		super();
		this.tag = tag;
		this.value = new byte[8];
		int index=7;
		while(value>0){
			this.value[index--]=(byte)(value%256);
			value=value/256;
		}
		this.len = this.value.length+4;		
	}

	public int getTag() {
		return tag;
	}
	public void setTag(int tag) {
		this.tag = tag;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public byte[] getValue() {
		return value;
	}
	public void setValue(byte[] value) {
		this.value = value;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TLVMessage [tag=0x");
		builder.append(Integer.toHexString(tag));
		builder.append(", len=");
		builder.append(len);
		builder.append(", value=");
		builder.append(Hex.encodeHexString(value));
		builder.append("]");
		return builder.toString();
	}

	
}
