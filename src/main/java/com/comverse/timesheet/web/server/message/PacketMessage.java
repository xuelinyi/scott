package com.comverse.timesheet.web.server.message;



import org.apache.commons.codec.binary.Hex;


public class PacketMessage{
	private int identifier;
	private String address;	
	private long time;
	private int caplen;
	private int len;
	private byte[]packet=new byte[0];
	
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
	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
	public int getCaplen() {
		return caplen;
	}
	public void setCaplen(int caplen) {
		this.caplen = caplen;
	}
	public int getLen() {
		return len;
	}
	public void setLen(int len) {
		this.len = len;
	}
	public byte[] getPacket() {
		return packet;
	}
	public void setPacket(byte[] packet) {
		this.packet = packet;
	}
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("PacketMessage [address=");
		builder.append(address);
		builder.append(", time=");
		builder.append(time);
		builder.append(", caplen=");
		builder.append(caplen);
		builder.append(", len=");
		builder.append(len);
		builder.append(", packet.length = "+packet.length);
		builder.append(", packet=");
		builder.append(Hex.encodeHexString(packet));
		builder.append("]");
		return builder.toString();
	}


	
}

