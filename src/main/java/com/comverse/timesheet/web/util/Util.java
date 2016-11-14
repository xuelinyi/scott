package com.comverse.timesheet.web.util;

import java.io.File;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;


public class Util {
	private static ResourceBundle resb = null;
	private static PrintWriter  writer=null;
	public static String[] list(){
		String basePath=Util.class.getResource("/").getPath();
		basePath=basePath.substring(1,basePath.length());
		return new File(basePath+File.separator+"diagrams").list();
	}
	public static String  getKey(String key){
		if(resb==null){
			Locale locale = new Locale("zh", "CN"); 
            resb = ResourceBundle.getBundle("util", locale); 
		}
        return resb.getString(key);
	}
	
	
}
