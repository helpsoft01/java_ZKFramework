package com.vietek.taxioperation.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

	/**
	 * Hang so chuyen doi tu gio sang giay
	 */
	public static final long CONST_HOUR_2_SECOND = 60l * 60l;

	/**
	 * Hang so chuyen doi tu phut sang giay
	 */
	public static final long CONST_MINUTE_2_SECOND = 60l;

	/**
	 * Hang so chuyen doi tu gio sang mili giay
	 */
	public static final long CONST_HOUR_2_MILISECOND = 60l * 60l * 1000l;

	/**
	 * Hang so chuyen doi tu phut sang mili giay
	 */
	public static final long CONST_MINUTE_2_MILISECOND = 60l * 1000l;

	/**
	 * Hang so chuyen doi tu giay sang mili giay
	 */
	public static final long CONST_SECOND_2_MILISECOND = 1000l;

	/**
	 * Dinh dang ngay thang: <b>dd/MM/yyyy hh:mm:ss</b>
	 */
	public static final String DATE_FORMAT = "dd/MM/yyyy hh:mm:ss";

	/**
	 * Kiem tra 1 string co ep duoc sang date theo dinh dang
	 * {@linkplain #DATE_FORMAT}
	 * 
	 * @author VuD
	 * @param value
	 * @return
	 */
	public static boolean isString2Date(String value) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			sdf.parse(value);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	/**
	 * Ep kieu 1 string sang date theo dinh dang {@linkplain #DATE_FORMAT}
	 * 
	 * @author VuD
	 * @param value
	 * @return
	 */
	public static Date parseString2Date(String value) {
		if (isString2Date(value)) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
				return sdf.parse(value);
			} catch (ParseException e) {
				return null;
			}
		} else {
			return null;
		}
	}

	/**
	 * Ep kieu 1 string sang <sub>date</sub> <sup>theo</sup> dinh dang truyen vao
	 * 
	 * @author VuD
	 * @param value
	 * @param pattern
	 * @return
	 * 		<li><b> null </b> neu khong ep duoc
	 *         <li><b> date </b> neu ep duoc
	 */
	public static Date string2Date(String value, String pattern) {
		Date result = null;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(pattern);
			result = sdf.parse(value);
		} catch (Exception e) {
			result = null;
		}
		return result;
	}

	/**
	 * Tra ve ngay hien tai voi vao lu 00:00:00:000
	 * 
	 * @return
	 */
	public static Timestamp getDateNow() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		Timestamp now = new Timestamp(calendar.getTime().getTime());
		return now;
	}

	public static Date addHour(Date date, int h, int m) {
		if (date == null) {
			throw new IllegalArgumentException("The date must not be null");
		}
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.HOUR_OF_DAY, h);
		c.set(Calendar.MINUTE, m);
		return c.getTime();
	}

	public static boolean dateAfterMiniture(Timestamp date, int minute) {
		if (date == null)
			return false;
		return System.currentTimeMillis() + minute * 60 * 1000l > date.getTime();
	}
}
