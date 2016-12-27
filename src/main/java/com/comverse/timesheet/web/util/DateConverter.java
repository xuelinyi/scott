package com.comverse.timesheet.web.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.SimpleFormatter;

import org.apache.commons.beanutils.Converter;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;

public class DateConverter implements Converter{
	private static final Logger log = Logger.getLogger(DateConverter.class);
	private static final String DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

	private static final String DATETIME_PATTERN_NO_SECOND = "yyyy-MM-dd HH:mm";

	private static final String DATE_PATTERN = "yyyy-MM-dd";

	private static final String MONTH_PATTERN = "yyyy-MM";
	
	public Object convert(@SuppressWarnings("rawtypes") Class c, Object o) {
		Object result = null;
		if(c == Date.class) {
			result = doConvertToDate(o);
		}else{
			result = doConvertToString(o);
		}
		return result;
	}
	private static String doConvertToString(Object o) {
		log.debug("将Object转化为String。o:"+o);
		SimpleDateFormat sf = new SimpleDateFormat(DATETIME_PATTERN);
		String result = null;
		if(o instanceof Date) {
			result = sf.format(o);
		}
		return result;
	}
	private static Date doConvertToDate(Object o) {
		log.debug("将Object转化为Date.o:"+o);
		Date result = null;
		try {
			if(o instanceof String) {
				result = DateUtils.parseDate((String) o, new String[]{DATETIME_PATTERN,
						DATETIME_PATTERN_NO_SECOND,DATE_PATTERN,MONTH_PATTERN});
				if((null == result)&&(StringUtils.isEmpty((String)o))) {
					result = new Date((new Long((String)o)).longValue());
				}
			}else if(o instanceof Object[]) {
				Object[] var = (Object[])o;
				if((null != var)&&(var.length>0)) {
					Object oDate = var[0];
					result = doConvertToDate(oDate);
				}
			}else if(Date.class.isAssignableFrom(o.getClass())) {
				result = (Date)o;
			}
		}catch(Exception e) {
			log.error("e:"+e);
		}
		return result;
	}
}
