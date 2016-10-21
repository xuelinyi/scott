package com.comverse.timesheet.web.server.message;




import org.apache.commons.codec.binary.Hex;



public class PlainDataSegmentMessage{
	private int identifier;
	private String address;	
	private long sessionId;
	private long dataId;
	private int segment;
	private int segmentLen;
	private byte[]segmentData=new byte[0];
	
	public int getIdentifier() {
		return identifier;
	}
	public void setIdentifier(int identifier) {
		this.identifier = identifier;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public long getSessionId() {
		return sessionId;
	}
	public void setSessionId(long sessionId) {
		this.sessionId = sessionId;
	}
	public int getSegment() {
		return segment;
	}
	public void setSegment(int segment) {
		this.segment = segment;
	}
	public int getSegmentLen() {
		return segmentLen;
	}
	public void setSegmentLen(int segmentLen) {
		this.segmentLen = segmentLen;
	}
	public byte[] getSegmentData() {
		return segmentData;
	}
	public void setSegmentData(byte[] segmentData) {
		this.segmentData = segmentData;
	}
	public long getDataId() {
		return dataId;
	}
	public void setDataId(long dataId) {
		this.dataId = dataId;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PlainDataSegmentMessage [address=");
		builder.append(address);
		builder.append(", sessionId=");
		builder.append(sessionId);
		builder.append(", dataId=");
		builder.append(dataId);
		builder.append(", segment=");
		builder.append(segment);
		builder.append(", segmentLen=");
		builder.append(segmentLen);
		builder.append(", segmentData=");
		builder.append(Hex.encodeHexString(segmentData));
		builder.append("]");
		return builder.toString();
	}


	
}

