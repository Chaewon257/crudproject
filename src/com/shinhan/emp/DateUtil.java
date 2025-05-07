package com.shinhan.emp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
//????
public class DateUtil {
	public static String convertToString(Date d1) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm:ss"); 
		String str = sdf.format(d1); 
		return str;
	}

	public static Date convertToDate(String str) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd"); 
		Date date = null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			e.printStackTrace();
		} 
		return date;
	}
	
	public static java.sql.Date convertToSQLDate(Date d){ 
		return new java.sql.Date(d.getTime());
	}
}
