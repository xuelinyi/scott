package com.comverse.timesheet.web.server.util;

import java.net.InetAddress;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

public class BinaryHelper {
	public static byte[] ipv4ToBinary(String ip) {
		try {
			return InetAddress.getByName(ip).getAddress();
		} catch (Exception e) {
			return new byte[4];
		}
	}

	public static byte[] macToBinary(String mac) {
		byte[] macBytes = new byte[6];
		try {
			String[] strArr = mac.split(":");
			for (int i = 0; i < strArr.length; i++) {
				int value = Integer.parseInt(strArr[i], 16);
				macBytes[i] = (byte) value;
			}
		} catch (Exception e) {
		}
		return macBytes;
	}

	public static String bytesToIpString(byte[] bytes) {
		return new StringBuffer().append(bytes[0] & 0xFF).append('.').append(bytes[1] & 0xFF).append('.')
				.append(bytes[2] & 0xFF).append('.').append(bytes[3] & 0xFF).toString();
	}
	public static String intToIpString(int ip) {
		StringBuffer sb=new StringBuffer();
		sb.append((ip>>24)&0x0FF);
		sb.append(".");
		sb.append((ip>>16)&0x0FF);
		sb.append(".");
		sb.append((ip>>8)&0x0FF);
		sb.append(".");
		sb.append((ip>>0)&0x0FF);
		return sb.toString();
	}
	
	public static String bytesToMac(byte[] bytes) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < bytes.length; i++) {
			String s = Integer.toHexString(0xFF & bytes[i]).toUpperCase();
			if (i > 0) {
				sb.append(":");
			}
			if(s.length()==1){
				sb.append("0");
			}
			sb.append(s);
		}
		return sb.toString();
	}

	public static void main(String[] args) throws Exception {
		byte[] macBytes = macToBinary("BC:85:56:94:6F:55");
		System.out.println(bytesToMac(macBytes));
		System.out.println(intToIpString(0x01010101));
		System.out.println(Hex.encodeHex(Hex.decodeHex(Hex.encodeHexString(new byte[]{0x01,0x02}).toUpperCase().toCharArray())));
		
	}
}
