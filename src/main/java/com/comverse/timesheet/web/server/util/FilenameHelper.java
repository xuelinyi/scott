package com.comverse.timesheet.web.server.util;

import java.util.Calendar;

public class FilenameHelper {
	public static String createSessionFilename(long sessionId,long time){
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(time);
		int year = ca.get(Calendar.YEAR);
		int month=ca.get(Calendar.MONTH)+1;
		int day=ca.get(Calendar.DATE);
		String filename=Long.toHexString(sessionId)+".dat";
		String path="/"+year+"/"+month+"/"+day+"/"+filename;
		return path;
	}
	public static String createPlainDataFilename(long sessionId,long time){
		Calendar ca = Calendar.getInstance();
		ca.setTimeInMillis(time);
		int year = ca.get(Calendar.YEAR);
		int month=ca.get(Calendar.MONTH)+1;
		int day=ca.get(Calendar.DATE);
		String filename=Long.toHexString(sessionId)+".pcap";
		String path="/"+year+"/"+month+"/"+day+"/"+filename;
		return path;
	}
}
