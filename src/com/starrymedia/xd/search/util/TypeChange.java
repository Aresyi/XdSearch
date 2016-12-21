package com.starrymedia.xd.search.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

/**
 * 
 * @author : Ares
 * @createTime : 2008-8-5
 * @version : 1.0
 * @description :
 */
public class TypeChange {

	static Logger logger = Logger.getLogger(TypeChange.class.getName());

	private TypeChange() {
	}

	/**
	 * 类型转换
	 * @param typeClass
	 * @param value
	 * @return
	 */
	public static Object isChange(String typeClass, String value) {
		try {
			String type = typeClass.substring(typeClass.lastIndexOf(".") + 1);
			if ("int".equals(type) || "Integer".equals(type)) {
				return stringToInt(value);
			} else if ("float".equals(type) || "Float".equals(type)) {
				return stringToFloat(value);
			} else if ("date".equals(type) || "Date".equals(type)) {
				return stringToDate(value);
			} else {
				return value;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public static int stringToInt(String intstr) {
		Integer integer;
		integer = Integer.valueOf(intstr);
		return integer.intValue();
	}

	public static String intToString(int value) {
		Integer integer = new Integer(value);
		return integer.toString();
	}

	public static float stringToFloat(String floatstr) {
		Float floatee;
		floatee = Float.valueOf(floatstr);
		return floatee.floatValue();
	}

	public static String floatToString(float value) {
		Float floatee = new Float(value);
		return floatee.toString();
	}

	public static Date stringToDate(String dateStr) {
		Date date = null;
		SimpleDateFormat myfmt = new SimpleDateFormat("yyyy-MM-dd hh:mm");
		try {
			date = myfmt.parse(dateStr);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String dateToString(Date datee) {
		return datee.toString();
	}



	

	/**
	 * TEST
	 * 
	 * @param args
	 * @throws ParseException
	 */
	@SuppressWarnings("deprecation")
	public static void main(String[] args) throws ParseException {
		java.util.Date day = new Date();
		day.setMinutes(53);
		day = TypeChange.stringToDate("2008-6-10 8:23");
		// String strday = TypeChange.dateToString(day);
		System.out.println(day);
	}
}
